package dbpediaobjects;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;

public class PediaOntologyThread extends Thread {
    private JsonParser parser;
    private URLReader urlReader;
    private ArrayList<DBOntologyClass> threadOntologies;
    private HashMap<String, DBOntologyClass> dbontologies;

    public PediaOntologyThread(JsonParser parser, URLReader urlReader, HashMap<String, DBOntologyClass> dbontologies,
    		ArrayList<DBOntologyClass> threadOntologies) {
        super();
        this.parser = parser;
        this.urlReader = urlReader;
        this.threadOntologies = threadOntologies;
        this.dbontologies = dbontologies;
    }

    @Override
    public void run() {
        for(int i=0; i < threadOntologies.size(); i++) {
            DBOntologyClass cat = threadOntologies.get(i);
            String jsonParents = new String();
            try {
                jsonParents = urlReader
                        .getJSON(URLEncoder
                                .encode("select distinct ?Ontology1 ?Ontology2 where { "
                                		+ "?Ontology1 rdfs:subClassOf ?Ontology2 . "
                                		+ "FILTER(STRSTARTS(STR(?Ontology1), \"http://dbpedia.org/ontology\")) . "
                                		+ "FILTER(STRSTARTS(STR(?Ontology2), \"http://dbpedia.org/ontology\")) . "
                                		+ "}",
                                        "UTF-8"));

                parser.setStringToParse(jsonParents);
                HashMap<String, DBOntologyClass> parents;

                parents = parser.getDbPediaOntologyParents();
                dbontologies.putAll(parents);

                Set<String> parentsKeys = parents.keySet();

                for (String parentKey : parentsKeys) {
                    DBOntologyClass parent = dbontologies.get(parentKey);
                    if(parent != null) {
                        System.out.println("FOUND " + parent.getUri() + " AS PARENT FOR " + cat.getUri());
                        cat.addParent(parent);
                        threadOntologies.set(i, cat);
                    }
                }
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
