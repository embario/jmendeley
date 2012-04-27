package main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import util.JMendeleyApiUrls;

public class MendeleyConnectionStrategy implements ConnectionStrategy {
	
	/** Reference to the authentication manager singleton */
	private AuthenticationManager auth;
	
	public MendeleyConnectionStrategy(AuthenticationManager auth) {
		this.auth = auth;
	}
	
	
	/**
	 * The method search() searches.
	 * @param searchTerm
	 * @param title
	 * @param author
	 * @param maxResults
	 * @return
	 * @throws JSONException 
	 */
	public List<Paper> search(ArrayList <String> terms, int maxResults) throws JSONException {
	
		String searchURL = String.format(JMendeleyApiUrls.PUBLIC_GET_SEARCH_FOR_DOCUMENTS, buildSearch(terms), maxResults);

		Response response = auth.sendPublicRequest(Verb.GET, searchURL);
		ArrayList<Paper> papers = new ArrayList<Paper>();
		
		try {
			JSONObject results = new JSONObject(response.getBody());
			JSONArray documents = results.getJSONArray("documents");
			for(int i = 0; i < documents.length(); i++) {
				JSONObject doc = documents.getJSONObject(i);
				String uuid = doc.getString("uuid");
				String docURL = String.format(JMendeleyApiUrls.PUBLIC_GET_DOCUMENT_DETAILS, uuid);
				
				response = auth.sendPublicRequest(Verb.GET, docURL);
				
				doc = new JSONObject(response.getBody());
				Paper p = new Paper();
				
				if(doc.has("abstract"))
					p.abst = doc.getString("abstract");
				
				JSONArray authors = doc.getJSONArray("authors");
				String[] docAuthors = new String[authors.length()];
				for(int j = 0; j < authors.length(); j++) {
					JSONObject docAuthor = authors.getJSONObject(j);
					docAuthors[j] = docAuthor.getString("forename") + " " + docAuthor.getString("surname");
				}
				p.authors = docAuthors;
				
				p.title = doc.getString("title");
				
				if(doc.has("publication_outlet")) {
					p.venue = doc.getString("publication_outlet");
					p.type = "Journal Article";
				} else p.type = "Generic";
				
				p.year = doc.getInt("year") + "";
				
				papers.add(p);
			}
			
		} catch (JSONException e) { e.printStackTrace(); }
		
		return papers;
	}
	
	public  String buildSearch(ArrayList <String> terms) {
		
		String searchTerm = " ";
		
		try {
			
			for (int i = 0; i < terms.size(); i++){
				
					String term = terms.get(i);
					
					//Deal with no term.
					if (term.equals("") == true)
						continue;
					
					//URL Encode.
					//term = URLEncoder.encode(term, "UTF-8").replace("+", "%20");
					
					if (term.contains(JMendeleyApiUrls.JMEND_SEARCH_TERM) == true)
						searchTerm += JMendeleyApiUrls.MENDELEY_SEARCH_TERM;
					else if(term.contains(JMendeleyApiUrls.JMEND_TITLE_TERM) == true)
						searchTerm += JMendeleyApiUrls.MENDELEY_TITLE_TERM;
					else if(term.contains(JMendeleyApiUrls.JMEND_AUTHOR_TERM) == true)
						searchTerm += JMendeleyApiUrls.MENDELEY_AUTHOR_TERM;
					else if(term.contains(JMendeleyApiUrls.JMEND_YEAR_TERM) == true)
						searchTerm += JMendeleyApiUrls.MENDELEY_YEAR_TERM;
					else if(term.contains(JMendeleyApiUrls.JMEND_PUBREF_TERM) == true)
						searchTerm += JMendeleyApiUrls.MENDELEY_PUBREF_TERM;
					
					//Should be only TWO terms - the prefix and the actual search term.
					String [] thingToSplit = term.split("([a-zA-Z]+:)");
					for (int j = 1; j < thingToSplit.length; j++)
						searchTerm += URLEncoder.encode(thingToSplit[j], "UTF-8").replace("+", "%20");
					
					//Must add a Mendeley-specific conjunction here (if need be).
					if (i != terms.size() - 1)
						searchTerm += "%20";
				
			}
			
			return searchTerm;
			
			/*
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

			return term.toString(); */
			
		} catch (UnsupportedEncodingException e) { e.printStackTrace();}
		return null;
	}
	
	
	public String identifyConnection(){
		
		return "============================\nImported from Mendeley: \n";
		
		
	}

}
