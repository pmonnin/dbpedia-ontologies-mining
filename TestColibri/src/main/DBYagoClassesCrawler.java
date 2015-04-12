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
import dbpediaobjects.DBYagoClass;
import dbpediaobjects.PediaYagoThread;

public class DBYagoClassesCrawler {

    private HashMap<String, DBYagoClass> dbyagoclasses;

    public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParseException {
        System.out.println("START MAIN");
        new DBYagoClassesCrawler().computeParents();
    }

    public HashMap<String, DBYagoClass> getDbYagoClasses() {
        return dbyagoclasses;
    }

    public void computeParents() throws UnsupportedEncodingException, IOException, ParseException {
        // Ask for ontology json
        URLReader urlReader = new URLReader();
        String jsonResponse = urlReader.getJSON(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                        + "select distinct ?yago where " + "{ [] rdf:type ?yago . " + "FILTER (REGEX(STR(?yago), \"http://dbpedia.org/class/yago\", \"i\")) }", "UTF-8"));

        // Parse the categories
        JsonParser parser = new JsonParser(jsonResponse);
        dbyagoclasses = parser.getDbPediaYagoClasses();

        Set<String> keys = dbyagoclasses.keySet();
        int i = 0, keySize = keys.size();
        ArrayList<PediaYagoThread> threadList = new ArrayList<PediaYagoThread>();
        int nbCores = 40; // Runtime.getRuntime().availableProcessors();
        ArrayList<DBYagoClass> threadYagoClasses = new ArrayList<DBYagoClass>();

        // Add relationship
        for (String key : keys) {
            if (i % 1000 == 0) {
                // System.out.println("i " + i);
            }

            DBYagoClass yagoClass = dbyagoclasses.get(key);
            threadYagoClasses.add(yagoClass);

            if (i % Math.ceil(keySize / nbCores) == 0 && i != 0) {
                PediaYagoThread thread = new PediaYagoThread(parser, urlReader, threadYagoClasses);
                thread.start();
                threadList.add(thread);
                threadYagoClasses = new ArrayList<DBYagoClass>();
            }

            i++;
        }

        System.out.println("STARTING THREADS JOIN...");
        dbyagoclasses.clear();
        int yagoClassesWithParents = 0;
        for (PediaYagoThread thread : threadList) {
            try {
                thread.join();
                System.out.println("THREAD TERMINE :)");
                for (DBYagoClass yagoClass : thread.getThreadYagoClasses()) {
                    dbyagoclasses.put(yagoClass.getUri(), yagoClass);
                    if (yagoClass.getParentsNumber() != 0) {
                        yagoClassesWithParents++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("PROGRAMME TERMINE : " + yagoClassesWithParents + " classes yago avec parents.");
        System.out.println("Nombre total de classes yago : " + dbyagoclasses.size());
    }
}
