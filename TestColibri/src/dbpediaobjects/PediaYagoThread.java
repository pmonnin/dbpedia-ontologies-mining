package dbpediaobjects;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;

/**
 * Class used to get the parents of a subset of yago classes
 * 
 * @author Thomas Herbeth
 *
 */
public class PediaYagoThread extends Thread {
    private JsonParser parser;
    private URLReader urlReader;
    private ArrayList<DBYagoClass> threadYagoClasses;

    public PediaYagoThread(JsonParser parser, URLReader urlReader, ArrayList<DBYagoClass> threadYagoClasses) {
        super();
        this.parser = parser;
        this.urlReader = urlReader;
        this.threadYagoClasses = threadYagoClasses;
    }

    @Override
    public void run() {
        for (int i = 0; i < threadYagoClasses.size(); i++) {
            DBYagoClass yagoClass = threadYagoClasses.get(i);
            String jsonParents = new String();
            try {
                jsonParents = urlReader.getJSON(URLEncoder.encode("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                        + "select distinct ?parent where " + "{ <" + yagoClass.getUri() + "> rdfs:subClassOf ?parent . " + "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/class/yago\", \"i\")) }",
                        "UTF-8"));

                parser.setStringToParse(jsonParents);

                ArrayList<String> parents = parser.getDbPediaParents("parent");
                yagoClass.setParents(parents);

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<DBYagoClass> getThreadYagoClasses() {
        return threadYagoClasses;
    }
}
