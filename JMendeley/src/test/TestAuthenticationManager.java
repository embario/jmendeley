package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import main.AuthenticationManager;
import main.ConnectionStrategy;
import main.MendeleyConnectionStrategy;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import util.MendeleyApiUrls;

public class TestAuthenticationManager {

	private AuthenticationManager am = null;
	
	@Before
	public void setup() throws FileNotFoundException, IOException{
		
		am = AuthenticationManager.getInstance(AllTests.CONSUMER_KEY, AllTests.CONSUMER_SECRET);
		am.connectToMendeley();
	}
	
	
	@Test
	public void testBadRequests() throws FileNotFoundException, IOException {
		
		Response response = null;
		int statusCode = 0;
		
		//A completely useless request - will throw an OAuthException.
		response = am.sendPublicRequest(Verb.GET, "Failing test");
		Assert.assertTrue(response == null);
		
		//A public request that seeks details for a ridiculous document id.
		response = am.sendPublicRequest(Verb.GET, String.format(MendeleyApiUrls.PUBLIC_GET_DOCUMENT_DETAILS, "0000"));
		Assert.assertFalse(response.isSuccessful());
		statusCode = response.getCode();
		Assert.assertTrue(statusCode != 200 && (statusCode == 400 || statusCode == 404 || statusCode == 401 || statusCode == 500));
		
		//Now, a request that attempts to fetch user profile info but is appended with 'bad'. 
		response = am.sendRequest(Verb.GET, MendeleyApiUrls.USER_GET_PROFILE_INFO_URL + "bad");
		Assert.assertFalse(response.isSuccessful());
		statusCode = response.getCode();
		Assert.assertTrue(statusCode != 200 && (statusCode == 400 || statusCode == 404 || statusCode == 401 || statusCode == 500));
		
	}
	
	@Test
	public void testGoodRequests() throws FileNotFoundException, IOException {
		
		Response response = null;
		int statusCode = 0;
		ConnectionStrategy cs = null;
		
		//Attempt to request for user profile info. Should always succeed.
		response = am.sendRequest(Verb.GET, MendeleyApiUrls.USER_GET_PROFILE_INFO_URL);
		Assert.assertTrue(response.isSuccessful());
		statusCode = response.getCode();
		Assert.assertTrue(statusCode == 200);
		
		cs = new MendeleyConnectionStrategy (am);
		response = am.sendPublicRequest(Verb.GET, 
				String.format(MendeleyApiUrls.PUBLIC_GET_SEARCH_FOR_DOCUMENTS, cs.buildSearch("Something", null, null), 10));
		Assert.assertTrue(response.isSuccessful());
		statusCode = response.getCode();
		Assert.assertTrue(statusCode == 200);
		
		
		
	}

}
