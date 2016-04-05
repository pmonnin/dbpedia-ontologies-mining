package dbpediaanalyzer.core;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DataBasedSubsumption {
    private HierarchyElement bottom;
    private HierarchyElement top;
    private List<Double> extensionsRatios;

    public DataBasedSubsumption(HierarchyElement bottom, HierarchyElement top, double extensionsRatio) {
        this.bottom = bottom;
        this.top = top;
        this.extensionsRatios = new ArrayList<>();
        this.extensionsRatios.add(extensionsRatio);
    }

    public void newSubmission(double extensionsRatio) {
        this.extensionsRatios.add(extensionsRatio);
    }
}
