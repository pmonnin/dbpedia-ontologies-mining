package io.github.pmonnin.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import io.github.pmonnin.fca.FormalConcept;
import io.github.pmonnin.fca.FormalLattice;

import java.io.File;
import java.io.IOException;

/**
 * Read and load a lattice output from SOFIA
 * @author Pierre Monnin
 */
public class FormalLatticeReader {
    public FormalLatticeReader() {

    }

    /**
     * Read a lattice from a SOFIA output file
     * Warning: this method supposes that the file is correctly written (no error check)
     * Maybe to refactor
     * @param filePath the JSON file output by SOFIA
     * @return the equivalent formal lattice
     */
    public FormalLattice readLattice(String filePath) {
        FormalLattice lattice = null;

        try {
            JsonParser parser = (new JsonFactory()).createParser(new File(filePath));

            parser.nextToken(); // Begin array
            parser.nextToken(); // Begin object (metadata)

            parser.nextToken(); // NodesCount field name
            parser.nextToken(); // NodesCount value
            int nodesCount = parser.getIntValue();

            parser.nextToken(); // ArcsCount field name
            parser.nextToken(); // ArcsCount value
            int arcsCount = parser.getIntValue();

            parser.nextToken(); // Bottom field name
            parser.nextToken(); // Bottom array start
            parser.nextToken(); // Bottom index
            int bottomIndex = parser.getIntValue();
            parser.nextToken(); // Bottom array stop

            parser.nextToken(); // Top field name
            parser.nextToken(); // Top array start
            parser.nextToken(); // Top index
            int topIndex = parser.getIntValue();
            parser.nextToken(); // Top array end

            parser.nextToken(); // End object (metadata)

            lattice = new FormalLattice(nodesCount, topIndex, bottomIndex);

            // Nodes parsing
            parser.nextToken(); // Begin object (nodes)
            parser.nextToken(); // Nodes field name
            parser.nextToken(); // Begin array (nodes)
            for (int i = 0 ; i < nodesCount ; i++) {
                FormalConcept concept = new FormalConcept();

                parser.nextToken(); // Begin object (node)

                parser.nextToken(); // Ext field name
                parser.nextToken(); // Begin object (Ext)
                parser.nextToken(); // Count field name
                parser.nextToken(); // Count of Extent objects
                int numberOfObjects = parser.getIntValue();
                int[] objects = new int[numberOfObjects];
                parser.nextToken(); // Inds field name
                parser.nextToken(); // Array begin (Inds)
                for (int j = 0 ; j < numberOfObjects ; j++) {
                    parser.nextToken(); // one index
                    objects[j] = parser.getIntValue();
                    concept.addExtendObject(objects[j]);
                }
                parser.nextToken(); // Array end (Inds)

                if (numberOfObjects != 0) {
                    parser.nextToken(); // Names field name
                    parser.nextToken(); // Array start (Names)

                    for (int j = 0 ; j < numberOfObjects ; j++) {
                        parser.nextToken(); // one name
                        lattice.addObject(objects[j], parser.getText());
                    }

                    parser.nextToken(); // Array end (Names)
                }

                parser.nextToken(); // Object end (Ext)

                parser.nextToken();
                while (!parser.getText().equals("Int"))
                    parser.nextToken();


                if (i == bottomIndex) {
                    parser.nextToken(); // "BOTTOM"
                }

                else {
                    parser.nextToken(); // Begin object (Int)
                    parser.nextToken(); // Count field name
                    parser.nextToken(); // Count of Intent attributes
                    int numberOfAttributes = parser.getIntValue();
                    int[] attributes = new int[numberOfAttributes];
                    parser.nextToken(); // Inds field name
                    parser.nextToken(); // Begin array (Inds)
                    for (int j = 0 ; j < numberOfAttributes ; j++) {
                        parser.nextToken(); // one index
                        attributes[j] = parser.getIntValue();
                        concept.addIntentAttribute(attributes[j]);
                    }
                    parser.nextToken(); // End array (Inds)

                    parser.nextToken(); // Names field name
                    parser.nextToken(); // Begin array (names)

                    for (int j = 0 ; j < numberOfAttributes ; j++) {
                        parser.nextToken(); // one name
                        lattice.addAttribute(attributes[j], parser.getText());
                    }

                    parser.nextToken(); // End array (names)
                    parser.nextToken(); // End object (Int)
                }

                parser.nextToken(); // End array (node)

                lattice.addConcept(i, concept);
            }
            parser.nextToken(); // End array (nodes)
            parser.nextToken(); // End object (nodes)

            // Arcs parsing
            parser.nextToken(); // Begin object (arcs)
            parser.nextToken(); // Arcs field name
            parser.nextToken(); // Begin array (arcs)
            for (int i = 0 ; i < arcsCount ; i++) {
                parser.nextToken(); // Begin object (arc)
                parser.nextToken(); // S field name
                parser.nextToken(); // S index
                int supIndex = parser.getIntValue();

                parser.nextToken(); // D fieldName
                parser.nextToken(); // D index
                int subIndex = parser.getIntValue();

                parser.nextToken(); // End object (arc)

                lattice.addArc(subIndex, supIndex);
            }

            // Completion of bottom intent
            FormalConcept bottom = lattice.getBottom();
            for (int i = 0 ; i < lattice.getAttributesNumber() ; i++) {
                bottom.addIntentAttribute(i);
            }

            parser.close();
        }

        catch (IOException e) {
            System.err.println("[ERROR] IO error while reading the formal lattice: " + e.getMessage() + ". An empty" +
                    "lattice will be used");
            return null;
        }

        return lattice;
    }
}
