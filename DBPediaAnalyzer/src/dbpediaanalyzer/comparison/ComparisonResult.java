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
    private double value;

    public ComparisonResult(HierarchyElement bottom, HierarchyElement top, double value) {
        this.bottom = bottom;
        this.top = top;
        this.value = value;
    }

    public HierarchyElement getBottom() {
        return this.bottom;
    }

    public HierarchyElement getTop() {
        return this.top;
    }

    public double getValue() {
        return this.value;
    }
}
