package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;
import dbpediaobjects.DBOntologyClass;
import dbpediaobjects.PediaOntologyThread;

public class DBOntologyClassesCrawler {
	
	private HashMap<String, DBOntologyClass> dbontologies;
	
	public static void main(String[] args) throws UnsupportedEncodingException,
    IOException, ParseException {
		System.out.println("START MAIN");
		new DBOntologyClassesCrawler().computeParents();
	}
	
	public DBOntologyClassesCrawler() {
		
	}
	
	public void computeParents() throws UnsupportedEncodingException, IOException, ParseException {
		//Ask for ontology json
		URLReader urlReader = new URLReader();
        String jsonResponse = urlReader
                .getJSON(URLEncoder
                        .encode("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                        		+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                        		+ "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                        		+ "select distinct ?Ontology ?Label where "
                        		+ "{ ?Ontology rdf:type owl:Class . ?Ontology rdfs:label ?Label . "
                        		+ "FILTER (REGEX(STR(?Ontology), \"http://dbpedia.org/ontology\", \"i\")) "
                        		+ "FILTER(langMatches(lang(?Label), \"EN\")) }", "UTF-8"));

        // Parse the categories
        JsonParser parser = new JsonParser(jsonResponse);
        dbontologies = parser.getDbPediaOntologyClasses();

        Set<String> keys = dbontologies.keySet();
        int i = 0, keySize = keys.size();
        ArrayList<PediaOntologyThread> threadList = new ArrayList<PediaOntologyThread>();
        int nbCores = Runtime.getRuntime().availableProcessors();
        ArrayList<DBOntologyClass> threadOntologies = new ArrayList<DBOntologyClass>();

        // Add relationship
        for (String key : keys) {
            if (i % 1000 == 0) {
                System.out.println("i " + i);
            }

            DBOntologyClass ont = dbontologies.get(key);
            threadOntologies.add(ont);

            if (i % Math.ceil(keySize / nbCores) == 0 && i != 0) {
                PediaOntologyThread thread = new PediaOntologyThread(parser, urlReader, threadOntologies);
                thread.start();
                threadList.add(thread);
                threadOntologies = new ArrayList<DBOntologyClass>();
            }

            i++;
        }

        System.out.println("STARTING THREADS JOIN...");
        dbontologies.clear();
        int categoriesWithParents = 0;
        for (PediaOntologyThread thread : threadList) {
            try {
                thread.join();
                System.out.println("THREAD TERMINE :)");
                for (DBOntologyClass ont : thread.getThreadCategories()) {
                	dbontologies.put(ont.getUri(), ont);
                    if (ont.getParentsNumber() != 0) {
                        categoriesWithParents++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("PROGRAMME TERMINE : " + categoriesWithParents + " ontologies avec parents.");
        System.out.println("Nombre total de classes ontologies : " + dbontologies.size());
	}
}
