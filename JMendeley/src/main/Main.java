package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONException;

public class Main {
	
	private static final String CONSUMER_KEY = "cfc24e1782a13e619030a531177df76504f811506";
	private static final String CONSUMER_SECRET = "f7aac3649ec6f18f7d7a2cc7c3f7f3d9";
	protected static Scanner infile = null;
	
	public static void main(String args[]) throws FileNotFoundException, IOException, JSONException {
		// start up UI, ask user for info, etc
		// for now, just connect to the mand
		
		// 
		AuthenticationManager am = AuthenticationManager.getInstance(CONSUMER_KEY, CONSUMER_SECRET);
		if (am.connectToMendeley() == true){
			
			//Create the AccountManager singleton that will use the AuthenticationManager to search
			//for the profile information (Account) for the authenticated user.
			AccountManager acm = AccountManager.getInstance(am);			
			System.out.println(acm);
			
		}
	}
	
}
