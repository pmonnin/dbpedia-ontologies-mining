package dbpediaanalyzer.dbpediaobjects;

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
}
