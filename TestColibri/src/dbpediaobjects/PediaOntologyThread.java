package dbpediaobjects;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;

/**
 * Class used to get the parents of a subset of ontology classes
 * 
 * @author Thomas Herbeth
 *
 */
public class PediaOntologyThread extends Thread {
    private JsonParser parser;
    private URLReader urlReader;
    private ArrayList<DBOntologyClass> threadOntologies;

    public PediaOntologyThread(JsonParser parser, URLReader urlReader,
    		ArrayList<DBOntologyClass> threadOntologies) {
        super();
        this.parser = parser;
        this.urlReader = urlReader;
        this.threadOntologies = threadOntologies;
    }

    @Override
    public void run() {
        for(int i=0; i < threadOntologies.size(); i++) {
            DBOntologyClass ont = threadOntologies.get(i);
            String jsonParents = new String();
            try {
                jsonParents = urlReader
                        .getJSON(URLEncoder
                                .encode("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                                		+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                                		+ "select distinct ?Ontology2 where "
                                		+ "{ <" + ont.getUri() + "> rdfs:subClassOf ?Ontology2 . "
                                		+ "FILTER (REGEX(STR(?Ontology2), \"http://dbpedia.org/ontology\", \"i\")) }",
                                        "UTF-8"));

                parser.setStringToParse(jsonParents);

                ArrayList<String> parents = parser.getDbPediaParents("Ontology2");
                ont.setParents(parents);
                
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<DBOntologyClass> getThreadCategories() {
        return threadOntologies;
    }
}
