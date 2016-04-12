package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public abstract class EvaluationStrategy {

    public abstract double computeValue(DataBasedSubsumption subsumption);

}
