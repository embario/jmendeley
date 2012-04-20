package main;

import java.util.List;

public interface ConnectionStrategy {
	public List<Paper> search(String searchTerm, String title, String author, int maxResults);
}
