package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import LatticeCreation.ALattice;
import ServerLink.AFileReader;
import ServerLink.AJsonParser;

public class Main {

	// Ontology :
	// http://wiki.dbpedia.org/Ontology
	// http://mappings.dbpedia.org/index.php/How_to_edit_the_DBpedia_Ontology
	// http://mappings.dbpedia.org/server/templatestatistics/fr/?template=Infobox_Ch%C3%A2teau
	
	public static void main(String[] args) throws ParseException, IOException
	{
		// TODO : get the json from our server
		// The current request :
		// SELECT DISTINCT ?chose WHERE {?chose a owl:Thing}
		AFileReader fileReader = new AFileReader("1Response.txt");
		String jsonResponse = fileReader.readFile();
		
		/*"{ \"head\": { \"link\": [], \"vars\": [\"chose\"] },"
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
		*/
		// We parse it to get the different results
		AJsonParser parser = new AJsonParser(jsonResponse);
		ArrayList<String> results = parser.getResults("chose");
		
		// For each result
		String request = "";
		String response = "";
		int i = 0;
		for (i=0 ; i<results.size() ; i++)
		{
			request = parser.makeRequestAtt(results.get(i));
			System.out.println(request);

			// TODO : run the request on the server and get the response
			// TODO : add the object to the lattice
			
			// We get the response
			fileReader.setNameFile("1"+i+"Response.txt");
			response = fileReader.readFile();
			
			// We parse it to get the different attributes of the thing
			parser.setStringToParse(response);
			ArrayList<String> attributes = parser.getResults("att");
			
			// TODO : add them to the lattice
			int j = 0;
			for (j=0 ; j<attributes.size() ; j++)
			{
				System.out.println(attributes.get(j));
			}
			
			System.out.println("----------------------");
		}
		

		//ALattice lattice = new ALattice();
		//lattice.execIterator();
	}
}
