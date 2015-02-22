package ServerLink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AnURLReader {
	
	public AnURLReader()
	{
		
	}
	
	public String getJSON(String requestUrl) throws IOException
	{
		URL url = new URL("http://dbpedia.org/sparql?query="+requestUrl+"&format=application%2Fsparql-results%2Bjson");
		URLConnection connection = url.openConnection();
		BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		String jsonResponse = "";
		inputLine = buff.readLine();
		while ( inputLine != null)
		{
			jsonResponse += inputLine;
			inputLine = buff.readLine();
		}
		
		buff.close();
		// System.out.println(jsonResponse);
		return jsonResponse;
	}
	
}
