package main;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import dbpediaobjects.DBCategoriesManager;
import org.json.simple.parser.ParseException;

import serverlink.ChildAndParent;
import serverlink.JSONReader;
import statistics.DBCategoriesStatistics;
import dbpediaobjects.DBCategory;

/**
 * Crawler of the DBPedia categories
 * Also contains a main method to just test the crawler (no comparison)
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
public class DBCategoriesCrawler {

    private HashMap<String, DBCategory> dbcategories;

    /**
     * Main method to test the crawler 
     * @param args not used
     * @throws IOException thrown when server is unavailable
     * @throws ParseException thrown when JSON is not valid
     */
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("== START MAIN DB CATEGORIES CRAWLER ==");
        DBCategoriesCrawler crawler = new DBCategoriesCrawler();
        crawler.computeCategoriesHierarchy();
        DBCategoriesStatistics stats = new DBCategoriesStatistics(crawler.dbcategories);
        stats.computeStatistics();
        stats.displayStatistics();
    }

    /**
     * Getter on the parsed categories
     * @return the parsed categories as a manager object)
     */
    public DBCategoriesManager getDBCategoriesManager() {
        return new DBCategoriesManager(this.dbcategories);
    }
    
    /**
     * Method computing the DBPedia categories hierarchy
     * @throws IOException
     * @throws ParseException
     */
    public void computeCategoriesHierarchy() throws IOException, ParseException {
    	// Ask for all the categories
        List<ChildAndParent> childrenAndParents = JSONReader.getChildrenAndParents(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
                + "select distinct ?child ?parent where {"
                + "?child rdf:type skos:Concept ."
                + "FILTER (REGEX(STR(?child), \"http://dbpedia.org/resource/Category\", \"i\")) ."
                + "OPTIONAL {"
                + "?child skos:broader ?parent . "
                + "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/resource/Category\", \"i\"))"
                + "}}", "UTF-8"));

        this.dbcategories = new HashMap<>();
        
        for(ChildAndParent childAndParent : childrenAndParents) {
            String child = childAndParent.getChild().getValue();

            if(this.dbcategories.get(child) == null) {
            	this.dbcategories.put(child, new DBCategory(child));
            }
        }

        // Hierarchy relationship creation
        for(ChildAndParent childAndParent : childrenAndParents) {
            DBCategory child = this.dbcategories.get(childAndParent.getChild().getValue());
            DBCategory parent = this.dbcategories.get(childAndParent.getParent().getValue());

            if(child != null && parent != null) {
                child.addParent(parent);
                parent.addChild(child);
            }
        }
        
        childrenAndParents.clear();
    }
}
