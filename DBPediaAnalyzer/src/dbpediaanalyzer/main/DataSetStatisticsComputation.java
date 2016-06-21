package dbpediaanalyzer.main;

import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.dbpediaobject.Page;
import dbpediaanalyzer.factory.DataSetFactory;
import dbpediaanalyzer.factory.HierarchiesFactory;
import dbpediaanalyzer.io.DataSetStatisticsWriter;
import dbpediaanalyzer.statistic.DataSetStatistics;
import dbpediaanalyzer.util.TimeMeasurer;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class DataSetStatisticsComputation {

    public static void main(String[] args) {
        boolean incorrectParams;

        if(args.length != 3) {
            incorrectParams = true;
        }

        else {
            // TODO Improve this pattern
            Pattern pattern = Pattern.compile("[0-9][0-9][0-9][0-9]-(1[0-2]|0[0-9])-(3[0-1]|[0-2][0-9])");
            incorrectParams = !pattern.matcher(args[0]).find() || !pattern.matcher(args[1]).find();
        }

        if(incorrectParams) {
            System.out.println("Usage:\n java DataSetStatisticsComputation minimalDeathDate maximalDeathDate output");
            System.out.println("\t minimalDeathDate\n\t\t minimal death date for data set creation (YYYY-MM-DD)");
            System.out.println("\t maximalDeathDate\n\t\t maximal death date for data set creation (YYYY-MM-DD)");
            System.out.println("\t output\n\t\t file used to store data set statistics");
        }

        else {
            System.out.println("=== DATA SET STATISTICS COMPUTATION ===");

            TimeMeasurer tm = new TimeMeasurer();
            tm.begin();

            System.out.println("Data set creation...");
            System.out.println("\t Querying and parsing DBPedia hierarchies...");
            HierarchiesManager hm = HierarchiesFactory.createHierarchies();

            System.out.println("\t Querying and parsing data set pages...");
            Map<String, Page> dataSet = DataSetFactory.createDataSet(args[0], args[1], hm);

            System.out.println("Data set statistics computation...");
            System.out.println("\t Computing data set statistics...");
            DataSetStatistics dataSetStatistics = new DataSetStatistics(dataSet);

            System.out.println("\t Saving data set statistics...");
            DataSetStatisticsWriter dataSetStatisticsWriter = new DataSetStatisticsWriter(args[2]);
            dataSetStatisticsWriter.writeDataSetStatistics(dataSetStatistics);
            dataSetStatisticsWriter.close();

            tm.stop();
            System.out.println("Processing time: " + tm.toString());
            System.out.println("=== End of data set statistics computation program ===");
        }
    }

}
