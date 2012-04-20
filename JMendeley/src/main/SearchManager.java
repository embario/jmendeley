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

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import com.google.common.io.ByteStreams;

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

	
	public void searchForPapers() {
		
		Scanner scn = new Scanner(System.in);
		System.out.print("Please enter your search term: ");
		String searchTerm = scn.nextLine();
		System.out.print("How many results?: ");
		int maxResults = Integer.parseInt(scn.nextLine());
		System.out.println("Type 'm' if you want to search Mendeley. Type 'a' if you want to search arXiv. Type both if you want both.");
		String choice = scn.nextLine();
		
		ArrayList <ConnectionStrategy> connections = this._connections;
	
		if (choice.trim().contains("m") == true)
			connections.add(new MendeleyConnectionStrategy(this.am));
		if (choice.trim().contains("a") == true)
			connections.add(new ArXivConnectionStrategy());
			
		ArrayList <Paper> papers = new ArrayList <Paper> ();
		
		//Let the concurrency begin (here).
		for (ConnectionStrategy s : connections){
			
			ArrayList <Paper> thesePapers = (ArrayList<Paper>) s.search(searchTerm, null, null, maxResults);
			
			if(thesePapers == null)
				continue;
			
			System.out.println(s.identifyConnection());
			
			for (Paper p : thesePapers){
				System.out.println(p.summarize());
				System.out.println();	
			}
			
			//Add the papers found to the list.
			papers.addAll(thesePapers);
			
		}
		
		sendPapersToMendeley(scn, papers);
	}


	private boolean sendPapersToMendeley(Scanner scn, List<Paper> papers) {
		
		System.out.println("Do you wish to add these papers to your Mendeley account? (yes/no)");
		
		if (scn.nextLine().equalsIgnoreCase("yes")) {
			
			for (Paper p : papers) {
				
				try {
					
					//First, encode the paper JSON Object for the URL.
					String encodedURL = URLEncoder.encode(p.toJSON().toString().trim(), "UTR-8").replace("+", "%20");
					
					//Craft the response, POST-it to Mendeley, and get the Document ID back.
					Response response = am.sendRequest(Verb.POST, "http://api.mendeley.com/oapi/library/documents?document=" + encodedURL);
					JSONObject docIDObj = new JSONObject(response.getBody());
					String id = docIDObj.getString("document_id");
					
					//Convert the PDF to bytes, encrypt with SHA.
				    byte[] fileBytes = ByteStreams.toByteArray(p.pdf.openStream());
				    String sha = SHASum.SHASum(fileBytes);
					
				    //Now, send off the PDF bytes off to specified document PUT request.
					OAuthRequest request = new OAuthRequest(Verb.PUT, String.format("http://www.mendeley.com/oapi/library/documents/%s/",id));
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
			
			return true;
		}
		
		return false;
	}//end send method

}
