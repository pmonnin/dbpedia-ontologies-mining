package dbpediaobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Manager of DB Categories list created from DBCategoriesCrawler
 *
 * @author Pierre Monnin
 */
public class DBCategoriesManager {
    public HashMap<String, DBCategory> categories;

    public DBCategoriesManager(HashMap<String, DBCategory> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getPageCategoriesAndAncestors(DBPage page) {
        ArrayList<String> retVal = new ArrayList<>();

        for(String category : page.getCategories()) {
            for(String c : getSelfAndAncestors(category)) {
                if(!retVal.contains(c)) {
                    retVal.add(c);
                }
            }
        }

        return retVal;
    }

    public ArrayList<String> getSelfAndAncestors(String category) {
        ArrayList<String> retVal = new ArrayList<>();

        Queue<String> queue = new LinkedList<>();
        queue.add(category);

        while(!queue.isEmpty()) {
            String c = queue.poll();

            if(this.categories.containsKey(c)) {
                DBCategory dbc = this.categories.get(c);
                if(!retVal.contains(c)) {
                    retVal.add(c);
                }

                for(String parent : dbc.getParents()) {
                    queue.add(parent);
                }
            }
        }

        return retVal;
    }
}
