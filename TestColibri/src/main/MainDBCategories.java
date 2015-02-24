package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import serverlink.JsonParser;
import serverlink.URLReader;
import dbpediaobjects.DBCategory;

public class MainDBCategories {

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		//Ask for categories
		URLReader urlReader = new URLReader();
		String jsonResponse = urlReader.getJSON(URLEncoder.encode("select distinct ?Category ?Label where "
																  + "{ [] dcterms:subject ?Category . ?Category rdfs:label ?Label"
																  + "} LIMIT 100", "UTF-8"));
		
		// Get the categories
		JsonParser parser = new JsonParser(jsonResponse);	
		HashMap<String, DBCategory> categoriesList = parser.getDbPediaCategories();
	}
	
	void computeParents(HashMap<String, DBCategory> categories) {
		
	}
}
