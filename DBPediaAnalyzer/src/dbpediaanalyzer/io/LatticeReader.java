package dbpediaanalyzer.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import dbpediaanalyzer.lattice.Lattice;

import java.io.File;
import java.io.IOException;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class LatticeReader {

    public Lattice readLattice(String fileName) {
        Lattice lattice = null;

        try {
            JsonParser jsonParser = (new JsonFactory()).createParser(new File(fileName));
        }

        catch(IOException e) {
            System.err.println("Error while trying to load lattice from file " + fileName + ". ");
        }

        return lattice;
    }
}
