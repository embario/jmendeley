package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ArXivConnectionStrategy implements ConnectionStrategy {

	public ArXivConnectionStrategy() { }
	
	/**
	 * The method search() searches.
	 * @param searchTerm
	 * @param title
	 * @param author
	 * @param maxResults
	 * @return
	 */
	public List<Paper> search(ArrayList <String> terms, int maxResults) {
		
		try {
			
			final URL url = new URL(String.format("http://export.arxiv.org/api/query?search_query=%s&start=0&max_results=%d", buildSearch(terms), maxResults));
			final InputStream stream = url.openStream();
			System.out.println(url);

			final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document results = db.parse(stream);

			ArrayList<Paper> papers = new ArrayList<Paper>(maxResults);

			Element doc = results.getDocumentElement();
			NodeList entries = doc.getElementsByTagName("entry");
			
			for(int i = 0; i < entries.getLength(); i++) {
				Paper p = new Paper();
				p.venue = "";
				p.type = "Generic";

				Element entry = (Element) entries.item(i);

				Element titleElement = (Element) entry.getElementsByTagName("title").item(0);
				p.title = titleElement.getTextContent();

				NodeList authors = entry.getElementsByTagName("author");
				String[] authorNames = new String[authors.getLength()];
				for(int j = 0; j < authors.getLength(); j++) {
					Element authorElement = (Element) authors.item(j);
					Element authorName = (Element) authorElement.getElementsByTagName("name").item(0);
					authorNames[j] = authorName.getTextContent();
				}
				p.authors = authorNames;

				NodeList links = entry.getElementsByTagName("link");
				for(int j = 0; j < links.getLength(); j++) {
					Element link = (Element) links.item(j);
					if(link.hasAttribute("title") && link.getAttribute("title").equalsIgnoreCase("pdf")) {
						p.pdf = new URL(link.getAttribute("href"));
						break;
					}
				}

				Element published = (Element) entry.getElementsByTagName("published").item(0);
				p.year = published.getTextContent().split("-")[0];

				Element abst = (Element) entry.getElementsByTagName("summary").item(0);
				p.abst = abst.getTextContent();

				papers.add(p);
			}

			return papers;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String buildSearch(ArrayList <String> terms) {
		
		String searchTerm = "";
	
		try {
			
			
			for (int i = 0; i < terms.size(); i++){
				
				String term = terms.get(i);
				term = URLEncoder.encode(term, "UTF-8");
				if (i == terms.size() - 1)
					searchTerm += term;
				else
					searchTerm += term + "+AND+";
			
			}
			
			/* all = (all==null)?null:URLEncoder.encode(all, "UTF-8");
			title = (title==null)?null:URLEncoder.encode(title, "UTF-8");
			author = (author==null)?null:URLEncoder.encode(author, "UTF-8");

			StringBuilder term = new StringBuilder();
			boolean alle = true, tie = true;
			if(all == null || all.equals(""))
				alle = false;
			else term.append("all:" + all);

			if(title == null || title.equals(""))
				tie = false;
			else {
				if(alle)
					term.append("+AND+");
				term.append("ti:"+title);
			}

			if(author != null && !author.equals("")) {
				if(alle || tie)
					term.append("+AND+");
				term.append("au:"+author);
			} */

			return searchTerm;
		} catch (UnsupportedEncodingException e) { e.printStackTrace();}
		
		return null;
	}
	
	public String identifyConnection(){
		
		return "============================\nImported from ArXiv: \n";
		
		
	}
}
