package main;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import util.MendeleyApiUrls;

public class AccountManager {
	
	/** Our singleton object **/
	private static AccountManager _singleton = null;
	/** Our reference to the AuthenticationManager **/
	private AuthenticationManager _authManager = null;
	/** The Account object associated with this AccountManager **/
	private Account _account = null;
	
	
	private AccountManager(AuthenticationManager am) throws JSONException{
		
		this._authManager = am;
		this._account = this.requestAccount();
		
	}
	
	
	public static AccountManager getInstance (AuthenticationManager am) throws JSONException{
		
		if (_singleton == null)
			_singleton = new AccountManager(am);
		
		return _singleton;
	}
	
	
	public Account getAccount(){ return this._account;}
	
	 /**
	  * This method needs the AuthenticationManager singleton to send a request and retrieves a 
	  * response that represents the account information for the authenticated user. The method 
	  * works with JSON objects in order to construct a valid Account and returns it.
	  * 
	  * @return
	  * @throws JSONException
	  */
	 private Account requestAccount() throws JSONException{
		 
		//Get the response back in this object.
		 Response response = this._authManager.sendRequest(Verb.GET, MendeleyApiUrls.USER_GET_PROFILE_INFO_URL);
		 
		 Account result = null;
		 int statusCode = response.getCode();
		 
		 if (statusCode == 200){

			 try {
				 
				 //Wrap in a JSON Object, and also, we only care about its "main" section. 
				 JSONObject results = new JSONObject(response.getBody());
				 JSONObject main = results.getJSONObject("main");
			
				 String profileid, name, academic_status, research_interests, discipline, url;
				 
				 //Gather the <key,value> pairs found inside the JSON Object.
				 profileid = this.getValueFromJSONObject(main, "profile_id");
				 name = this.getValueFromJSONObject(main, "name");
				 academic_status = this.getValueFromJSONObject(main, "academic_status");
				 research_interests = this.getValueFromJSONObject(main, "research_interests");
				 discipline = this.getValueFromJSONObject(main, "discipline_name");
				 url = this.getValueFromJSONObject(main, "url");
			
				 //Create an Account object and return it.
				 result = new Account(profileid, name, academic_status, discipline, research_interests, url);
				 return result;
				 
			 } catch (JSONException e){ System.err.println(e);}
		 }//end if conditional
		 
		 //It didn't work - return null and handle.
		 return null;
	 }
	
	 
	 public String getValueFromJSONObject (JSONObject json, String key){
		 
		 try { return json.getString(key);}
		 catch (JSONException e){
			 
			 return "";
		 }
	 }
	
	public String toString(){
		
		return "Account::= " + this._account;
		
	}
	

}
