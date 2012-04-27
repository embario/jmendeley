package util;

/**
 * Utility class for referring to API Urls for all Mendeley API method calls.
 * 
 * @author A,M,M
 *
 */
public class JMendeleyApiUrls {
    
	/** Mendeley GET User Profile Info URL. **/
	public static final String USER_GET_PROFILE_INFO_URL = "http://api.mendeley.com/oapi/profiles/info/me/";
	
	/** Mendeley POST Document URL. Requires the encoded URL for the JSON representation of the paper. **/
	public static final String USER_POST_DOCUMENT_URL = "http://api.mendeley.com/oapi/library/documents?document=";
    
	/** Mendeley PUT Document File URL. Requires the Document ID and OAuth SHA1 in the authentication header. **/
	public static final String USER_PUT_DOCUMENT_PDF_URL = "http://www.mendeley.com/oapi/library/documents/%s/";

	/** Mendeley GET (SEARCH) for Document. Requires formatting the string with MendeleyConnectionStrategy.buildSearch() and a number for max results.**/
	public static final String PUBLIC_GET_SEARCH_FOR_DOCUMENTS = "http://api.mendeley.com/oapi/documents/search/%s/?items=%d";
	
	/** Mendeley GET (SEARCH) for Document Details. Requires the document id. **/
	public static final String PUBLIC_GET_DOCUMENT_DETAILS = "http://api.mendeley.com/oapi/documents/details/%s/";
	
	
	//JMendeley String Prefixes for Search Terms
	public static final String JMEND_SEARCH_TERM = "s:";
	public static final String JMEND_AUTHOR_TERM = "a:";
	public static final String JMEND_TITLE_TERM = "t:";
	public static final String JMEND_YEAR_TERM = "y:";
	public static final String JMEND_PUBREF_TERM = "pr:";
	
	//Mendeley Search Term Prefixes.
	public static final String MENDELEY_SEARCH_TERM = "";
	public static final String MENDELEY_AUTHOR_TERM = "author:";
	public static final String MENDELEY_TITLE_TERM = "title:";
	public static final String MENDELEY_YEAR_TERM = "year:";
	public static final String MENDELEY_PUBREF_TERM = "pubref:";
	
	// ArXiv Search Term Prefixes
	public static final String ARXIV_SEARCH_TERM = "all:";
	public static final String ARXIV_TITLE_TERM = "ti:";
	public static final String ARXIV_AUTHOR_TERM = "au:";
	public static final String ARXIV_YEAR_TERM = "yr:";
	public static final String ARXIV_PUBREF_TERM = "pr:";
	
	
}
