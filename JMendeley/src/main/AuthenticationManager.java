package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import util.MendeleyApi;


/**
 * The AuthenticationManager serves as the gateway to allowing access to the user's Mendeley Account.
 * 
 * @author mbarrenecheajr, annielala, mvitousek
 *
 */
public class AuthenticationManager {

	/** Our Singleton Manager **/
	private static AuthenticationManager _singleton = null;

	/** Consumer Key is used to retrieve the Access Token when authenticating a User for Mendeley access. **/
	private String _consumer_key = null;
	/** Consumer Secret is used for verifying the consumer key. **/
	private String _consumer_secret = null;
	/** File that contains the Access Token and Access secret for signing user-specific 
	 * method requests when interfacing with the Mendeley API **/
	private File _token_file = null;
	/** Flag for determining if connection has been made already **/
	private boolean _isConnected = false;

	/** OAuth service that performs the magic we need to provide access to the Mendeley User. **/
	private OAuthService _oauthService = null;
	/** The access token for signing method requests **/
	private Token _accessToken = null;
	

	// Utility variables
	private Scanner _infile = null;

	/**
	 * Private Constructor for the AM. The OAuth ServiceBuilder object is constructed here, given the consumer key and secret variables.
	 * Note that the subsequent connectToMendeley() function must be called in order to allow access to Mendeley.
	 * @param consumer_key
	 * @param consumer_secret
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private AuthenticationManager(String consumer_key, String consumer_secret)
			throws FileNotFoundException, IOException {

		this._consumer_key = consumer_key;
		this._consumer_secret = consumer_secret;

		// Build OAuth service
		this._oauthService = new ServiceBuilder().provider(MendeleyApi.class)
				.apiKey(this._consumer_key).apiSecret(this._consumer_secret)
				.build();
	}
	
	public boolean isConnected() { return this._isConnected;}

	/**
	 * 
	 * This private method allows for the AM to connect to Mendeley using the OAuth ServiceBuilder. First, it tries
	 * to find a token file within the project directory. If it's there, create the access Token and return TRUE. Otherwise,
	 * create the token file for later reference and send the token request to Mendeley. Upon receiving the access token, return TRUE.
	 *  
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean connectToMendeley() throws FileNotFoundException, IOException {

		Token token_file = null;

		// Refer to the Token file; check if it exists
		File tokenfile = this._token_file = new File("./usr/jmendeley_token");

		try {

			if (tokenfile.exists() == true) {

				this._infile = Main.infile = new Scanner(this._token_file);
				
				//Grab the token and secret from the existing token file.
				String token = this._infile.nextLine();
				String secret = this._infile.nextLine();
				
				//Keep the access token for later; we are now connected.
				this._accessToken = new Token(token, secret);
				this._isConnected = true;
				return true;
				
			} else
				this._token_file.createNewFile();

		} catch (Exception e) {
			this._token_file.createNewFile();
		}

		// For reading input
		this._infile = new Scanner(System.in);

		// Request access to Mendeley
		Token requestToken = this._oauthService.getRequestToken();

		// Retrieve the URL for Mendeley authorization, direct user there,
		// wait for response, construct verifier
		String authURL = this._oauthService.getAuthorizationUrl(requestToken);
		System.out.println("Go to " + authURL);
		System.out.print("Enter verification code: ");
		Verifier verify = new Verifier(this._infile.nextLine());

		try { token_file = this._oauthService.getAccessToken(requestToken, verify); }
		catch (OAuthException oaex) {
			
			System.err.println("The Token File is incomplete/corrupt. Please make sure that the verification code is entered correctly.");
			this._isConnected = false;
			return false;
		}
		
		// Print token information to file.
		FileWriter fw = new FileWriter(this._token_file);
		fw.write(token_file.getToken() + "\n");
		fw.write(token_file.getSecret());
		fw.close();

		this._accessToken = token_file;
		this._isConnected = true;
		return true;
	}

	 public static AuthenticationManager getInstance(String consumer_key,
			String consumer_secret) throws FileNotFoundException, IOException {

		return (_singleton == null) ? new AuthenticationManager(consumer_key,
				consumer_secret) : _singleton;

	}

	public void testDownloads() {
		// Request my document library ids and sign with access token
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.mendeley.com/oapi/library?items=25");
		this._oauthService.signRequest(this._accessToken, request);

		Response response = request.send();
		try {
			JSONObject results = new JSONObject(response.getBody());
			JSONArray ids = results.getJSONArray("document_ids");
			System.out.println("First 25 document IDs: ");
			for (int i = 0; i < ids.length(); i++) {
				System.out.println(ids.getString(i));
			}
			System.out.println("..." + (results.getInt("total_pages") - results.getInt("current_page") - 1) + " more pages");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}// end class
