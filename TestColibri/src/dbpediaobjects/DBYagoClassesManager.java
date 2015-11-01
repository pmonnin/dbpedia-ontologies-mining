package dbpediaobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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
        ArrayList<String> retVal = new ArrayList<>();

        Queue<String> queue = new LinkedList<>();
        queue.add(yagoClass);

        while(!queue.isEmpty()) {
            String y = queue.poll();

            if(this.yagoClasses.containsKey(y)) {
                DBYagoClass dby = this.yagoClasses.get(y);
                if(!retVal.contains(y)) {
                    retVal.add(y);
                }

                for(String parent : dby.getParents()) {
                    queue.add(parent);
                }
            }
        }

        return retVal;
    }
}
