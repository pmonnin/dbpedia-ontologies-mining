package dbpediaanalyzer.dbpediaobject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class Category extends HierarchyElement {

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

    public static ArrayList<Category> getAccessibleCategories(Collection<Category> fromSubset) {
        Collection<HierarchyElement> accessible = HierarchyElement.getAccessibleElements(fromSubset);
        ArrayList<Category> retVal = new ArrayList<>();

        for(HierarchyElement he : accessible) {
            retVal.add((Category) he);
        }

        return retVal;
    }
}
