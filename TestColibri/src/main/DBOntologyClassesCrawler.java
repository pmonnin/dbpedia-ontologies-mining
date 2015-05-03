package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.simple.parser.ParseException;

import serverlink.ChildAndParent;
import serverlink.JSONReader;
import statistics.DBOntologyClassesStatistics;
import dbpediaobjects.DBOntologyClass;

/**
 * Crawler of the DBPedia ontology classes
 * Also contains a main method to just test the crawler (no comparison)
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBOntologyClassesCrawler {

    private HashMap<String, DBOntologyClass> dbontologies;

    /**
     * Main method to test the crawler 
     * @param args not used
     * @throws UnsupportedEncodingException thrown when JSON is not valid
     * @throws IOException thrown when server is unavailable
     * @throws ParseException thrown when JSON is not valid
     */
    public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParseException {
        System.out.println("START MAIN");
        DBOntologyClassesCrawler crawler = new DBOntologyClassesCrawler();
        crawler.computeParents();
        DBOntologyClassesStatistics stats = new DBOntologyClassesStatistics(crawler.getDbontologies());
        stats.computeStatistics();
        stats.displayStatistics();
    }

    /**
     * Getter on the parsed ontology classes
     * @return the parsed ontology classes with HashMap (key = URI of the ontology classes and object is the DBOnolotyClass)
     */
    public HashMap<String, DBOntologyClass> getDbontologies() {
        return dbontologies;
    }

    /**
     * Method computing the DBPedia ontology classes hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws ParseException
     */
    public void computeParents() throws UnsupportedEncodingException, IOException, ParseException {
    	// Ask for all the ontology classes
        List<ChildAndParent> childrenAndParents = JSONReader.getChildrenAndParents(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + "select distinct ?child ?parent ?label where {"
                + "?child rdf:type owl:Class ."
                + "FILTER (REGEX(STR(?child), \"http://dbpedia.org/ontology\", \"i\")) ."
                + "?child rdfs:label ?label ."
                + "FILTER(langMatches(lang(?label), \"EN\"))"
                + "OPTIONAL {"
                + "?child rdfs:subClassOf ?parent . "
                + "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/ontology\", \"i\"))"
                + "}}", "UTF-8"));

        dbontologies = new HashMap<String, DBOntologyClass>();
        DBOntologyClass currentOntology = null;
        for (ChildAndParent childAndParent : childrenAndParents) {
            String child = childAndParent.getChild().getValue();
            String label = childAndParent.getLabel().getValue();
            String parent = childAndParent.getParent() == null ? null : childAndParent.getParent().getValue();

            if (currentOntology == null) {
                currentOntology = new DBOntologyClass(label, child);
                if (parent != null)
                    currentOntology.addParent(parent);
            } else if (!child.equals(currentOntology.getUri())) {
                dbontologies.put(currentOntology.getUri(), currentOntology);
                currentOntology = new DBOntologyClass(label, child);
                if (parent != null)
                    currentOntology.addParent(parent);
//                System.out.println("ADD PARENT " + child + " -> " + parent);
            } else {
                if (parent != null)
                    currentOntology.addParent(parent);
            }
        }
    }
}
