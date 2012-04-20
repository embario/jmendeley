package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.NoSuchAlgorithmException;
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
	/** The connection to the ArXiv Connection Manager **/
	private ArXivConnectionManager arXiv;
	/** Our reference to the singleton AccountManager **/
	private AccountManager acm;
	/** Our reference to the singleton Authentication Manager **/
	private AuthenticationManager am;
	
	private SearchManager(AccountManager acm, AuthenticationManager am) {
		this.acm = acm;
		this.am = am;
		arXiv = new ArXivConnectionManager();
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

	
	public void search() {
		
		Scanner scn = new Scanner(System.in);
		System.out.println("Please enter your search term.");
		String searchTerm = scn.nextLine();
		
		List<Paper> papers = arXiv.search(searchTerm, null, null, 5);
		
		for(Paper p : papers) {
			System.out.println(p.summarize());
			System.out.println();
		}
		
		System.out.println("Do you wish to add these papers to your Mendeley account? (yes/no)");
		
		if(scn.nextLine().equalsIgnoreCase("yes")) {
			
			for(Paper p : papers) {
				
				try {
					Response response = am.sendRequest(Verb.POST, "http://api.mendeley.com/oapi/library/documents?document=" + URLEncoder.encode(p.toJSON().toString().trim(), "UTF-8").replace("+", "%20"));
					JSONObject docIDObj = new JSONObject(response.getBody());
					String id = docIDObj.getString("document_id");
					
				    byte[] fileBytes = ByteStreams.toByteArray(p.pdf.openStream());
				    
				    String sha = SHASum.SHASum(fileBytes);
					
					OAuthRequest request = new OAuthRequest(Verb.PUT, String.format("http://www.mendeley.com/oapi/library/documents/%s/",id));
					request.addOAuthParameter("oauth_body_hash", sha);
					request.addPayload(fileBytes);
					response = am.sendRequest(request);
					if(response.getCode() == 201)
						System.out.println("\"" + p.title + "\" was uploaded successfully.");
					else 
						System.out.println(String.format("Error %d: \"%s\" failed to upload [%s]",response.getCode(),p.title,response.getBody()));
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
