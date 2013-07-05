package net.airtheva.omspie;

public interface ICrawler {
	
	void Extract();
	
	String GetFilename();
	long GetLastModified();
	String GetTitle();
	String GetAddress();

}
