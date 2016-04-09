package dbpediaanalyzer.comparison;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 */
public class ComparisonResult {
    private ComparisonResultType type;
    private HierarchyElement bottom;
    private HierarchyElement top;
    private double value;

    public ComparisonResult(ComparisonResultType type, HierarchyElement bottom, HierarchyElement top, double value) {
        this.type = type;
        this.bottom = bottom;
        this.top = top;
        this.value = value;
    }

    public ComparisonResultType getType() {
        return this.type;
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
