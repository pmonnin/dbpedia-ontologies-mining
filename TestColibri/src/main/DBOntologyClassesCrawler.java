package main;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import serverlink.JsonParser;
import serverlink.URLReader;
import dbpediaobjects.DBCategory;
import dbpediaobjects.DBOntologyClass;
import dbpediaobjects.PediaCategoryThread;

public class DBOntologyClassesCrawler {
	
	private HashMap<String, DBOntologyClass> dbontologies;
	
	public DBOntologyClassesCrawler() {
		
	}
	
	public void computeParents() {
		//Ask for ontology json
		URLReader urlReader = new URLReader();
        String jsonResponse = urlReader
                .getJSON(URLEncoder
                        .encode("select distinct ?Ontology ?Label where"
                    			+"{"
                    			+"[] rdf:type ?Ontology . "
                    			+"?Ontology rdfs:label ?Label . "
                    			+"FILTER(STRSTARTS(STR(?Ontology ), \"http://dbpedia.org/ontology\")) . "
                    			+"FILTER(langMatches(lang(?Label), \"FR\")) "
                    			+"}", "UTF-8"));

        // Parse the categories
        JsonParser parser = new JsonParser(jsonResponse);
        dbontologies = parser.getDbPediaOntologyClasses();

        Set<String> keys = dbontologies.keySet();
        int i = 0, keySize = keys.size();
        /*ArrayList<PediaCategoryThread> threadList = new ArrayList<PediaCategoryThread>();
        int nbCores = Runtime.getRuntime().availableProcessors();
        ArrayList<DBCategory> threadCategories = new ArrayList<DBCategory>();

        // Add relationship
        for (String key : keys) {
            if (i % 1000 == 0) {
                System.out.println("i " + i);
            }

            DBCategory cat = dbcategories.get(key);
            threadCategories.add(cat);

            if (i % Math.ceil(keySize / nbCores) == 0 && i != 0) {
                PediaCategoryThread thread = new PediaCategoryThread(parser, urlReader,
                        dbcategories, threadCategories);
                thread.start();
                threadList.add(thread);
                threadCategories = new ArrayList<DBCategory>();
            }

            i++;
        }

        System.out.println("STARTING THREADS JOIN...");
        int categoriesWithoutParents = 0;
        for (PediaCategoryThread thread : threadList) {
            try {
                thread.join();
                System.out.println("THREAD TERMINE :)");
                for (DBCategory cat : thread.getThreadCategories()) {
                    if (cat.getParents().isEmpty()) {
//                        System.err.println("La cat�gorie " + cat.getUri()  + " n'a pas de parents.");
                        categoriesWithoutParents++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("PROGRAMME TERMINE : " + categoriesWithoutParents + " cat�gories sans parents.");*/
	}
}
