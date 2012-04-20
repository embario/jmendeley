package main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.scribe.model.Response;
import org.scribe.model.Verb;

public class MendeleySearchInterface implements LibrarySearchInterface {
	
	/** Our singleton object **/
	private static MendeleySearchInterface _singleton = null;
	/** Reference to the authentication manager singleton */
	private AuthenticationManager auth;
	
	
	private MendeleySearchInterface(AuthenticationManager auth) {
		this.auth = auth;
	}
	
	
	public static MendeleySearchInterface getInstance(AuthenticationManager auth) {
		
		if (_singleton == null)
			_singleton = new MendeleySearchInterface(auth);
		
		return _singleton;
	}
	
	/**
	 * The method search() searches.
	 * @param searchTerm
	 * @param title
	 * @param author
	 * @param maxResults
	 * @return
	 */
	public List<Paper> search(String searchTerm, String title, String author, int maxResults) {
		String searchURL = String.format("http://api.mendeley.com/oapi/documents/search/%s/?items=%d&consumer_key=cfc24e1782a13e619030a531177df76504f811506", buildSearch(searchTerm, title, author), maxResults);
		System.out.println(searchURL);
		Response response = auth.sendPublicRequest(Verb.GET, searchURL);
		
		System.out.println(response.getBody());
		
		return null;
	}
	
	private String buildSearch(String all, String title, String author) {
		try {
			all = (all==null)?null:URLEncoder.encode(all, "UTF-8").replace("+", "%20");
			title = (title==null)?null:URLEncoder.encode(title, "UTF-8").replace("+", "%20");
			author = (author==null)?null:URLEncoder.encode(author, "UTF-8").replace("+", "%20");

			StringBuilder term = new StringBuilder();
			boolean alle = true, tie = true;
			if(all == null || all.equals(""))
				alle = false;
			else term.append(all);

			if(title == null || title.equals(""))
				tie = false;
			else {
				if(alle)
					term.append("%20");
				term.append("title:"+title);
			}

			if(author != null && !author.equals("")) {
				if(alle || tie)
					term.append("%20");
				term.append("author:"+author);
			}

			return term.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
