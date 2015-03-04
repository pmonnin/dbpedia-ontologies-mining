package serverlink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dbpediaobjects.DBCategory;
import dbpediaobjects.DBOntologyClass;

public class JsonParser {

    private String stringToParse;

    public JsonParser(String stringToParse) {
        this.stringToParse = stringToParse;
    }

    public ArrayList<String> getResults(String nameRequested) throws ParseException {
        // We get the JSON parsed
        JSONParser parser = new JSONParser();
        Map map = (Map) parser.parse(stringToParse);
        // We get the results
        map = (Map) map.get("results");
        JSONArray array = (JSONArray) map.get("bindings");

        ArrayList<String> returnArray = new ArrayList<>();

        // For each result
        for (int i = 0; i < array.size(); i++) {
            // We get the value of the link
            map = (Map) array.get(i);
            map = (Map) map.get(nameRequested);
            String str = (String) map.get("value");

            returnArray.add(str);
        }
        return returnArray;
    }

    public HashMap<String, DBCategory> getDbPediaCategories() throws ParseException {
        // We get the JSON parsed
        JSONParser parser = new JSONParser();
        Map map = (Map) parser.parse(stringToParse);
        // We get the results
        map = (Map) map.get("results");
        JSONArray array = (JSONArray) map.get("bindings");

        HashMap<String, DBCategory> res = new HashMap<>();

        // For each result
        for (int i = 0; i < array.size(); i++) {
            // We get the value of the link
            map = (Map) array.get(i);
            Map categoryMap = (Map) map.get("Category");
            String uri = (String) categoryMap.get("value");

            Map labelMap = (Map) map.get("Label");
            String label = (String) labelMap.get("value");

            res.put(uri, new DBCategory(label, uri));
        }

        return res;
    }

    public ArrayList<String> getDbPediaCategoriesParents() throws ParseException {
        // We get the JSON parsed
        JSONParser parser = new JSONParser();
        Map map = (Map) parser.parse(stringToParse);
        // We get the results
        map = (Map) map.get("results");
        JSONArray array = (JSONArray) map.get("bindings");

        ArrayList<String> res = new ArrayList<String>();

        // For each result
        for (int i = 0; i < array.size(); i++) {
            // We get the value of the link
            map = (Map) array.get(i);
            Map categoryMap = (Map) map.get("Category");
            String uri = (String) categoryMap.get("value");

            res.add(uri);
        }

        return res;
    }

    public HashMap<String, DBOntologyClass> getDbPediaOntologyClasses() throws ParseException {
        // We get the JSON parsed
        JSONParser parser = new JSONParser();
        Map map = (Map) parser.parse(stringToParse);
        // We get the results
        map = (Map) map.get("results");
        JSONArray array = (JSONArray) map.get("bindings");

        HashMap<String, DBOntologyClass> res = new HashMap<>();

        // For each result
        for (int i = 0; i < array.size(); i++) {
            // We get the value of the link
            map = (Map) array.get(i);
            Map categoryMap = (Map) map.get("Ontology");
            String uri = (String) categoryMap.get("value");

            Map labelMap = (Map) map.get("Label");
            String label = (String) labelMap.get("value");

            res.put(uri, new DBOntologyClass(label, uri));
        }

        return res;
    }

    public ArrayList<String> getDbPediaOntologyParents() throws ParseException {
        // We get the JSON parsed
        JSONParser parser = new JSONParser();
        Map map = (Map) parser.parse(stringToParse);
        // We get the results
        map = (Map) map.get("results");
        JSONArray array = (JSONArray) map.get("bindings");

        ArrayList<String> res = new ArrayList<String>();

        // For each result
        for (int i = 0; i < array.size(); i++) {
            // We get the value of the link
            map = (Map) array.get(i);
            Map categoryMap = (Map) map.get("Ontology2");
            String uri = (String) categoryMap.get("value");

            res.add(uri);
        }

        return res;
    }

    public String makeRequestAtt(String link) {
        // With this value, we can make a new request
        // The request is :
        // SELECT DISTINCT ?att WHERE { <str> ?att ?other }
        String request = "SELECT DISTINCT ?att WHERE { <" + link + "> ?att ?other }";

        return request;
    }

    public void setStringToParse(String s) {
        this.stringToParse = s;
    }

}
