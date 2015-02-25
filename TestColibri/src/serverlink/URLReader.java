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
		System.out.println(requestUrl);
		//URL url = new URL("http://sbc2015.telecomnancy.univ-lorraine.fr/project/query?query="+requestUrl+"&output=json");
		URL url = new URL("http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query="+requestUrl+"&format=application%2Fsparql-results%2Bjson&timeout=30000&debug=on");
		URLConnection connection = url.openConnection();
		BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder jsonResponse = new StringBuilder();
		inputLine = buff.readLine();
		
//		int i = 0;
		while ( inputLine != null)
		{
//			if(i % 10000 == 0)
//				System.out.println("Ligne " + i);
//			i++;
			
			jsonResponse.append(inputLine);
			inputLine = buff.readLine();
		}
		
		buff.close();

		// System.out.println(jsonResponse);
		return jsonResponse.toString();
	}
	
}
