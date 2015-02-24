package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;
import dbpediaobjects.DBCategory;

public class MainDBCategories {

	public static void main(String[] args) throws UnsupportedEncodingException,
			IOException, ParseException {
		// Ask for categories
		System.out.println("Before server action");
		URLReader urlReader = new URLReader();
		String jsonResponse = urlReader
				.getJSON(URLEncoder
						.encode("PREFIX dcterms:<http://purl.org/dc/terms/> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> select distinct ?Category ?Label where "
								+ "{ [] dcterms:subject ?Category . ?Category rdfs:label ?Label "
								+ "}", "UTF-8"));

		System.out.println("After server action");
		// Get the categories
		JsonParser parser = new JsonParser(jsonResponse);
		HashMap<String, DBCategory> categoriesList = parser.getDbPediaCategories();
		System.out.println("After parser action");
		computeParents(categoriesList);
	}

	public static void computeParents(HashMap<String, DBCategory> categories)
			throws UnsupportedEncodingException, IOException, ParseException {
		Set<String> keys = categories.keySet();

		for (String key : keys) {
			DBCategory cat = categories.get(key);

			if (cat.getParentsNumber() == 0) {
				URLReader urlReader = new URLReader();
				String jsonResponse = urlReader
						.getJSON(URLEncoder
								.encode("PREFIX dcterms:<http://purl.org/dc/terms/> select distinct ?parent ?label where "
										+ " { "
										+ "<" + cat.getUri() + "> skos:broader ?parent . "
										+ "?parent rdfs:label ?label" + "}",
										"UTF-8"));

				JsonParser parser = new JsonParser(jsonResponse);
				HashMap<String, DBCategory> parents = parser.getDbPediaCategories();

				Set<String> parentsKeys = parents.keySet();
				
				for(String parentKey : parentsKeys) {
					DBCategory parent = categories.get(parentKey);
					cat.addParent(parent);
				}
			}
		}
		
		System.out.println(categories.size());
	}
}
