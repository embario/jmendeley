package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import com.google.common.io.ByteStreams;

import util.JMendeleyApiUrls;
import util.SHASum;

/**
 * 
 * This SearchManager class is responsible for connecting to the various Online Digital Library APIs out there.
 * @author mbarrenecheajr, annielala, mvitousek
 *
 */
public class SearchManager {

	/** our Singleton Manager **/
	private static SearchManager _singleton;
	/** The references to LibrarySearchInterfaces **/
	private ConnectionStrategy arXiv, mendeley;
	private ArrayList <ConnectionStrategy> _connections = null;
	/** Our reference to the singleton AccountManager **/
	private AccountManager acm;
	/** Our reference to the singleton Authentication Manager **/
	private AuthenticationManager am;

	/** Flag for flipping concurrency on/off **/
	private final boolean _concurrent = true;


	private SearchManager(AccountManager acm, AuthenticationManager am) {
		this.acm = acm;
		this.am = am;
		this._connections = new ArrayList <ConnectionStrategy> ();
	}

	/**
	 * Our singleton factory method. We return a singleton object if it exists; 
	 * create just one and return it if it doesn't.
	 * @param acm
	 * @param am
	 * @return
	 */
	public static SearchManager getInstance(AccountManager acm, AuthenticationManager am) {

		if (_singleton == null)	
			_singleton = new SearchManager(acm, am);

		return _singleton;
	}


