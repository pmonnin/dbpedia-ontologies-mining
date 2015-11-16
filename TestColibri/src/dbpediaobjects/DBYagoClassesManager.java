package dbpediaobjects;

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

    public DBYagoClass getYagoClassFromUri(String uri) {
        return this.yagoClasses.get(uri);
    }

    public int getDataSetYagoClassesNumber(HashMap<String, DBPage> dataSet) {
        return -1;
    }
}
