package dbpediaanalyzer.dbpediaobjects;

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
}
