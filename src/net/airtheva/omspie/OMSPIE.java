package net.airtheva.omspie;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;

public class OMSPIE {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String path = "C:\\Users\\unknown\\Desktop\\omspie\\";
		File dir = new File(path);
		
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				
				if(file.getName().toLowerCase().endsWith(".obml16")) {
					return true;
				}
				else {
					return false;
				}
				
			}
		});
		
		JSONArray j = new JSONArray();
		
		OBMLParser obmlParser = new OBMLParser();
		
		for(File file : files) {
			
			obmlParser.Feed(file);
			
			j.put(obmlParser.Extract());
			
		}
		
		FileWriter fw = new FileWriter(new File(dir, "omspie.json"));
		fw.write(j.toString(4));
		fw.close();
		
	}

}
