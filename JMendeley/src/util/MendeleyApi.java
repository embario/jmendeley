package util;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
import org.scribe.model.Verb;

public class MendeleyApi extends DefaultApi10a {
	private final String AUTH_URL = "http://www.mendeley.com/oauth/authorize?oauth_token=%s";
	
	public Verb getRequestTokenVerb() {
		return Verb.GET;
	}
	
	public Verb getAccessTokenVerb() {
		return Verb.GET;
	}
	
	public String getAccessTokenEndpoint() {
		return "http://www.mendeley.com/oauth/access_token/";
	}

	public String getAuthorizationUrl(Token arg0) {
		return String.format(AUTH_URL, arg0.getToken());
	}

	public String getRequestTokenEndpoint() {
		return "http://www.mendeley.com/oauth/request_token/";
	} 
}