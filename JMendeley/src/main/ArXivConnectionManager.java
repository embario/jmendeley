package main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ArXivConnectionManager {
	public List<Paper> search(String searchTerm, int num) {
		try {
			final URL url = new URL(String.format("http://export.arxiv.org/api/query?search_query=all:%s&start=0&max_results=%d", searchTerm, num));
			final InputStream stream = url.openStream();
			
			final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			Document results = db.parse(stream);
			
			ArrayList<Paper> papers = new ArrayList<Paper>(num);
			
			Element doc = results.getDocumentElement();
			NodeList entries = doc.getElementsByTagName("entry");
			for(int i = 0; i < entries.getLength(); i++) {
				Paper p = new Paper();
				p.venue = "";
				p.type = "Article";
				
				Element entry = (Element) entries.item(i);
				
				Element title = (Element) entry.getElementsByTagName("title").item(0);
				p.title = title.getTextContent();
				
				NodeList authors = entry.getElementsByTagName("author");
				String[] authorNames = new String[authors.getLength()];
				for(int j = 0; j < authors.getLength(); j++) {
					Element author = (Element) authors.item(j);
					Element authorName = (Element) author.getElementsByTagName("name").item(0);
					authorNames[j] = title.getTextContent();
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
	
	public static void main(String[] args) {
		new ArXivConnectionManager().search("Siek",10);
	}
}
