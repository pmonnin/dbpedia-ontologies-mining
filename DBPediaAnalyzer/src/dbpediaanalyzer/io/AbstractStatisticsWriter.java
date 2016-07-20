package dbpediaanalyzer.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Abstract writer for statistics providing basic methods as well as the handling of the print writer object
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

    protected void print(String str) {
        this.writer.print(str);
    }

    protected String getFileName() {
        return this.fileName;
    }

    public void close() {
        this.writer.close();
    }
}
