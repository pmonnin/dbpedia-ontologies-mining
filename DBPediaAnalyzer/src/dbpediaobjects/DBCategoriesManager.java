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
@Deprecated
public class DBCategoriesManager {
    private HashMap<String, DBCategory> categories;

    public DBCategoriesManager(HashMap<String, DBCategory> categories) {
        this.categories = categories;
    }

    public DBCategory getCategoryFromUri(String uri) {
        return this.categories.get(uri);
    }

    public ArrayList<DBCategory> getDataSetCategories(HashMap<String, DBPage> dataSet) {
        // Initialization of categories
        for(String catUri : categories.keySet()) {
            categories.get(catUri).setSeen(false);
        }

        // Initialization of queue
        Queue<DBCategory> queue = new LinkedList<>();
        for(String pageUri : dataSet.keySet()) {
            for(DBCategory category : dataSet.get(pageUri).getCategories()) {
                if(!category.getSeen()) {
                    category.setSeen(true);
                    queue.add(category);
                }
            }
        }

        // Categories traversal
        while(!queue.isEmpty()) {
            DBCategory category = queue.poll();

            for(DBCategory parent : category.getParents()) {
                if(!parent.getSeen()) {
                    parent.setSeen(true);
                    queue.add(parent);
                }
            }
        }

        // Add each category to return value + categories reset
        ArrayList<DBCategory> retVal = new ArrayList<>();
        for(String catUri : categories.keySet()) {
            if(categories.get(catUri).getSeen()) {
                retVal.add(categories.get(catUri));
                categories.get(catUri).setSeen(false);
            }
        }

        return retVal;
    }

    public int getDataSetCategoriesNumber(HashMap<String, DBPage> dataSet) {
        return getDataSetCategories(dataSet).size();
    }
}
