package dbpediaanalyzer.factory;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

/**
 * Builds an ontology class
 *
 * @author Pierre Monnin
 *
 */
interface HierarchyElementFactory<T extends HierarchyElement> {

    public T createHierarchyElement(String uri);

}
