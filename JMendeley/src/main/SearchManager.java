package main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

public class SearchManager {
	private static SearchManager _singleton;
	
	public static SearchManager getInstance(AccountManager acm,
			AuthenticationManager am) {
		return (_singleton == null) ? new SearchManager(acm, am) : _singleton;
	}

	private ArXivConnectionManager arXiv;
	private AccountManager acm;
	private AuthenticationManager am;
	
	private SearchManager(AccountManager acm, AuthenticationManager am) {
		this.acm = acm;
		this.am = am;
		arXiv = new ArXivConnectionManager();
		_singleton = this;
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
					System.out.println(response.getBody());
				} catch (UnsupportedEncodingException e) {}
				
				
			}
		}
	}

}
