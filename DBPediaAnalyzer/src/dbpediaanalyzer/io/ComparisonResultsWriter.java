package dbpediaanalyzer.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import dbpediaanalyzer.comparison.ComparisonResult;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class ComparisonResultsWriter {

    public void writeComparisonResults(List<ComparisonResult> comparisonResults, String fileName) {
        try {
            JsonGenerator jsonGenerator = (new JsonFactory()).createGenerator(new File(fileName), JsonEncoding.UTF8);
            jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter()); // Only for debug purposes

            // Global array begin
            jsonGenerator.writeStartArray();

            for (ComparisonResult result : comparisonResults) {
                jsonGenerator.writeStartObject();

                jsonGenerator.writeStringField("type", result.getType().toString());
                jsonGenerator.writeStringField("bottom", result.getBottom().getUri());
                jsonGenerator.writeStringField("top", result.getTop().getUri());

                jsonGenerator.writeObjectFieldStart("values");
                for(Map.Entry<String, Double> entry : result.getValues().entrySet()) {
                    jsonGenerator.writeNumberField(entry.getKey(), entry.getValue());
                }
                jsonGenerator.writeEndObject();

                jsonGenerator.writeEndObject();
            }

            // Global array end
            jsonGenerator.writeEndArray();

            jsonGenerator.close();
        }

        catch(IOException e) {
            System.err.println("Error while trying to save comparison results inside file " + fileName + ".");
        }
    }
}
