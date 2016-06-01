package dbpediaanalyzer.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public abstract class AbstractStatisticsWriter {
    private PrintWriter writer;
    private String fileName;

    public AbstractStatisticsWriter(String fileName, String statisticsType) {
        this.fileName = fileName;

        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }

        catch(IOException e) {
            System.err.println("Error while trying to open file " + fileName + " to save " + statisticsType + " statistics");
        }
    }

    protected void println(String str) {
        this.writer.println(str);
    }

    protected String getFileName() {
        return this.fileName;
    }

    public void close() {
        this.writer.close();
    }
}
