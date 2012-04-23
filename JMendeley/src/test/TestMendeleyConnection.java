package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import main.AuthenticationManager;

import org.junit.Before;
import org.junit.Test;

public class TestMendeleyConnection {

	private AuthenticationManager am = null;
	
	@Before
	public void setup() throws FileNotFoundException, IOException{
		
		am = AuthenticationManager.getInstance(AllTests.CONSUMER_KEY, AllTests.CONSUMER_SECRET);
	}
	
	@Test
	public void testConnectToMendeley() throws FileNotFoundException, IOException {
		
		//Not yet connected.
		Assert.assertFalse(am.isConnected());
		
		assert(true);
		
		//Connect.
		Assert.assertTrue(am.connectToMendeley());
		
		//We should now be connected.
		Assert.assertTrue(am.isConnected());
		
	}
	
	@Test
	public void testDeleteFile () throws FileNotFoundException, IOException{
		
		//delete the token file.
		File file = new File("./usr/jmendeley_token");
		file.delete();
		
		Assert.assertFalse(file.exists());
		
		//Just check to see if the file exists again.
		Assert.assertTrue(am.connectToMendeley());
		Assert.assertTrue(file.exists() == true);
	}
	
	@Test
	public void testInvalidVerID() throws FileNotFoundException, IOException{
		
		//Assert.assertFalse(am.isConnected());
		
		//Delete the token file.
		File file = new File("./usr/jmendeley_token");
		file.delete();
		
		//Now input an incorrect verification code; this should fail;
		Assert.assertFalse(am.connectToMendeley());
	}

}
