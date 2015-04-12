package dbpediaobjects;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;

public class PediaCategoryThread extends Thread {
    private JsonParser parser;
    private URLReader urlReader;
    private ArrayList<DBCategory> threadCategories;

    public PediaCategoryThread(JsonParser parser, URLReader urlReader,
            ArrayList<DBCategory> threadCategories) {
        super();
        this.parser = parser;
        this.urlReader = urlReader;
        this.threadCategories = threadCategories;
    }

    @Override
    public void run() {
        for(int i=0; i<threadCategories.size(); i++) {
            DBCategory cat = threadCategories.get(i);
            String jsonParents = new String();
            try {
                jsonParents = urlReader
                        .getJSON(URLEncoder
                                .encode("PREFIX dcterms:<http://purl.org/dc/terms/> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> PREFIX skos:<http://www.w3.org/2004/02/skos/core#> select distinct ?Category where "
                                        + " { "
                                        + "<"
                                        + cat.getUri()
                                        + "> skos:broader ?Category . "
                                        + "}",
                                        "UTF-8"));

                parser.setStringToParse(jsonParents);

                ArrayList<String> parents = parser.getDbPediaParents("Category");
                cat.setParents(parents);
                
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
