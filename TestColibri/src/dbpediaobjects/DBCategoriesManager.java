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
}
