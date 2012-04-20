package main;

import java.util.List;

/**
 * This ConnectionStrategy class separates variability from the SearchManager as a run-time operation, allowing the user to 
 * connect to any one API he/she chooses to
 * @author Mario Barrenechea
 *
 */
public interface ConnectionStrategy {
	
	public abstract List<Paper> search(String searchTerm, String title, String author, int maxResults);
	
	public abstract String buildSearch(String all, String title, String author); 
	
	public abstract String identifyConnection();
}
