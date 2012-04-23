package util;

/**
 * Utility class for referring to API Urls for all Mendeley API method calls.
 * 
 * @author A,M,M
 *
 */
public class MendeleyApiUrls {
    
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
	
}
