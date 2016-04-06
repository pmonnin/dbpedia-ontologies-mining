package dbpediaanalyzer.comparison;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 */
public abstract class ComparisonResult {
    private HierarchyElement bottom;
    private HierarchyElement top;

    public ComparisonResult(HierarchyElement bottom, HierarchyElement top) {
        this.bottom = bottom;
        this.top = top;
    }
}
