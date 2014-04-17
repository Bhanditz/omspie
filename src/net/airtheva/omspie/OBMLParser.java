package net.airtheva.omspie;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class OBMLParser extends IParser {

	@Override
	public void Feed(File file) {
		
		try {
			
			Filename = file.getName();
			
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			
			dis.skipBytes(15);
			short titleLength = dis.readShort();
			byte[] titleBytes = new byte[titleLength];
			dis.readFully(titleBytes);
			Title = new String(titleBytes);
			
			short unknownLength = dis.readShort();
			dis.skipBytes(unknownLength);
			
			short uriLength = dis.readShort();
			byte[] uriBytes = new byte[uriLength];
			dis.readFully(uriBytes);
			URI = new String(uriBytes);
			
			if(URI.matches("^https?:\\/\\/[^\\/]+\\/$")) {
				
				short uriRestLength = dis.readShort();
				byte[] uriRestBytes = new byte[uriRestLength];
				dis.readFully(uriRestBytes);
				String uriRest = new String(uriRestBytes, 1, uriRestLength - 1);
				
				URI += uriRest;
				
			}
			
			LastModified = file.lastModified();
			
			dis.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
