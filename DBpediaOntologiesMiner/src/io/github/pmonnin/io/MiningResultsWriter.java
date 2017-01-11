package io.github.pmonnin.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pmonnin.kcomparison.Subsumption;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Write results of mining annotated lattice w.r.t an ontology
 * @author Pierre Monnin
 */
public class MiningResultsWriter {

    public void writeMiningResults(List<Subsumption> results, String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File(filePath), results);
        }

        catch (IOException e) {
            System.err.println("[ERROR] Error while writing the mining results (" + e.getMessage() + ")");
        }
    }

}
