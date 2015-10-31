package dbpediaobjects;

import java.util.ArrayList;
import java.util.HashMap;

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

    public ArrayList<String> getSelfAndAncestors(String category) {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(category);

        return retVal;
    }
}
