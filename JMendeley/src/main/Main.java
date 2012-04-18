package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	private static final String CONSUMER_KEY = "cfc24e1782a13e619030a531177df76504f811506";
	private static final String CONSUMER_SECRET = "f7aac3649ec6f18f7d7a2cc7c3f7f3d9";
	protected static Scanner infile = null;
	
	public static void main(String args[]) throws FileNotFoundException, IOException {
		// start up UI, ask user for info, etc
		// for now, just connect to the mand
		
		// Build OAuth service
		AuthenticationManager am = AuthenticationManager.getInstance(CONSUMER_KEY, CONSUMER_SECRET);
		if (am.connectToMendeley() == true){
			
			am.testDownloads();
			
		}
		
	}
	
}
