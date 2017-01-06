package io.github.pmonnin.statistics;

import io.github.pmonnin.fca.FormalContext;

/**
 * Compute and store statistics about the formal context given to the constructor
 * @author Pierre Monnin
 */
public class FormalContextStatistics {
    private int objectsNumber;
    private int attributesNumber;
    private double attributesPerObject;

    public FormalContextStatistics(FormalContext fc) {
        objectsNumber = fc.getObjectsNumber();
        attributesNumber = fc.getAttributesNumber();

        if (objectsNumber != 0) {
            attributesPerObject = 0.0;
            for (int i = 0; i < fc.getObjectsNumber(); i++) {
                attributesPerObject += fc.getObjectIncidence(i).size();
            }
            attributesPerObject /= objectsNumber;
        }

        else
            attributesPerObject = -1;
    }

    public int getObjectsNumber() {
        return objectsNumber;
    }

    public int getAttributesNumber() {
        return attributesNumber;
    }

    public double getAttributesPerObject() {
        return attributesPerObject;
    }
}
