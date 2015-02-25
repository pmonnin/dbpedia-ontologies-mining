package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;
import dbpediaobjects.DBCategory;

public class DBCategoriesCrawler {

	private HashMap<String, DBCategory> dbcategories;
	
	public DBCategoriesCrawler() {
		
	}

	public void computeParents(HashMap<String, DBCategory> categories)
			throws UnsupportedEncodingException, IOException, ParseException {
		// Ask for categories json
		URLReader urlReader = new URLReader();
		String jsonResponse = urlReader
				.getJSON(URLEncoder
						.encode("PREFIX dcterms:<http://purl.org/dc/terms/> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> select distinct ?Category ?Label where "
								+ "{ [] dcterms:subject ?Category . ?Category rdfs:label ?Label "
								+ "}", "UTF-8"));
		
		// Parse the categories
		JsonParser parser = new JsonParser(jsonResponse);
		dbcategories = parser.getDbPediaCategories();
		
		Set<String> keys = dbcategories.keySet();
		int i = 0, keySize = keys.size();
		
		//Add relationship
		for (String key : keys) {
			i++;
			if(i % 1000 == 0) {
				System.out.println("i " + i);
			}
			
			DBCategory cat = dbcategories.get(key);

			if (cat.getParentsNumber() == 0) {
				String jsonParents = urlReader
						.getJSON(URLEncoder
								.encode("PREFIX dcterms:<http://purl.org/dc/terms/> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> PREFIX skos:<http://www.w3.org/2004/02/skos/core#> select distinct ?parent ?label where "
										+ " { "
										+ "<" + cat.getUri() + "> skos:broader ?parent . "
										+ "?parent rdfs:label ?label" + "}", "UTF-8"));

				parser.setStringToParse(jsonParents);
				HashMap<String, DBCategory> parents = parser.getDbPediaCategories();

				Set<String> parentsKeys = parents.keySet();
				
				for(String parentKey : parentsKeys) {
					DBCategory parent = dbcategories.get(parentKey);
					cat.addParent(parent);
				}
			}
		}
	}
}
