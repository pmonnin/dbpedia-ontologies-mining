package dbpediaanalyzer.factory;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
interface HierarchyElementFactory<T extends HierarchyElement> {

    public T createHierarchyElement(String uri);

}
