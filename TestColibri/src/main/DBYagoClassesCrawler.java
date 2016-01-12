package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import dbpediaobjects.DBYagoClassesManager;

import serverlink.ChildAndParent;
import serverlink.JSONReader;
import statistics.DBYagoClassesStatistics;
import dbpediaobjects.DBYagoClass;

/**
 * Crawler of the DBPedia Yago classes
 * Also contains a main method to just test the crawler (no comparison)
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBYagoClassesCrawler {

    private HashMap<String, DBYagoClass> dbyagoclasses;

    /**
     * Main method to test the crawler 
     * @param args not used
     * @throws IOException thrown when server is unavailable
     */
    public static void main(String[] args) throws IOException {
        System.out.println("== START MAIN DB YAGO CLASSES CRAWLER ==");
        DBYagoClassesCrawler crawler = new DBYagoClassesCrawler();
        crawler.computeParents();
        DBYagoClassesStatistics stats = new DBYagoClassesStatistics(crawler.dbyagoclasses);
        stats.computeStatistics();
        stats.displayStatistics();
    }

    /**
     * Getter on the parsed yago classes
     * @return the parsed yago classes as a manager object)
     */
    public DBYagoClassesManager getDBYagoClassesManager() {
        return new DBYagoClassesManager(this.dbyagoclasses);
    }

    /**
     * Method computing the DBPedia yago classes hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void computeParents() throws IOException {
    	// Ask for all the yago classes that don't have parents
    	List<ChildAndParent> children = JSONReader.getChildrenAndParents(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + "select distinct ?child where {"
                + "[] rdfs:subClassOf ?child . "
            	+ "FILTER (NOT EXISTS "
    			+ " { "
    			+ "?child rdfs:subClassOf ?parent . "
    			+ "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/class/yago\", \"i\")) . "
    			+ "}) ." 
    			+ "FILTER (REGEX(STR(?child), \"http://dbpedia.org/class/yago\", \"i\")) ."
                + "}", "UTF-8"));
    	
        // Ask for all the yago classes that have parents
        List<ChildAndParent> childrenAndParents = JSONReader.getChildrenAndParents(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + "select distinct ?child ?parent where {"
                + "FILTER (REGEX(STR(?child), \"http://dbpedia.org/class/yago\", \"i\")) ."
                + "OPTIONAL {"
                + "?child rdfs:subClassOf ?parent . "
                + "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/class/yago\", \"i\"))"
                + "}}", "UTF-8"));
        
        // Building complete list
        childrenAndParents.addAll(children);
        
        this.dbyagoclasses = new HashMap<>();
        
        for (ChildAndParent childAndParent : childrenAndParents) {
            String child = childAndParent.getChild().getValue();

            if(this.dbyagoclasses.get(child) == null) {
                this.dbyagoclasses.put(child, new DBYagoClass(child));
            }
        }
        
        // Children relationship creation
        for(ChildAndParent childAndParent : childrenAndParents) {
            DBYagoClass child = this.dbyagoclasses.get(childAndParent.getChild().getValue());
            DBYagoClass parent = (childAndParent.getParent() == null) ? null : this.dbyagoclasses.get(childAndParent.getParent().getValue());

            if(child != null && parent != null) {
                child.addParent(parent);
                parent.addChild(child);
            }
        }

        childrenAndParents.clear();
    }
}
