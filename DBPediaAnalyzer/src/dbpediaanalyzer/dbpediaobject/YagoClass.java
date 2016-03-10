package dbpediaanalyzer.dbpediaobject;

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
}
