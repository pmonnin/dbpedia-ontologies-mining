package io.github.pmonnin.factories;

import io.github.pmonnin.io.SparqlRecord;
import io.github.pmonnin.io.SparqlResponse;
import io.github.pmonnin.fca.FormalContext;
import io.github.pmonnin.io.ServerQuerier;
import io.github.pmonnin.io.SparqlValue;
import io.github.pmonnin.settings.ContextSettings;

import java.io.IOException;

/**
 * Create a formal context based on the given settings
 * @author Pierre Monnin
 */
public class FormalContextFactory {
    public FormalContextFactory() {

    }

    public FormalContext buildFormalContext(ContextSettings settings) {
        FormalContext fc = new FormalContext();

        try {
            SparqlResponse rdfObjects = (new ServerQuerier()).runQuery(settings.getPrefixes() +
                    "select distinct ?object where {" + settings.getWhereConditions() + "}");

            for (SparqlRecord r : rdfObjects.getRecords()) {
                SparqlValue object = r.getFields().get("object");

                if (object != null) {
                    SparqlResponse rdfPredicates = (new ServerQuerier()).runQuery("select distinct ?p where { " +
                            "<" + object.getValue() + "> ?p ?o . }");

                    for (SparqlRecord r2 : rdfPredicates.getRecords()) {
                        SparqlValue predicate = r2.getFields().get("p");

                        if (predicate != null) {
                            fc.add(object.getValue(), predicate.getValue());
                        }
                    }
                }
            }
        }

        catch (IOException e) {
            System.err.println("[ERROR] Error while trying to query context from endpoint (" + e.getMessage() + "). " +
                    "As a consequence, an empty data set will be used.");
            fc = new FormalContext();
        }

        return fc;
    }
}
