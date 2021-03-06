package dbpediaanalyzer.databasedknowledge;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a subsumption suggested by the annotated lattice based on data
 *
 * @author Pierre Monnin
 *
 */
public class DataBasedSubsumption {
    private HierarchyElement bottom;
    private HierarchyElement top;
    private List<Double> extensionsRatios;
    private List<Double> intensionsRatios;

    public DataBasedSubsumption(HierarchyElement bottom, HierarchyElement top, double extensionsRatio, double intensionsRatio) {
        this.bottom = bottom;
        this.top = top;
        this.extensionsRatios = new ArrayList<>();
        this.extensionsRatios.add(extensionsRatio);
        this.intensionsRatios = new ArrayList<>();
        this.intensionsRatios.add(intensionsRatio);
    }

    public void newSubmission(double extensionsRatio, double intensionsRatio) {
        this.extensionsRatios.add(extensionsRatio);
        this.intensionsRatios.add(intensionsRatio);
    }

    public HierarchyElement getBottom() {
        return this.bottom;
    }

    public HierarchyElement getTop() {
        return this.top;
    }

    public int getNumberOfSubmissions() {
        return this.extensionsRatios.size();
    }

    public List<Double> getExtensionsRatios() {
        return new ArrayList<>(this.extensionsRatios);
    }

    public List<Double> getIntensionsRatios() {
        return new ArrayList<>(this.intensionsRatios);
    }
}
