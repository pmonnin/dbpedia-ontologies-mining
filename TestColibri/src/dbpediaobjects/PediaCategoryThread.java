package dbpediaobjects;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;

public class PediaCategoryThread extends Thread {
    private JsonParser parser;
    private URLReader urlReader;
    private ArrayList<DBCategory> threadCategories;
    private HashMap<String, DBCategory> dbcategories;

    public PediaCategoryThread(JsonParser parser, URLReader urlReader,
            HashMap<String, DBCategory> dbcategories,
            ArrayList<DBCategory> threadCategories) {
        super();
        this.parser = parser;
        this.urlReader = urlReader;
        this.threadCategories = threadCategories;
        this.dbcategories = dbcategories;
    }

    @Override
    public void run() {
        for(int i=0; i<threadCategories.size(); i++) {
            DBCategory cat = threadCategories.get(i);
            String jsonParents = new String();
            try {
                jsonParents = urlReader
                        .getJSON(URLEncoder
                                .encode("PREFIX dcterms:<http://purl.org/dc/terms/> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> PREFIX skos:<http://www.w3.org/2004/02/skos/core#> select distinct ?Category ?Label where "
                                        + " { "
                                        + "<"
                                        + cat.getUri()
                                        + "> skos:broader ?Category . "
                                        + "?Category rdfs:label ?Label" + "}",
                                        "UTF-8"));

                parser.setStringToParse(jsonParents);
                HashMap<String, DBCategory> parents;

                parents = parser.getDbPediaCategories();
                dbcategories.putAll(parents);

                Set<String> parentsKeys = parents.keySet();

                for (String parentKey : parentsKeys) {
                    DBCategory parent = dbcategories.get(parentKey);
                    if(parent != null) {
                        System.out.println("FOUND " + parent.getUri() + " AS PARENT FOR " + cat.getUri());
                        cat.addParent(parent);
                        threadCategories.set(i, cat);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<DBCategory> getThreadCategories() {
        return threadCategories;
    }
}
