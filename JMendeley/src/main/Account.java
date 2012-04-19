package main;

public class Account {

	private String _profileid = null;
	private String _name = null;
	private String _academic_status = null;
	private String _discipline = null;
	private String [] _research_interests = null;
	private String _url = null;
	
	public Account(String profileid, String name, String academic_status,
			String discipline, String research_interests, String url) {
	
		this._profileid = profileid;
		this._name = name;
		this._academic_status = academic_status;
		this._discipline = discipline;
		this._research_interests = research_interests.split("\n");
		this._url = url;
	}
	
	
	public String toString() {
		
		String result = "Profile ID: " + this._profileid + "\nName: " + this._name 
				+ "\nAcademic Status: " + this._academic_status + "\nDiscipline:" + this._discipline
				+ "\nResearch Interests: [";
		
		for (String ri : this._research_interests)
			result += ri + ", ";
		
		result += "]\nURL: " + this._url;
		return result;
	}

}
