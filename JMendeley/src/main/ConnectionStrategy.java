package main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

/**
 * This ConnectionStrategy class separates variability from the SearchManager as a run-time operation, allowing the user to 
 * connect to any one API he/she chooses to
 * @author Mario Barrenechea
 *
 */
public interface ConnectionStrategy {
	
	public abstract List<Paper> search(ArrayList <String> terms, int maxResults) throws JSONException ;
	
	public abstract String buildSearch(ArrayList <String> terms); 
	
	public abstract String identifyConnection();
}
