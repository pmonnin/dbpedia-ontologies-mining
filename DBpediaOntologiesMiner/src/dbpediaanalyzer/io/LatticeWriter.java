package dbpediaanalyzer.io;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import dbpediaanalyzer.dbpediaobject.Category;
import dbpediaanalyzer.dbpediaobject.OntologyClass;
import dbpediaanalyzer.dbpediaobject.YagoClass;
import dbpediaanalyzer.lattice.Concept;
import dbpediaanalyzer.lattice.Lattice;

import java.io.File;
import java.io.IOException;

/**
 * Saves the annotated lattice to a file
 *
 * @author Pierre Monnin
 *
 */
public class LatticeWriter {

    public void writeLattice(Lattice lattice, String fileName) {
        try {
            JsonGenerator jsonGenerator = (new JsonFactory()).createGenerator(new File(fileName), JsonEncoding.UTF8);
            jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter()); // Only for debug purposes

            // Start of JSON global object
            jsonGenerator.writeStartObject();

            // Concepts saving
            jsonGenerator.writeArrayFieldStart("concepts");
            for(Concept concept : lattice.getConcepts()) {
                // Start of concept JSON object
                jsonGenerator.writeStartObject();

                // Objects saving
                jsonGenerator.writeArrayFieldStart("objects");
                for(String page : concept.getObjects()) {
                    jsonGenerator.writeString(page);
                }
                jsonGenerator.writeEndArray();

                // Attributes saving
                jsonGenerator.writeArrayFieldStart("attributes");
                for(String attribute : concept.getAttributes()) {
                    jsonGenerator.writeString(attribute);
                }
                jsonGenerator.writeEndArray();

                // Categories saving
                jsonGenerator.writeArrayFieldStart("categories");
                for(Category category : concept.getCategories()) {
                    jsonGenerator.writeString(category.getUri());
                }
                jsonGenerator.writeEndArray();

                // Ontology classes saving
                jsonGenerator.writeArrayFieldStart("ontologyClasses");
                for(OntologyClass ontology : concept.getOntologyClasses()) {
                    jsonGenerator.writeString(ontology.getUri());
                }
                jsonGenerator.writeEndArray();

                // Yago classes saving
                jsonGenerator.writeArrayFieldStart("yagoClasses");
                for(YagoClass yagoClass : concept.getYagoClasses()) {
                    jsonGenerator.writeString(yagoClass.getUri());
                }
                jsonGenerator.writeEndArray();

                // End of concept object
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();

            // Edges saving
            jsonGenerator.writeArrayFieldStart("edges");
            for(int i = 0 ; i < lattice.getConcepts().size() ; i++) {
                for(Concept child : lattice.getConcepts().get(i).getChildren()) {
                    jsonGenerator.writeStartObject();

                    jsonGenerator.writeNumberField("top", i);
                    jsonGenerator.writeNumberField("bottom", lattice.getConcepts().indexOf(child));

                    jsonGenerator.writeEndObject();
                }
            }
            jsonGenerator.writeEndArray();

            // End of JSON global object
            jsonGenerator.writeEndObject();

            jsonGenerator.close();
        }

        catch(IOException e) {
            System.err.println("Error while trying to save lattice inside file " + fileName + ". ");
        }
    }

}
