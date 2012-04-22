package util;

/**
 * Utility class for referring to API Urls for all Mendeley API method calls.
 * 
 * @author A,M,M
 *
 */
public class MendeleyApiUrls {
    
	/** Mendeley GET Profile Info URL. **/
	public static final String MENDELEY_GET_PROFILE_INFO_URL = "http://api.mendeley.com/oapi/profiles/info/me/";
	
	/** Mendeley POST Document URL. Requires the encoded URL for the JSON representation of the paper. **/
	public static final String MENDElEY_POST_DOCUMENT_URL = "http://api.mendeley.com/oapi/library/documents?document=";
    
	/** Mendeley PUT Document File URL. Requires the Document ID and OAuth SHA1 in the authentication header. **/
	public static final String MENDELEY_PUT_DOCUMENT_PDF_URL = "http://www.mendeley.com/oapi/library/documents/%s/";

}
