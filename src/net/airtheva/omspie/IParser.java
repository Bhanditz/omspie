package net.airtheva.omspie;

import java.io.File;

import org.json.JSONObject;

public abstract class IParser {

	public String Filename;
	public String Title;
	public String URI;
	public long LastModified;

	public IParser() {

		Filename = "";
		Title = "";
		URI = "";
		LastModified = 0;
		
	}
	
	abstract void Feed(File file);

	public JSONObject Extract() {

		JSONObject j = new JSONObject();
		
		j.put("filename", Filename);
		j.put("title", Title);
		j.put("uri", URI);
		j.put("lastModified", LastModified);

		return j;
		
	}

}
