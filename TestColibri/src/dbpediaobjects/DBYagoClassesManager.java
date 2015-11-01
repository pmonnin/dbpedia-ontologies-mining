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
        ArrayList<String> retVal = new ArrayList<>();

        for(String yagoClass : page.getYagoClasses()) {
            for(String y : getSelfAndAncestors(yagoClass)) {
                if(!retVal.contains(y)) {
                    retVal.add(y);
                }
            }
        }

        return retVal;
    }

    public ArrayList<String> getSelfAndAncestors(String yagoClass) {
        ArrayList<String> retVal =  new ArrayList<>();
        retVal.add(yagoClass);

        return retVal;
    }
}
