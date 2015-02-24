package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import serverlink.JsonParser;
import serverlink.URLReader;
import dbpediaobjects.DBCategory;

public class MainDBCategories {

	public static void main(String[] args) throws UnsupportedEncodingException,
			IOException {
		// Ask for categories
		URLReader urlReader = new URLReader();
		String jsonResponse = urlReader
				.getJSON(URLEncoder
						.encode("select distinct ?Category ?Label where "
								+ "{ [] dcterms:subject ?Category . ?Category rdfs:label ?Label "
								+ "} LIMIT 100", "UTF-8"));

		// Get the categories
		JsonParser parser = new JsonParser(jsonResponse);
		HashMap<String, DBCategory> categoriesList = parser.getDbPediaCategories();
	}

	void computeParents(HashMap<String, DBCategory> categories)
			throws UnsupportedEncodingException, IOException {
		Set<String> keys = categories.keySet();

		for (String key : keys) {
			DBCategory cat = categories.get(key);

			if (cat.getParentsNumber() == 0) {
				URLReader urlReader = new URLReader();
				String jsonResponse = urlReader
						.getJSON(URLEncoder
								.encode("select distinct ?parent ?label where "
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
	}
}
