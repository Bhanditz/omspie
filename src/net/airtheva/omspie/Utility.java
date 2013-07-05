package net.airtheva.omspie;

import java.io.*;

public class Utility {

static void SaveStreamFile(InputStream input, File file)
{
	try
	{
		OutputStream output = new FileOutputStream(file);
		int b = 0;
		while ((b = input.read()) != -1)
		{
			output.write(b);
		}
		output.close();
	}
	catch (IOException e)
	{
		e.printStackTrace();
	}
}

	static String GetExtensionName(String filename)
	{
		int index = filename.lastIndexOf(".");
		String ext = filename.substring(index);
		return ext;
	}
	
	static String ReadLines(BufferedReader reader) throws Exception
	{
		StringBuilder builder = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null)
		{
			builder.append(line);
			builder.append("\n");
		}
		return builder.toString();
	}
	
	static String Trim(String text)
	{
		String result = text.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
		return result;
	}
	
}
