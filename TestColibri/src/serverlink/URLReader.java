package serverlink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLReader {
	
	public URLReader()
	{
		
	}
	
	public String getJSON(String requestUrl) throws IOException
	{
		URL url = new URL("http://sbc2015.telecomnancy.univ-lorraine.fr/project/query?query="+requestUrl+"&output=json");
		URLConnection connection = url.openConnection();
		BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		String jsonResponse = "";
		inputLine = buff.readLine();
		
		int i = 0;
		while ( inputLine != null)
		{
			if(i % 10000 == 0)
				System.out.println("Ligne " + i);
			i++;
			
			jsonResponse += inputLine;
			inputLine = buff.readLine();
		}
		
		buff.close();
		// System.out.println(jsonResponse);
		return jsonResponse;
	}
	
}
