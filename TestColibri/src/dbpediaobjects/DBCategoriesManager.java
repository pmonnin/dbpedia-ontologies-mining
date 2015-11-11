package dbpediaobjects;

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

    public DBCategory getCategoryFromUri(String uri) {
        return this.categories.get(uri);
    }
}
