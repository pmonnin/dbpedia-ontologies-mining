package dbpediaanalyzer.comparison;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 */
public class ComparisonResult {
    private ComparisonResultType type;
    private HierarchyElement bottom;
    private HierarchyElement top;
    private Map<String, Double> values;

    public ComparisonResult(ComparisonResultType type, HierarchyElement bottom, HierarchyElement top) {
        this.type = type;
        this.bottom = bottom;
        this.top = top;
        this.values = new HashMap<>();
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

    public void addValue(String strategy, double value) {
        this.values.put(strategy, value);
    }

    public Map<String, Double> getValues() {
        return new HashMap<>(this.values);
    }
}