	/**
	 * This method accepts a String array of search refinements (including basic search term) and an array
	 * of ConnectionStrategies, and it performs a search based on these parameters.
	 * 
	 * The body of the search terms are as follows (in this order):
	 * -Search
	 * -Title
	 * -Author
	 * -Year
	 * -Publication Reference
	 * 
	 * @param terms
	 * @param connections
	 * @throws JSONException
	 */
	public List <Paper> searchForPapers(final ArrayList <String> terms, ArrayList <ConnectionStrategy> connections, final int numResults) {

		ArrayList <Paper> papers = new ArrayList <Paper> ();


		try {

			int numCores = Runtime.getRuntime().availableProcessors();
			double blockingCoefficientSearch = 0.9;
			int searchPoolSize = Math.min((int) (numCores / (1. - blockingCoefficientSearch)), connections.size());

			List<Callable<List<Paper>>> searchTasks = new ArrayList<Callable<List<Paper>>>(connections.size());

			//The work for downloading the PDFs
			for(final ConnectionStrategy s : connections) {
				searchTasks.add(new Callable<List<Paper>>() {
					public List<Paper> call() throws Exception {
						ArrayList <Paper> thesePapers = (ArrayList<Paper>) s.search(terms, numResults);

						if(thesePapers == null)
							return new ArrayList<Paper>();

						System.out.println(s.identifyConnection());
						
						return thesePapers;
					}
				});
			}

			//The ExecutorService for concurrently searching
			final ExecutorService searchExecutorPool = Executors.newFixedThreadPool(searchPoolSize);
			final List<Future<List<Paper>>> searchResults = searchExecutorPool.invokeAll(searchTasks, 10000, TimeUnit.SECONDS);

			for(final Future<List<Paper>> result : searchResults) {
				List<Paper> thesePapers = result.get();
				for (Paper p : thesePapers){
					System.out.println(p.summarize());
					System.out.println();	
				}
				papers.addAll(thesePapers);
			}

			/*for (ConnectionStrategy s : connections){

				ArrayList <Paper> thesePapers = (ArrayList<Paper>) s.search(terms, numResults);

				if(thesePapers == null)
					continue;

				System.out.println(s.identifyConnection());
				for (Paper p : thesePapers){
					System.out.println(p.summarize());
					System.out.println();	
				}

				//Add the papers found to the list.
				papers.addAll(thesePapers);

			}//end for loop
			 */			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return papers;
	}



	private boolean sendPapersToMendeley(List<Paper> papers) throws InterruptedException, ExecutionException {

		long init = 0;

		//If we want to send papers...
		if (this._concurrent) {

			//Start the timer.
			init = System.currentTimeMillis();

			//Separate the Papers with PDFS from those without them.
			ArrayList<Paper> pdfPapers = new ArrayList<Paper>(papers.size());

			for (Paper p : papers) 
				if(p.pdf != null)
					pdfPapers.add(p);

			/*
			 * Concurrency: 
			 * 
			 * 1) Download PDFs
			 * 2) Calculate SHA-1 for all PDFs
			 * 3) Upload PDFs to Mendeley Account
			 * 
			 */

			//Prepare to concurrently download PDFs.
			int numCores = Runtime.getRuntime().availableProcessors();
			double blockingCoefficientDownload = 0.9;
			int downloadPoolSize = (int) (numCores / (1. - blockingCoefficientDownload));

			List<Callable<Void>> downloadTasks = new ArrayList<Callable<Void>>(pdfPapers.size());

			//The work for downloading the PDFs
			for(final Paper p : pdfPapers) {
				downloadTasks.add(new Callable<Void>() {
					public Void call() throws Exception {
						p.file = ByteStreams.toByteArray(p.pdf.openStream());
						System.out.println(p.title + " PDF downloaded...");
						return null;
					}
				});
			}

			System.out.println("Downloading PDFs...");

			//The ExecutorService for concurrently downloading the PDFs
			final ExecutorService downloadExecutorPool = Executors.newFixedThreadPool(downloadPoolSize);
			final List<Future<Void>> downloadResults = downloadExecutorPool.invokeAll(downloadTasks, 10000, TimeUnit.SECONDS);

			for(final Future<Void> result : downloadResults)
				result.get();


			System.out.println("PDFs downloaded...");

			//Then, prepare to concurrently calculate SHA-1s.
			int shaPoolSize = numCores;
			List<Callable<Void>> shaTasks = new ArrayList<Callable<Void>>(pdfPapers.size());

			//The work for concurrently calculating SHA-1s
			for(final Paper p : pdfPapers) {
				shaTasks.add(new Callable<Void>() {
					public Void call() throws Exception {
						p.sha = SHASum.SHASum(p.file);
						System.out.println("SHA1 for " + p.title + " calculated...");
						return null;
					}
				});
			}

			System.out.println("Calculating SHA1s...");

			//The ExecutorService for concurrently calculating SHA-1s.
			final ExecutorService shaExecutorPool = Executors.newFixedThreadPool(shaPoolSize);
			final List<Future<Void>> shaResults = shaExecutorPool.invokeAll(shaTasks, 10000, TimeUnit.SECONDS);

			for(final Future<Void> result : shaResults)
				result.get();


			System.out.println("SHA1s calculated...");

			//Finally, prepare for concurrently uploading PDFs.
			double blockingCoefficientUpload = 0.9;
			int uploadPoolSize = (int) (numCores / (1. - blockingCoefficientUpload));
			List<Callable<Response>> uploadTasks = new ArrayList<Callable<Response>>(papers.size());

			//The work for concurrently uploading PDFs.
			for(final Paper p : papers) {
				uploadTasks.add(new Callable<Response>() {
					public Response call() throws Exception {

						//First, encode the paper JSON Object for the URL.
						String encodedURL = URLEncoder.encode(p.toJSON().toString().trim(), "UTF-8").replace("+", "%20");

						//Craft the response, POST-it to Mendeley, and get the Document ID back.
						Response response = am.sendRequest(Verb.POST, JMendeleyApiUrls.USER_POST_DOCUMENT_URL + encodedURL);
						System.out.println("Metadata for " + p.title + " uploaded...");
						JSONObject docIDObj = new JSONObject(response.getBody());
						String id = docIDObj.getString("document_id");

						if(p.pdf != null) {
							//Now, send off the PDF bytes off to specified document PUT request.
							OAuthRequest request = new OAuthRequest(Verb.PUT, String.format(JMendeleyApiUrls.USER_PUT_DOCUMENT_PDF_URL,id));

							request.addOAuthParameter("oauth_body_hash", p.sha);
							request.addPayload(p.file);
							response = am.sendRequest(request);
							System.out.println("PDF for " + p.title + " uploaded...");
						}

						return response;
					}
				});
			}

			System.out.println("Uploading paper information...");

			//The ExecutorService for concurrently uploading PDFs.
			final ExecutorService uploadExecutorPool = Executors.newFixedThreadPool(uploadPoolSize);
			final List<Future<Response>> uploadResults = uploadExecutorPool.invokeAll(uploadTasks, 10000, TimeUnit.SECONDS);

			for(final Future<Response> result : uploadResults) 
				result.get();

			System.out.println("Paper information uploaded. Process complete.");

			//return true;

		} else if(this._concurrent == false) {

			System.out.println("proceeding on slow (sequential) path");
			init = System.currentTimeMillis();
			for (Paper p : papers) {

				try {

					//First, encode the paper JSON Object for the URL.
					String encodedURL = URLEncoder.encode(p.toJSON().toString().trim(), "UTF-8").replace("+", "%20");

					//Craft the response, POST-it to Mendeley, and get the Document ID back.
					Response response = am.sendRequest(Verb.POST, JMendeleyApiUrls.USER_POST_DOCUMENT_URL + encodedURL);
					JSONObject docIDObj = new JSONObject(response.getBody());
					String id = docIDObj.getString("document_id");

					//Download the PDF, compute SHA checksum.
					byte[] fileBytes = ByteStreams.toByteArray(p.pdf.openStream());
					String sha = SHASum.SHASum(fileBytes);

					//Now, send off the PDF bytes off to specified document PUT request.
					OAuthRequest request = new OAuthRequest(Verb.PUT, String.format(JMendeleyApiUrls.USER_PUT_DOCUMENT_PDF_URL,id));

					request.addOAuthParameter("oauth_body_hash", sha);
					request.addPayload(fileBytes);
					response = am.sendRequest(request);

					//If the status is good:
					if(response.getCode() == 201)
						System.out.println("\"" + p.title + "\" was uploaded successfully.");
					else 
						System.out.println(String.format("Error %d: \"%s\" failed to upload [%s]",response.getCode(),p.title,response.getBody()));

				} catch (Exception e) { e.printStackTrace();}
			}
		}
		System.out.println(System.currentTimeMillis() - init);
		//56040 without concurrency for 20 arXiv papers with pdfs
		//15530 with concurrency
		// hell. fuckin. yes.

		return false;
	}//end send method

}
