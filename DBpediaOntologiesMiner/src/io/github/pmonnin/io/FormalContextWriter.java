package io.github.pmonnin.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import io.github.pmonnin.fca.FormalContext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Write computed formal context into a file
 * @author Pierre Monnin
 */
public class FormalContextWriter {
    private PrintWriter writer;

    public FormalContextWriter() {

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

    public void writeFormalContext(FormalContext fc) {
        if (this.writer != null) {
            try {
                JsonGenerator g = (new JsonFactory()).createGenerator(this.writer);

                g.writeStartArray();

                // Objects and attributes
                g.writeStartObject();
                g.writeArrayFieldStart("ObjNames");
                for (int i = 0 ; i < fc.getObjectsNumber() ; i++) {
                    g.writeString(fc.getObject(i));
                }
                g.writeEndArray();

                g.writeObjectFieldStart("Params");
                g.writeArrayFieldStart("AttrNames");
                for (int i = 0 ; i < fc.getAttributesNumber() ; i++) {
                    g.writeString(fc.getAttribute(i));
                }
                g.writeEndArray();
                g.writeEndObject();

                g.writeEndObject();

                // Incidence
                g.writeStartObject();
                g.writeNumberField("Count", fc.getObjectsNumber());

                g.writeArrayFieldStart("Data");
                for (int i = 0 ; i < fc.getObjectsNumber() ; i++) {
                    g.writeStartObject();

                    List<Integer> incidence = fc.getObjectIncidence(i);
                    g.writeNumberField("Count", incidence.size());

                    g.writeArrayFieldStart("Inds");
                    for (Integer j : incidence) {
                        g.writeNumber(j);
                    }
                    g.writeEndArray();

                    g.writeEndObject();
                }
                g.writeEndArray();

                g.writeEndObject();

                g.writeEndArray();
                g.close();
            }

            catch (IOException e) {
                System.err.println("[ERROR] IO error while writting the formal context: " + e.getMessage());
            }
        }
    }

    public void close() {
        if (this.writer != null) {
            this.writer.close();
            this.writer = null;
        }
    }
}
