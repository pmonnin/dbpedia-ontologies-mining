package dbpediaanalyzer.main;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import dbpediaanalyzer.dbpediaobject.DBCategoriesManager;

import dbpediaanalyzer.dbpediaobject.DBCategory;

/**
 * Crawler of the DBPedia categories
 * Also contains a main method to just test the crawler (no comparison)
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
@Deprecated
public class DBCategoriesCrawler {

    private HashMap<String, DBCategory> dbcategories;

    /**
     * Main method to test the crawler 
     * @param args not used
     * @throws IOException thrown when server is unavailable
     */
    public static void main(String[] args) throws IOException {
        System.out.println("=== START MAIN DB CATEGORIES CRAWLER ===");
        DBCategoriesCrawler crawler = new DBCategoriesCrawler();
        crawler.computeCategoriesHierarchy();
    }

    /**
     * Getter on the parsed categories
     * @return the parsed categories as a manager object
     */
    public DBCategoriesManager getDBCategoriesManager() {
        return new DBCategoriesManager(this.dbcategories);
    }
    
    /**
     * Method computing the DBPedia categories hierarchy
     * @throws IOException
     */
    public void computeCategoriesHierarchy() throws IOException {

    }
}
