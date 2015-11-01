package dbpediaobjects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manager of DB Yago Classes list created from DBYagoClassesCrawler
 *
 * @author Pierre Monnin
 */
public class DBYagoClassesManager {
    private HashMap<String, DBYagoClass> yagoClasses;

    public DBYagoClassesManager(HashMap<String, DBYagoClass> yagoClasses) {
        this.yagoClasses = yagoClasses;
    }

    public ArrayList<String> getPageYagoClassesAndAncestors(DBPage page) {
        return new ArrayList<>();
    }
}
