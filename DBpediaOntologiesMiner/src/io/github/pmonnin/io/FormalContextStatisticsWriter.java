package io.github.pmonnin.io;

import io.github.pmonnin.statistics.FormalContextStatistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Writes computed formal context statistics into a file
 * @author Pierre Monnin
 */
public class FormalContextStatisticsWriter {
    private PrintWriter writer;

    public void open(String fileName) {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName);
            this.writer = null;
        }
    }

    public void writeFormalContextStatistics(FormalContextStatistics statistics) {
        if (this.writer != null) {
            this.writer.println("--- Formal Context statistics ---");
            this.writer.println("Objects: " + statistics.getObjectsNumber());
            this.writer.println("Attributes: " + statistics.getAttributesNumber());
            this.writer.println("Attributes / object: " + statistics.getAttributesPerObject());
        }
    }

    public void close() {
        if (this.writer != null) {
            this.writer.close();
            this.writer = null;
        }
    }
}
