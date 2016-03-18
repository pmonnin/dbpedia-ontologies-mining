package dbpediaanalyzer.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import dbpediaanalyzer.dbpediaobject.DBYagoClassesManager;

import dbpediaanalyzer.dbpediaobject.DBYagoClass;

/**
 * Crawler of the DBPedia Yago classes
 * Also contains a main method to just test the crawler (no comparison)
 * 
 * @author Thomas Herbeth
 * @author Pierre Monnin
 *
 */
@Deprecated
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
        crawler.computeYagoCLassesHierarchy();
    }

    /**
     * Getter on the parsed yago classes
     * @return the parsed yago classes as a manager object
     */
    public DBYagoClassesManager getDBYagoClassesManager() {
        return new DBYagoClassesManager(this.dbyagoclasses);
    }

    /**
     * Method computing the DBPedia yago classes hierarchy
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void computeYagoCLassesHierarchy() throws IOException {
    	
    }
}
