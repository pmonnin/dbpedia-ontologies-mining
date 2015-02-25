package serverlink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLReader {
	
	public URLReader()
	{
		
	}
	
	public String getJSON(String requestUrl) throws IOException
	{
	    requestUrl = requestUrl.replaceAll("%3C", "<").replaceAll("%3E", ">");
	    System.out.println("SENDIN SERVER REQUEST : " + requestUrl);
//		URL url = new URL("http://sbc2015.telecomnancy.univ-lorraine.fr/project/query?query="+requestUrl+"&output=json");
		URL url = new URL("http://dbpedia.org/sparql?query="+requestUrl+"&format=application%2Fsparql-results%2Bjson");
        
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//		System.out.println("GET JSON : Received !");
		String inputLine;
		StringBuilder jsonResponse = new StringBuilder();
		inputLine = buff.readLine();
		
		while ( inputLine != null)
		{
			jsonResponse.append(inputLine);
			inputLine = buff.readLine();
		}
		
		buff.close();
		connection.disconnect();
		// System.out.println(jsonResponse);
		return jsonResponse.toString();
	}
	
}
