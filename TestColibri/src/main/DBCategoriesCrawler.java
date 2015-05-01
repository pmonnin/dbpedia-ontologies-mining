package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.simple.parser.ParseException;

import serverlink.ChildAndParent;
import serverlink.JSONReader;
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
     * @throws UnsupportedEncodingException thrown when JSON is not valid
     * @throws IOException thrown when server is unavailable
     * @throws ParseException thrown when JSON is not valid
     */
    public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParseException {
        System.out.println("START MAIN");
        new DBCategoriesCrawler().computeParents();
    }

    /**
     * Getter on the parsed categories
     * @return the parsed categories with HashMap (key = URI of the categories and object is the DBCategory)
     */
    public HashMap<String, DBCategory> getDbcategories() {
        return dbcategories;
    }

    /**
     * Method computing the DBPedia categories hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws ParseException
     */
    public void computeParents() throws UnsupportedEncodingException, IOException, ParseException {
    	// Ask for all the categories
        List<ChildAndParent> childrenAndParents = JSONReader.getChildrenAndParents(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
                + "select distinct ?child ?parent ?label where {"
                + "?child rdf:type skos:Concept ."
                + "FILTER (REGEX(STR(?child), \"http://dbpedia.org/resource/Category\", \"i\")) ."
                + "?child rdfs:label ?label ."
                + "FILTER(langMatches(lang(?label), \"EN\"))"
                + "OPTIONAL {"
                + "?child skos:broader ?parent . "
                + "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/resource/Category\", \"i\"))"
                + "}}", "UTF-8"));

        dbcategories = new HashMap<String, DBCategory>();
        DBCategory currentCategory = null;
        for (ChildAndParent childAndParent : childrenAndParents) {
            String child = childAndParent.getChild().getValue();
            String label = childAndParent.getLabel().getValue();
            String parent = childAndParent.getParent() == null ? null : childAndParent.getParent().getValue();

            if (currentCategory == null) {
                currentCategory = new DBCategory(label, child);
                if (parent != null)
                    currentCategory.addParent(parent);
            } else if (!child.equals(currentCategory.getUri())) {
                dbcategories.put(currentCategory.getUri(), currentCategory);
                currentCategory = new DBCategory(label, child);
                if (parent != null)
                    currentCategory.addParent(parent);
//                System.out.println("ADD PARENT " + child + " -> " + parent);
            } else {
                if (parent != null)
                    currentCategory.addParent(parent);
            }
        }
        
        System.out.println("Nombre total de catégories : " + dbcategories.size());
    }
}
