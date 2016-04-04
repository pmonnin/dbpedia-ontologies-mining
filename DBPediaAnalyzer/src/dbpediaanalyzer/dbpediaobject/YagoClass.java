package dbpediaanalyzer.dbpediaobject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class YagoClass extends HierarchyElement {

    public YagoClass(String uri) {
        super(uri);
    }

    @Override
    public void addParent(HierarchyElement parent) {
        if(parent instanceof YagoClass) {
            super.addParent(parent);
        }
    }

    @Override
    public void addChild(HierarchyElement child) {
        if(child instanceof YagoClass) {
            super.addChild(child);
        }
    }

    public static ArrayList<YagoClass> getAccessibleYagoClasses(Collection<YagoClass> fromSubset) {
        Collection<HierarchyElement> accessible = HierarchyElement.getAccessibleElements(fromSubset);
        ArrayList<YagoClass> retVal = new ArrayList<>();

        for(HierarchyElement he : accessible) {
            retVal.add((YagoClass) he);
        }

        return retVal;
    }
}
