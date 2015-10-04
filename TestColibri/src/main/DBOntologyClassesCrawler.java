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
        System.out.println("== START MAIN DB ONTOLOGIES CRAWLER ==");
        DBOntologyClassesCrawler crawler = new DBOntologyClassesCrawler();
        crawler.computeOntologiesHierarchy();
        DBOntologyClassesStatistics stats = new DBOntologyClassesStatistics(crawler.getDbontologies());
        stats.computeStatistics();
        stats.displayStatistics();
    }

    /**
     * Getter on the parsed ontology classes
     * @return the parsed ontology classes with HashMap (key = URI of the ontology classes and object is the DBOnolotyClass)
     */
    public HashMap<String, DBOntologyClass> getDbontologies() {
        return this.dbontologies;
    }

    /**
     * Method computing the DBPedia ontology classes hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws ParseException
     */
    public void computeOntologiesHierarchy() throws UnsupportedEncodingException, IOException, ParseException {
    	// Ask for all the ontology classes
        List<ChildAndParent> childrenAndParents = JSONReader.getChildrenAndParents(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + "select distinct ?child ?parent where {"
                + "?child rdf:type owl:Class ."
                + "FILTER (REGEX(STR(?child), \"http://dbpedia.org/ontology\", \"i\")) ."
                + "OPTIONAL {"
                + "?child rdfs:subClassOf ?parent . "
                + "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/ontology\", \"i\"))"
                + "}}", "UTF-8"));

        this.dbontologies = new HashMap<String, DBOntologyClass>();
        for (ChildAndParent childAndParent : childrenAndParents) {
            String child = childAndParent.getChild().getValue();
            String parent = childAndParent.getParent() == null ? null : childAndParent.getParent().getValue();

            if(this.dbontologies.get(child) == null) {
            	this.dbontologies.put(child, new DBOntologyClass(child));
            }
            
            if(parent != null && !this.dbontologies.get(child).hasParent(parent)) {
            	this.dbontologies.get(child).addParent(parent);
            }
        }
        
        // Children relationship creation
        for(String key : this.dbontologies.keySet()) {
        	for(String parent : this.dbontologies.get(key).getParents()) {
				DBOntologyClass parentOnto = this.dbontologies.get(parent);
				
				if(parentOnto != null)
					parentOnto.addChildren(key);
			}
        }
    }
}
