package io.github.pmonnin.kcomparison;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a subsumption extracted from the lattice
 * @author Pierre Monnin
 */
public class Subsumption {
    public enum Type {
        DIRECT,
        INFERRED,
        NEW,
        REVERSED
    }

    private Type type;
    private String top;
    private String bottom;
    private int occurrencesNumber;
    private ArrayList<Double> extentRatios;
    private double avgExtentRatio;
    private ArrayList<Double> intentRatios;
    private double avgIntentRatio;
    private ArrayList<Integer> latticeDepths;
    private double avgLatticeDepth;
    private double distanceViaLCA;
    private double wuPalmerSimilarity;

    Subsumption(Type type, String top, String bottom, double extentRatio, double intentRatio, int depth) {
        this.type = type;
        this.top = top;
        this.bottom = bottom;
        this.occurrencesNumber = 1;
        this.extentRatios = new ArrayList<>();
        this.extentRatios.add(extentRatio);
        this.intentRatios = new ArrayList<>();
        this.intentRatios.add(intentRatio);
        this.latticeDepths = new ArrayList<>();
        this.latticeDepths.add(depth);
        this.distanceViaLCA = 0.0;
        this.wuPalmerSimilarity = 0.0;
    }

    void found(double extentRatio, double intentRatio, int depth) {
        extentRatios.add(extentRatio);
        intentRatios.add(intentRatio);
        latticeDepths.add(depth);
        occurrencesNumber++;
    }

    void computeAverages() {
        avgExtentRatio = 0.0;
        avgIntentRatio = 0.0;
        avgLatticeDepth = 0.0;

        for (int i = 0; i < occurrencesNumber; i++) {
            avgExtentRatio += extentRatios.get(i);
            avgIntentRatio += intentRatios.get(i);
            avgLatticeDepth += latticeDepths.get(i);
        }

        avgExtentRatio /= (double) occurrencesNumber;
        avgIntentRatio /= (double) occurrencesNumber;
        avgLatticeDepth /= (double) occurrencesNumber;
    }

    public Type getType() {
        return type;
    }

    public String getTop() {
        return top;
    }

    public String getBottom() {
        return bottom;
    }

    public int getOccurrencesNumber() {
        return occurrencesNumber;
    }

    public List<Double> getExtentRatios() {
        return new ArrayList<>(extentRatios);
    }

    public double getAvgExtentRatio() {
        return avgExtentRatio;
    }

    public List<Double> getIntentRatios() {
        return new ArrayList<>(intentRatios);
    }

    public double getAvgIntentRatio() {
        return avgIntentRatio;
    }

    public List<Integer> getLatticeDepths() {
        return new ArrayList<>(latticeDepths);
    }

    public double getAvgLatticeDepth() {
        return avgLatticeDepth;
    }

    public double getDistanceViaLCA() {
        return distanceViaLCA;
    }

    public double getWuPalmerSimilarity() {
        return wuPalmerSimilarity;
    }
}
