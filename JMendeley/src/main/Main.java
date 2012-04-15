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
	private static final String CONSUMER_KEY = "cfc24e1782a13e619030a531177df76504f811506";
	private static final String CONSUMER_SECRET = "f7aac3649ec6f18f7d7a2cc7c3f7f3d9";
	
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
}
