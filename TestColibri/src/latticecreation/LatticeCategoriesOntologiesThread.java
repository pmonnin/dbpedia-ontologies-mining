package latticecreation;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;

public class LatticeCategoriesOntologiesThread extends Thread {
    private ArrayList<DBPage> threadObjects;

    public LatticeCategoriesOntologiesThread(ArrayList<DBPage> threadObjects) {
        super();
        this.threadObjects = threadObjects;
    }

    @Override
    public void run() {
        for (int i = 0; i < threadObjects.size(); i++) {
            DBPage obj = threadObjects.get(i);
            URLReader urlReader = new URLReader();
            JsonParser parser = new JsonParser("");

            try {
                String request = parser.makeRequestAtt(obj.getURI());

                // We get the response
                String response = urlReader.getJSON(URLEncoder.encode(request, "UTF-8"));

                // We parse it to get the different attributes of the thing
                parser.setStringToParse(response);
                ArrayList<String> attributes = parser.getResults("att");

                for (int j = 0; j < attributes.size(); j++) {
                    // We add the attributes to the object
                    obj.addRelationship(attributes.get(j));
                }

                // We get the ontologies for the thing
                String jsonOnto = urlReader.getJSON(URLEncoder.encode("PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                                                                      + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                                                                      + "select distinct ?Ontology2 where "
                                                                      + "{ <" + obj.getURI() +  "> rdf:type ?Ontology2 . "
                                                                      + "FILTER (REGEX(STR(?Ontology2), \"http://dbpedia.org/ontology\", \"i\")) }", "UTF-8"));
                parser.setStringToParse(jsonOnto);
                ArrayList<String> ontologies = parser.getDbPediaParents("Ontology2");
                obj.setOntologies(ontologies);
                
                
                // We get the categories for the thing
                String jsonCategories = urlReader.getJSON(URLEncoder.encode("PREFIX dcterms:<http://purl.org/dc/terms/> "
                                                                            + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                                                                            + "select distinct ?Category where {"
                                                                            + "<" + obj.getURI() + "> dcterms:subject ?Category }", "UTF-8"));
                parser.setStringToParse(jsonCategories);
                ArrayList<String> categories = parser.getDbPediaParents("Category");
                obj.setCategories(categories);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<DBPage> getThreadObjects() {
        return threadObjects;
    }

}
