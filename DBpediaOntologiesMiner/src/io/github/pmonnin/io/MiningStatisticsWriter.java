package io.github.pmonnin.io;

import io.github.pmonnin.statistics.MiningStatistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Writes computed mining and annotated lattice statistics into a file
 * @author Pierre Monnin
 */
public class MiningStatisticsWriter {
    private PrintWriter writer;

    public MiningStatisticsWriter() {

    }

    public void open(String fileName) {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName);
            this.writer = null;
        }
    }

    public void writeStatistics(MiningStatistics statistics) {
        if (this.writer != null) {
            this.writer.println("--- Mining statistics ---");
        }
    }

    public void close() {
        if (this.writer != null) {
            this.writer.close();
            this.writer = null;
        }
    }
}
