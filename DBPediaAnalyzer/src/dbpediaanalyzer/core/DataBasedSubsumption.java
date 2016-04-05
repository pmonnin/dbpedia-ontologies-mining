package dbpediaanalyzer.core;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DataBasedSubsumption {
    private HierarchyElement bottom;
    private HierarchyElement top;
    private int numberOfSubmissions;

    public DataBasedSubsumption(HierarchyElement bottom, HierarchyElement top) {
        this.bottom = bottom;
        this.top = top;
        this.numberOfSubmissions = 1;
    }

    public void incrementNumberOfSubmissions() {
        this.numberOfSubmissions++;
    }
}
