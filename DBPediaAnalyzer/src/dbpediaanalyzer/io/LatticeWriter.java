package dbpediaanalyzer.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import dbpediaanalyzer.dbpediaobject.Page;
import dbpediaanalyzer.lattice.Concept;
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

            // Start of JSON global object
            jsonGenerator.writeStartObject();

            // Concepts saving
            jsonGenerator.writeArrayFieldStart("concepts");
            for(Concept concept : lattice.getConcepts()) {
                // Start of concept JSON object
                jsonGenerator.writeStartObject();

                // Objects saving
                jsonGenerator.writeArrayFieldStart("objects");
                for(Page page : concept.getObjects()) {
                    jsonGenerator.writeString(page.getURI());
                }
                jsonGenerator.writeEndArray();

                // Attributes saving
                jsonGenerator.writeArrayFieldStart("attributes");
                for(String attribute : concept.getAttributes()) {
                    jsonGenerator.writeString(attribute);
                }
                jsonGenerator.writeEndArray();

                // End of concept object
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();

            // Edges saving
            jsonGenerator.writeArrayFieldStart("edges");
            jsonGenerator.writeEndArray();

            // End of global JSON object
            jsonGenerator.writeEndObject();

            jsonGenerator.close();
        }

        catch(IOException e) {
            System.err.println("Error while trying to save lattice inside file " + fileName + ". ");
        }
    }

}
