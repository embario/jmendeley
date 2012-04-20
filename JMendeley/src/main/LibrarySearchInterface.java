package main;

import java.util.List;

public interface LibrarySearchInterface {
	public List<Paper> search(String searchTerm, String title, String authors, int maxResults);
}
