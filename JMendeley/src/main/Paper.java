package main;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Paper {
	public String title;
	public String[] authors;
	public URL pdf;
	public String year;
	public String type;
	public String abst;
	public String venue;
	
	public Paper() {}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("type", type);
			json.put("title", title);
			JSONArray authors = new JSONArray(this.authors);
			json.put("authors", authors);
			json.put("url", pdf.toString());
			json.put("year", year);
			json.put("abstract", abst);
			json.put("published_in", venue);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public String summarize() {
		StringBuilder term = new StringBuilder();
		
		term.append(title + "\n");
		term.append("\t" + authors[0]);
		for(int i = 1; i < authors.length; i++)
			term.append(", " + authors[i]);
		term.append("\n\t" + year);
		
		return term.toString();
	}
}
