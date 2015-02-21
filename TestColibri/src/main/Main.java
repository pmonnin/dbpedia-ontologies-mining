package main;

import java.util.Iterator;
import java.util.Map;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import LatticeCreation.ALattice;

public class Main {

	// Ontology :
	// http://wiki.dbpedia.org/Ontology
	// http://mappings.dbpedia.org/index.php/How_to_edit_the_DBpedia_Ontology
	// http://mappings.dbpedia.org/server/templatestatistics/fr/?template=Infobox_Ch%C3%A2teau
	
	public static void main(String[] args) throws ParseException
	{
		// We create a parser
		JSONParser parser = new JSONParser();
		// TODO : get the json from our server
		// The current request :
		// SELECT DISTINCT ?chose WHERE {?chose a owl:Thing}
		String jsonResponse = "{ \"head\": { \"link\": [], \"vars\": [\"chose\"] },"
				+ "\"results\": { \"distinct\": false,"
				+ "\"ordered\": true,"
				+ "\"bindings\": ["
				+ "{ \"chose\": { \"type\": \"uri\", \"value\": \"http://dbpedia.org/resource/%C3%81ngel_Gim%C3%A9nez\" }},"
				+ "{ \"chose\": { \"type\": \"uri\", \"value\": \"http://dbpedia.org/resource/007:_Licence_to_Kill\" }},"
				+ "{ \"chose\": { \"type\": \"uri\", \"value\": \"http://dbpedia.org/resource/2000_series_(Chicago_'L')\" }},"
				+ "{ \"chose\": { \"type\": \"uri\", \"value\": \"http://dbpedia.org/resource/5172_Yoshiyuki\" }},"
				+ "{ \"chose\": { \"type\": \"uri\", \"value\": \"http://dbpedia.org/resource/5676_Voltaire\" }},"
				+ "{ \"chose\": { \"type\": \"uri\", \"value\": \"http://dbpedia.org/resource/6_Squadron_SAAF\" }},"
				+ "{ \"chose\": { \"type\": \"uri\", \"value\": \"http://dbpedia.org/resource/Aalsum,_Friesland\" }},"
				+ "{ \"chose\": { \"type\": \"uri\", \"value\": \"http://dbpedia.org/resource/Aalsum,_Groningen\" }},"
				+ "{ \"chose\": { \"type\": \"uri\", \"value\": \"http://dbpedia.org/resource/Aaron_Lines\" }},"
				+ "{ \"chose\": { \"type\": \"uri\", \"value\": \"http://dbpedia.org/resource/Abel_Lafleur\" }} ] } }";
		
		// We get the JSON parsed
		Map map = (Map)parser.parse(jsonResponse);
		// We get the results
		map = (Map) map.get("results");
		JSONArray array = (JSONArray) map.get("bindings");
		
		// For each result
		int i = 0;
		for (i=0 ; i<array.size() ; i++)
		{
			// We get the value of the link
			map = (Map) array.get(i);
			map = (Map) map.get("chose");
			String str = (String) map.get("value");
			System.out.println(str);
			
			// With this value, we can make a new request
			// The request is :
			// SELECT DISTINCT ?att WHERE { <str> ?att ?other }
			String request = "SELECT DISTINCT ?att WHERE { <"+str+"> ?att ?other }";
			System.out.println("Request is : " + request);
			System.out.println("");
			
			// TODO : ask the request on the server and get the response
		}

		//ALattice lattice = new ALattice();
		//lattice.execIterator();
	}
}
