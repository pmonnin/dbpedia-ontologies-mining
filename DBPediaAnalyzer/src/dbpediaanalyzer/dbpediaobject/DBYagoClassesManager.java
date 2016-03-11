package dbpediaanalyzer.dbpediaobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Manager of DB Yago Classes list created from DBYagoClassesCrawler
 *
 * @author Pierre Monnin
 */
@Deprecated
public class DBYagoClassesManager {
    private HashMap<String, DBYagoClass> yagoClasses;

    public DBYagoClassesManager(HashMap<String, DBYagoClass> yagoClasses) {
        this.yagoClasses = yagoClasses;
    }

    public DBYagoClass getYagoClassFromUri(String uri) {
        return this.yagoClasses.get(uri);
    }

    public ArrayList<DBYagoClass> getDataSetYagoClasses(HashMap<String, Page> dataSet) {
        // Initialization of yago classes
        for(String yagoUri : yagoClasses.keySet()) {
            yagoClasses.get(yagoUri).setSeen(false);
        }

        // Initialization of queue
        Queue<DBYagoClass> queue = new LinkedList<>();
        for(String pageUri : dataSet.keySet()) {
            /*for(DBYagoClass yagoClass : dataSet.get(pageUri).getYagoClasses()) {
                if(!yagoClass.getSeen()) {
                    yagoClass.setSeen(true);
                    queue.add(yagoClass);
                }
            }*/
        }

        // Yago classes traversal
        while(!queue.isEmpty()) {
            DBYagoClass yagoClass = queue.poll();

            for(DBYagoClass parent : yagoClass.getParents()) {
                if(!parent.getSeen()) {
                    parent.setSeen(true);
                    queue.add(parent);
                }
            }
        }

        // Count + yago classes reset
        ArrayList<DBYagoClass> retVal = new ArrayList<>();
        for(String yagoUri : yagoClasses.keySet()) {
            if(yagoClasses.get(yagoUri).getSeen()) {
                retVal.add(yagoClasses.get(yagoUri));
                yagoClasses.get(yagoUri).setSeen(false);
            }
        }

        return retVal;
    }

    public int getDataSetYagoClassesNumber(HashMap<String, Page> dataSet) {
        return getDataSetYagoClasses(dataSet).size();
    }
}
