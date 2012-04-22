package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import main.AuthenticationManager;

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
	}
	
	
	@Test
	public void testBadPublicRequest() throws FileNotFoundException, IOException {
		
		am.connectToMendeley();
		
		Response response = am.sendPublicRequest(Verb.GET, "Failing test");
		
		Assert.assertFalse(response.isSuccessful());
		int statusCode = response.getCode();
		Assert.assertTrue(statusCode != 200 && (statusCode == 404 || statusCode == 401));
		System.out.println("Unsuccessful response body: " + response.getBody());
		
	}
	
	
	@Test
	public void tesetGoodPublicRequest() throws FileNotFoundException, IOException {
		
		am.connectToMendeley();
		
		Response response = am.sendPublicRequest(Verb.GET, MendeleyApiUrls.MENDELEY_GET_PROFILE_INFO_URL);
		
		Assert.assertTrue(response.isSuccessful());
		int statusCode = response.getCode();
		Assert.assertTrue(statusCode == 200);
		System.out.println("Successful response body " + response.getBody());
	}

}
