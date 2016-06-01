package dbpediaanalyzer.io;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;
import dbpediaanalyzer.statistic.HierarchyStatistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class HierarchiesStatisticsWriter extends AbstractStatisticsWriter {

    public HierarchiesStatisticsWriter(String fileName) {
        super(fileName, "hierarchies");
    }

    public void writeHierarchyStatistics(HierarchyStatistics hs, String hierarchyName) {
        println("--- " + hierarchyName + " statistics ---");
        println("Elements number: " + hs.getElementsNumber());
        println("Orphans number: " + hs.getOrphansNumber());
        println("Depth: " + hs.getDepth());
        println("Inaccessible elements during depth calculation: " + hs.getDepthInaccessibleElements());
        println("Direct subsumptions number: " + hs.getDirectSubsumptions());
        println("Inferred subsumptions number: " + hs.getInferredSubsumptions());

        List<List<HierarchyElement>> cycles = hs.getCycles();
        println("Cycles number: " + cycles.size());

        if(!cycles.isEmpty()) {
            try {
                PrintWriter cyclesWriter = new PrintWriter(new BufferedWriter(new FileWriter(getFileName() + "-" +
                        hierarchyName.replace(" ", "-") + "-cycles")));

                for(List<HierarchyElement> cycle : cycles) {
                    cyclesWriter.print("[ ");

                    for(int i = 0; i < cycle.size(); i++) {
                        cyclesWriter.print(cycle.get(i).getUri());

                        if(i < cycle.size() - 1) {
                            cyclesWriter.print(" -> ");
                        }
                    }

                    cyclesWriter.println(" ]");
                }

                cyclesWriter.close();
            }

            catch(IOException e) {
                System.err.println("Error while trying to save cycles for " + hierarchyName + "");
            }
        }

        print("Depth path: ");
        List<HierarchyElement> depthPath = hs.getDepthPath();
        for(int i = 0 ; i < depthPath.size() ; i++) {
            print(depthPath.get(i).getUri());

            if(i != depthPath.size() - 1) {
                print(" -> ");
            }

            else {
                print("\n");
            }
        }
    }

}
