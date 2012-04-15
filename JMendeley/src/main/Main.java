package main;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import util.MendeleyApi;

public class Main {
	private static final String CONSUMER_KEY = "b60776803ae54e6b23a3bacac395aa6104f85d33a";
	private static final String CONSUMER_SECRET = "448b52f707d379962edce3857afc1267";
	
	private static OAuthService service;
	private static Token access;
	
	public static void main(String args[]) {
		// start up UI, ask user for info, etc
		// for now, just connect to the mand
		connectToMendeley();
	}
	
	public static void connectToMendeley() {
		// For reading input
		Scanner scn = new Scanner(System.in);

		// Build OAuth service
		service = new ServiceBuilder().provider(MendeleyApi.class)
				.apiKey(CONSUMER_KEY)
				.apiSecret(CONSUMER_SECRET).build();

		// Request access to Mendeley
		Token requestToken = service.getRequestToken();

		// Retrieve the URL for Mendeley authorization, direct user there,
		// wait for response, construct verifier
		String authURL = service.getAuthorizationUrl(requestToken);
		System.out.println("Go to " + authURL);
		System.out.print("Enter verification code: ");
		Verifier verify = new Verifier(scn.nextLine());
		
		access = service.getAccessToken(requestToken, verify);
	}

	private void referenceToOldStuff() {
		// For reading input
		Scanner scn = new Scanner(System.in);

		// Build OAuth service
		OAuthService service = new ServiceBuilder().provider(MendeleyApi.class)
				.apiSecret("448b52f707d379962edce3857afc1267")
				.apiKey("b60776803ae54e6b23a3bacac395aa6104f85d33a").build();

		// Request access to Mendeley
		Token requestToken = service.getRequestToken();

		// Retrieve the URL for Mendeley authorization, direct user there,
		// wait for response, construct verifier
		String authURL = service.getAuthorizationUrl(requestToken);
		System.out.println("Go to " + authURL);
		System.out.print("Enter verification code: ");
		Verifier verify = new Verifier(scn.nextLine());

		// Get access token
		Token accessToken = service.getAccessToken(requestToken, verify);

		// Request my document library ids and sign with access token
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://api.mendeley.com/oapi/library?items=25");
		service.signRequest(accessToken, request);
		Response response = request.send();

		// Print output
		System.out.println("Output:\n" + response.getBody());

	}
}
