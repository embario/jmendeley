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
	
	/**
	 * This strict Paper factory method accepts an array of Objects such that its elements conform to the order in which they are laid out in the Paper.java
	 * class. When calling this method, make sure to put your paper elements in the specific order and cast them to the Object class. The Paper returned
	 * will take care of the types here.
	 * @param fields
	 * @return
	 */
	public static Paper getInstance(Object [] fields){
		
		assert(fields.length == 7);
		Paper result = new Paper();
		result.title 		= (fields [0] instanceof String) ? (String) fields[0] : null;
		result.authors 		= (fields [1] instanceof String []) ? (String []) fields [1]: null;
		result.pdf 			= (fields [2] instanceof URL) ? (URL) fields [2]: null;
		result.year			= (fields [3] instanceof String) ? (String) fields [3] : null;
		result.type			= (fields [4] instanceof String) ? (String) fields [4] : null;
		result.abst			= (fields [5] instanceof String) ? (String) fields [5] : null;
		result.venue		= (fields [6] instanceof String) ? (String) fields [6] : null;
		
		return result;
	}
	
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
