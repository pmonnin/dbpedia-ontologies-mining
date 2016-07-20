package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;

/**
 * Evaluates an axiom based on the number of times it is suggested by the anntated lattice
 *
 * @author Pierre Monnin
 *
 */
public class NumberOfSubmissionsStrategy extends EvaluationStrategy{

    @Override
    public String getName() {
        return "NumberOfSubmissions";
    }

    @Override
    protected double computeValue(DataBasedSubsumption subsumption) {
        return subsumption.getNumberOfSubmissions();
    }

}
