package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.simple.parser.ParseException;

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
     * @throws UnsupportedEncodingException thrown when JSON is not valid
     * @throws IOException thrown when server is unavailable
     * @throws ParseException thrown when JSON is not valid
     */
    public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParseException {
        System.out.println("== START MAIN DB YAGO CLASSES CRAWLER ==");
        DBYagoClassesCrawler crawler = new DBYagoClassesCrawler();
        crawler.computeParents();
        DBYagoClassesStatistics stats = new DBYagoClassesStatistics(crawler.getDbYagoClasses());
        stats.computeStatistics();
        stats.displayStatistics();
    }

    /**
     * Getter on the parsed yago classes
     * @return the parsed yago classes with HashMap (key = URI of the yago classes and object is the DBYagoClass)
     */
    public HashMap<String, DBYagoClass> getDbYagoClasses() {
        return this.dbyagoclasses;
    }

    /**
     * Method computing the DBPedia yago classes hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws ParseException
     */
    public void computeParents() throws UnsupportedEncodingException, IOException, ParseException {
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
        
        this.dbyagoclasses = new HashMap<String, DBYagoClass>();
        
        DBYagoClass currentYagoClass = null;
        for (ChildAndParent childAndParent : childrenAndParents) {
            String child = childAndParent.getChild().getValue();
            String parent = childAndParent.getParent() == null ? null : childAndParent.getParent().getValue();

            if (currentYagoClass == null) {
                currentYagoClass = new DBYagoClass(child);
                
                if (parent != null) {
                    currentYagoClass.addParent(parent);
                }
            }
            
            else if (!child.equals(currentYagoClass.getUri())) {
                this.dbyagoclasses.put(currentYagoClass.getUri(), currentYagoClass);
                
                currentYagoClass = new DBYagoClass(child);
                
                if (parent != null) {
                    currentYagoClass.addParent(parent);
                }
            }
            
            else {
                if (parent != null)
                    currentYagoClass.addParent(parent);
            }
        }
        
        if(currentYagoClass != null && this.dbyagoclasses.get(currentYagoClass.getUri()) == null) {
        	this.dbyagoclasses.put(currentYagoClass.getUri(), currentYagoClass);
        }
        
        childrenAndParents.clear();
        
        // Children relationship creation
        for(String key : this.dbyagoclasses.keySet()) {
			for(String parent : this.dbyagoclasses.get(key).getParents()) {
				DBYagoClass p = this.dbyagoclasses.get(parent);
				
				if(p != null)
					p.addChild(key);
			}
		}
    }
}
