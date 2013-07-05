package net.airtheva.omspie;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

public class obmlCrawler implements ICrawler{

	File mFile = null;
	
	String mFilename = null;
	long mLastModified = 0;
	String mTitle = null;
	String mAddress = null;
	
	obmlCrawler(File file)
	{
		mFile = file;
	}
	
	int getUnsignedByteValue(byte b)
	{
		// I need unsigned byte.
		// "& 0xff" saves me, unknown but powerful.=w=
		return b & 0xff;
	}
	
	// Still in experiment.
	String getAddress(byte[] bytes)
	{
		try
		{
			byte[] addressBytes = new byte[1024];
			int addressCurrentLength = 0;
			for(int i = 0; i < 1024; i++)
			{
				// 0x68 == h.
				// 0x74 == t.
				// 0x70 == p.
				if(bytes[i] == 0x68 && bytes[i + 1] == 0x74 && bytes[i + 2] == 0x74 && bytes[i + 3] == 0x70)
				{
					// "http" founded, and only the first one is needed.
					int firstLength = getUnsignedByteValue(bytes[i - 1]);
					System.arraycopy(bytes, i, addressBytes, 0, firstLength);
					addressCurrentLength += firstLength;
					
					// How to get more address fragment?
					// Which offset stands for the amount of fragments?
					// I used to think its scheme is "[firstEnding] 0x00 xx [secondBeginning]",
					// and the first of secondBeginning is 0x00, but it may be some other value.
					// So there is some logic to deal with it.
					
					// Here is how I deal with second fragment.
					// Will somebody help me?
					int firstEndingOffset = i + firstLength; // represents the 0x00 before xx.
					int secondLength = getUnsignedByteValue(bytes[firstEndingOffset + 1]);
					int secondBeginOffset = firstEndingOffset + 2;
					switch(bytes[secondBeginOffset])
					{
					case 0x00:
						// Jump over the first 0x00.
						System.arraycopy(bytes, secondBeginOffset + 1, addressBytes, addressCurrentLength, secondLength - 1);
						addressCurrentLength += secondLength - 1;
						break;
					case 0x68:
						// In some cases it is another address beginning with "http". Just ignore it.
						break;
					}
					
					break;
				}
			}
			return (new String(addressBytes, 0, addressCurrentLength, "utf-8"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void Extract()
	{
		try {
			// Get filename.
			mFilename = mFile.getName();
			//System.out.println(Filename);
			
			// Get last modified.
			mLastModified = new Date(mFile.lastModified()).getTime();
			//System.out.println(LastModified);
			
			byte[] bytes = new byte[1024];
			FileInputStream input = new FileInputStream(mFile);
			input.read(bytes, 0, 1024);
			input.close();
			
			// Get title.
			int titleLength = getUnsignedByteValue(bytes[0x10]);
			mTitle = new String(bytes, 0x11, titleLength, "utf-8");
			//System.out.println(Title);
			
			// Get address.
			mAddress = getAddress(bytes);
			//System.out.println(Address);
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
