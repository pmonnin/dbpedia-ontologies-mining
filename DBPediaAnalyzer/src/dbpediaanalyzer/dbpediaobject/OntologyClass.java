package dbpediaanalyzer.dbpediaobject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class OntologyClass extends HierarchyElement {

    public OntologyClass(String uri) {
        super(uri);
    }

    @Override
    public void addParent(HierarchyElement parent) {
        if(parent instanceof OntologyClass) {
            super.addParent(parent);
        }
    }

    @Override
    public void addChild(HierarchyElement child) {
        if(child instanceof OntologyClass) {
            super.addChild(child);
        }
    }

    public static ArrayList<OntologyClass> getAccessibleUpwardOntologyClasses(Collection<OntologyClass> fromSubset) {
        Collection<HierarchyElement> accessible = HierarchyElement.getAccessibleUpwardElements(fromSubset);
        ArrayList<OntologyClass> retVal = new ArrayList<>();

        for(HierarchyElement he : accessible) {
            retVal.add((OntologyClass) he);
        }

        return retVal;
    }
}
