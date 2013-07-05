package net.airtheva.omspie;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mhtmlCrawler implements ICrawler{

	File mFile = null;
	
	String mFilename = null;
	long mLastModified = 0;
	String mTitle = null;
	String mAddress = null;
	
	mhtmlCrawler(File file)
	{
		mFile = file;
	}
	
	@Override
	public void Extract()
	{
		try {
			mFilename = mFile.getName();
			
			mLastModified = new Date(mFile.lastModified()).getTime();

			FileInputStream input = new FileInputStream(mFile);
			// FIXME: No need to read all the file.
			byte[] bytes = new byte[2048];
			input.read(bytes);
			input.close();
			
			// The ending may be broken.
			String text = new String(bytes, "utf-8");
			
			Pattern titlePattern = Pattern.compile("<title>(.+)</title>", Pattern.DOTALL);
			Matcher titleMatcher = titlePattern.matcher(text);
			titleMatcher.find();
			mTitle = Utility.Trim(titleMatcher.group(1));
			
			Pattern addressPattern = Pattern.compile("^Content-Location:\\s*(\\S+)$", Pattern.MULTILINE);
			Matcher addressMatcher = addressPattern.matcher(text);
			addressMatcher.find();
			mAddress = Utility.Trim(addressMatcher.group(1));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String GetFilename() {
		// TODO Auto-generated method stub
		return mFilename;
	}

	@Override
	public long GetLastModified() {
		// TODO Auto-generated method stub
		return mLastModified;
	}

	@Override
	public String GetTitle() {
		// TODO Auto-generated method stub
		return mTitle;
	}

	@Override
	public String GetAddress() {
		// TODO Auto-generated method stub
		return mAddress;
	}
	
}
