package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import util.MendeleyApi;

public class Main {
	private static final String CONSUMER_KEY = "cfc24e1782a13e619030a531177df76504f811506";
	private static final String CONSUMER_SECRET = "f7aac3649ec6f18f7d7a2cc7c3f7f3d9";
	private static final File TOKEN_FILE = new File("token");
	
	private static OAuthService service;
	private static Token access;
	
	public static void main(String args[]) {
		DocumentBuilder db;
		
		// start up UI, ask user for info, etc
		// for now, just connect to the mand
		
		// Build OAuth service
		service = new ServiceBuilder().provider(MendeleyApi.class)
				.apiKey(CONSUMER_KEY)
				.apiSecret(CONSUMER_SECRET).build();
		access = ConnectToMendeley();
		
		TestDownloads();
	}
	
	private static void TestDownloads() {
		// Request my document library ids and sign with access token
		OAuthRequest request = new OAuthRequest(Verb.GET, 
				"http://api.mendeley.com/oapi/library?items=25");
		service.signRequest(access, request);
		Response response = request.send();
		try {
			JSONObject results = new JSONObject(response.getBody());
			JSONArray ids = results.getJSONArray("document_ids");
			System.out.println("First 25 document IDs: ");
			for(int i = 0; i < ids.length(); i++) {
				System.out.println(ids.getString(i));
			}
			System.out.println("..." + (results.getInt("total_pages") - results.getInt("current_page") - 1) + " more pages");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static Token ConnectToMendeley() {
		boolean couldCreate = true;
		
		if (TOKEN_FILE.exists()) {
			try {
				Scanner tokenReader = new Scanner(TOKEN_FILE);
				String token = tokenReader.nextLine();
				String secret = tokenReader.nextLine();
				return new Token(token, secret);
			} catch (FileNotFoundException e) { 
				// Shouldn't happen due to previous check
			}
		} else {
			try {
				TOKEN_FILE.createNewFile();
			} catch (IOException e1) {
				System.err.println("Unable to create persistent token file -- you will have to reauthorize with " +
						"Mendeley on each launch.");
				couldCreate = false;
			}
		}
		
		// For reading input
		Scanner scn = new Scanner(System.in);

		// Request access to Mendeley
		Token requestToken = service.getRequestToken();

		// Retrieve the URL for Mendeley authorization, direct user there,
		// wait for response, construct verifier
		String authURL = service.getAuthorizationUrl(requestToken);
		System.out.println("Go to " + authURL);
		System.out.print("Enter verification code: ");
		Verifier verify = new Verifier(scn.nextLine());
		
		Token access = service.getAccessToken(requestToken, verify);
		
		// Print token information to file.
		BufferedWriter f;
		try {
			f = new BufferedWriter(new FileWriter(TOKEN_FILE));
			f.write(access.getToken());
			f.write("\n");
			f.write(access.getSecret());
			f.close();
		} catch (IOException e) {
			if(couldCreate) // Don't print duplicate error messages
				System.err.println("Unable to write token information to file -- you will have to reauthorize with " +
					"Mendeley on next launch.");
			TOKEN_FILE.delete(); // Don't want a corrupted/incomplete token file lying around
		}
		
		return access;
	}
}
