package dbpediaanalyzer.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import dbpediaanalyzer.lattice.Lattice;
import dbpediaanalyzer.statistic.LatticeStatistics;

import java.io.File;
import java.io.IOException;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class LatticeWriter {

    public void writeLattice(Lattice lattice, LatticeStatistics latticeStatistics, String fileName) {
        try {
            JsonGenerator jsonGenerator = (new JsonFactory()).createGenerator(new File(fileName), JsonEncoding.UTF8);

            jsonGenerator.writeStartObject();

            

            jsonGenerator.writeEndObject();
        }

        catch(IOException e) {
            System.err.println("Error while trying to save lattice inside file " + fileName + ". ");
        }
    }

}
