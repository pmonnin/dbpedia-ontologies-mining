package dbpediaanalyzer.comparison;

import dbpediaanalyzer.databasedknowledge.DataBasedSubsumption;

/**
 * TODO JAVADOC
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
    public double computeValue(DataBasedSubsumption subsumption) {
        return subsumption.getNumberOfSubmissions();
    }

}
