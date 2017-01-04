package dbpediaanalyzer.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import dbpediaanalyzer.dbpediaobject.Category;
import dbpediaanalyzer.dbpediaobject.HierarchiesManager;
import dbpediaanalyzer.dbpediaobject.OntologyClass;
import dbpediaanalyzer.dbpediaobject.YagoClass;
import dbpediaanalyzer.lattice.Concept;
import dbpediaanalyzer.lattice.Lattice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads an annotated lattice from a file
 *
 * @author Pierre Monnin
 *
 */
public class LatticeReader {

    public Lattice readLattice(String fileName, HierarchiesManager hm) {
        Lattice lattice;

        try {
            JsonParser jsonParser = (new JsonFactory()).createParser(new File(fileName));
            ArrayList<Concept> concepts = new ArrayList<>();

            // Json global object start
            if(jsonParser.nextToken() == JsonToken.START_OBJECT) {
                if(jsonParser.nextToken() == JsonToken.FIELD_NAME && jsonParser.getCurrentName().equals("concepts") && jsonParser.nextToken() == JsonToken.START_ARRAY) {
                    while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        concepts.add(parseConcept(jsonParser, hm));
                    }
                }

                else {
                    throw new IOException("JSON array field concepts not found in file");
                }

                if(jsonParser.nextToken() == JsonToken.FIELD_NAME && jsonParser.getCurrentName().equals("edges") && jsonParser.nextToken() == JsonToken.START_ARRAY) {
                    while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        parseEdge(jsonParser, concepts);
                    }
                }

                else {
                    throw new IOException("JSON array field edges not found in file");
                }
            }

            else {
                throw new IOException("Global JSON object not found in file");
            }

            if(jsonParser.nextToken() != JsonToken.END_OBJECT) {
                throw new IOException("Global JSON object end not found");
            }

            jsonParser.close();

            lattice = new Lattice(concepts);
        }

        catch(IOException e) {
            System.err.println("Error while trying to load lattice from file " + fileName + ". A null lattice will " +
                    "be returned. (" + e.getMessage() + ")");
            lattice = null;
        }

        return lattice;
    }

    private Concept parseConcept(JsonParser jsonParser, HierarchiesManager hm) throws IOException {
        if(jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IOException("JSON object describing a concept not found");
        }

        // Objects
        ArrayList<String> objects = new ArrayList<>();
        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && jsonParser.getCurrentName().equals("objects") && jsonParser.nextToken() == JsonToken.START_ARRAY) {
            while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                if(jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                    objects.add(jsonParser.getValueAsString());
                }

                else {
                    throw new IOException("Unexpected value inside JSON array field objects of a concept");
                }
            }
        }

        else {
            throw new IOException("JSON array field objects not found inside a concept");
        }

        // Attributes
        ArrayList<String> attributes = new ArrayList<>();
        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && jsonParser.getCurrentName().equals("attributes") && jsonParser.nextToken() == JsonToken.START_ARRAY) {
            while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                if(jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                    attributes.add(jsonParser.getValueAsString());
                }

                else {
                    throw new IOException("Unexpected value inside JSON array field attributes of a concept");
                }
            }
        }

        else {
            throw new IOException("JSON array field attributes not found inside a concept");
        }

        // Categories
        ArrayList<Category> categories = new ArrayList<>();
        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && jsonParser.getCurrentName().equals("categories") && jsonParser.nextToken() == JsonToken.START_ARRAY) {
            while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                if(jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                    categories.add(hm.getCategoryFromUri(jsonParser.getValueAsString()));
                }

                else {
                    throw new IOException("Unexpected value inside JSON array field categories of a concept");
                }
            }
        }

        else {
            throw new IOException("JSON array field categories not found inside a concept");
        }

        // Ontology classes
        ArrayList<OntologyClass> ontologyClasses = new ArrayList<>();
        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && jsonParser.getCurrentName().equals("ontologyClasses") && jsonParser.nextToken() == JsonToken.START_ARRAY) {
            while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                if(jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                    ontologyClasses.add(hm.getOntologyClassFromUri(jsonParser.getValueAsString()));
                }

                else {
                    throw new IOException("Unexpected value inside JSON array field ontologyClasses of a concept");
                }
            }
        }

        else {
            throw new IOException("JSON array field ontologyClasses not found inside a concept");
        }

        // Yago classes
        ArrayList<YagoClass> yagoClasses = new ArrayList<>();
        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && jsonParser.getCurrentName().equals("yagoClasses") && jsonParser.nextToken() == JsonToken.START_ARRAY) {
            while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                if(jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                    yagoClasses.add(hm.getYagoClassFromUri(jsonParser.getValueAsString()));
                }

                else {
                    throw new IOException("Unexpected value inside JSON array field yagoClasses of a concept");
                }
            }
        }

        else {
            throw new IOException("JSON array field yagoClasses not found inside a concept");
        }

        if(jsonParser.nextToken() != JsonToken.END_OBJECT) {
            throw new IOException("Extraneous JSON data after yagoClasses field for a concept");
        }

        return new Concept(objects, attributes, categories, ontologyClasses, yagoClasses);
    }

    private void parseEdge(JsonParser jsonParser, ArrayList<Concept> concepts) throws IOException {
        if(jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IOException("JSON object describing an edge not found");
        }

        if(jsonParser.nextToken() != JsonToken.FIELD_NAME || !jsonParser.getCurrentName().equals("top")) {
            throw new IOException("JSON field top inside edge not found");
        }

        if(jsonParser.nextToken() != JsonToken.VALUE_NUMBER_INT) {
            throw new IOException("JSON field top inside edge not an integer");
        }

        int edgeTopConceptIndex = jsonParser.getIntValue();

        if(edgeTopConceptIndex < 0 || edgeTopConceptIndex >= concepts.size()) {
            throw new IOException("JSON field top inside edge is an invalid integer");
        }

        if(jsonParser.nextToken() != JsonToken.FIELD_NAME || !jsonParser.getCurrentName().equals("bottom")) {
            throw new IOException("JSON field bottom inside edge not found");
        }

        if(jsonParser.nextToken() != JsonToken.VALUE_NUMBER_INT) {
            throw new IOException("JSON field bottom inside edge not an integer");
        }

        int edgeBottomConceptIndex = jsonParser.getIntValue();

        if(edgeBottomConceptIndex < 0 || edgeBottomConceptIndex >= concepts.size()) {
            throw new IOException("JSON field bottom inside edge is an invalid integer");
        }

        if(jsonParser.nextToken() != JsonToken.END_OBJECT) {
            throw new IOException("Extraneous JSON data after bottom field inside edge object");
        }

        concepts.get(edgeTopConceptIndex).addChild(concepts.get(edgeBottomConceptIndex));
        concepts.get(edgeBottomConceptIndex).addParent(concepts.get(edgeTopConceptIndex));
    }
}
