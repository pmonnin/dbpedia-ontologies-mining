package dbpediaobjects;

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

    public DBCategory getCategoryFromUri(String uri) {
        return this.categories.get(uri);
    }

    public int getDataSetCategoriesNumber(HashMap<String, DBPage> dataSet) {
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
            DBCategory category = queue.peek();

            for(DBCategory parent : category.getParents()) {
                if(!parent.getSeen()) {
                    parent.setSeen(true);
                    queue.add(parent);
                }
            }
        }

        // Count + categories reset
        int retVal = 0;
        for(String catUri : categories.keySet()) {
            if(categories.get(catUri).getSeen()) {
                retVal++;
                categories.get(catUri).setSeen(false);
            }
        }

        return retVal;
    }
}
