package dbpediaanalyzer.dbpediaobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a class of the ontology DBpedia Categories
 *
 * @author Pierre Monnin
 *
 */
public class Category extends HierarchyElement {
    public static final String DBPEDIA_CATEGORY_URI_PREFIX = "http://dbpedia.org/resource/Category";

    public Category(String uri) {
        super(uri);
    }

    @Override
    public void addParent(HierarchyElement parent) {
        if(parent instanceof Category) {
            super.addParent(parent);
        }
    }

    @Override
    public void addChild(HierarchyElement child) {
        if(child instanceof Category) {
            super.addChild(child);
        }
    }

    public static List<Category> getAccessibleUpwardCategories(Collection<Category> fromSubset) {
        Collection<HierarchyElement> accessible = HierarchyElement.getAccessibleUpwardElements(fromSubset);
        ArrayList<Category> retVal = new ArrayList<>();

        for(HierarchyElement he : accessible) {
            retVal.add((Category) he);
        }

        return retVal;
    }
}
