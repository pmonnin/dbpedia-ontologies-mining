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
public class HierarchiesStatisticsWriter {
    private String fileName;
    private PrintWriter writer;

    public HierarchiesStatisticsWriter(String fileName) {
        try {
            this.fileName = fileName;
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName + " to save hierarchies statistics");
        }
    }

    public void writeHierarchyStatistics(HierarchyStatistics hs, String hierarchyName) {
        this.writer.println("--- " + hierarchyName + " statistics ---");
        this.writer.println("Elements number: " + hs.getElementsNumber());
        this.writer.println("Orphans number: " + hs.getOrphansNumber());
        this.writer.println("Depth: " + hs.getDepth());
        this.writer.println("Direct subsumptions number: " + hs.getDirectSubsumptions());
        this.writer.println("Inferred subsumptions number: " + hs.getInferredSubsumptions());

        List<List<HierarchyElement>> cycles = hs.getCycles();
        this.writer.println("Cycles number: " + cycles.size());

        if(!cycles.isEmpty()) {
            try {
                PrintWriter cyclesWriter = new PrintWriter(new BufferedWriter(new FileWriter(this.fileName + "-" +
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
    }

    public void close() {
        writer.close();
    }

}
