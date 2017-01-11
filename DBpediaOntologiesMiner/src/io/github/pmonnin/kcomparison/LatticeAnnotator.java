package io.github.pmonnin.kcomparison;

import io.github.pmonnin.fca.FormalConcept;
import io.github.pmonnin.fca.FormalLattice;
import io.github.pmonnin.io.ServerQuerier;
import io.github.pmonnin.io.SparqlRecord;
import io.github.pmonnin.io.SparqlResponse;
import io.github.pmonnin.io.SparqlValue;
import io.github.pmonnin.settings.OntologySettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Annotate the lattice with classes from an ontology
 * @author Pierre Monnin
 */
public class LatticeAnnotator {
    public void annotateLattice(FormalLattice lattice, OntologySettings ontologySettings) {
        // First traversal: annotations
        for (int i = 0 ; i < lattice.getConceptsNumber() ; i++) {
            FormalConcept c = lattice.getConcept(i);

            if (c.getExtentSize() != 0) {
                ArrayList<String> commonClasses = new ArrayList<>(getClasses(lattice.getObject(c.getExtentObject(0)),
                        ontologySettings));

                for (int j = 1 ; j < c.getExtentSize() ; j++) {
                    List<String> currentClasses = getClasses(lattice.getObject(c.getExtentObject(j)), ontologySettings);
                    commonClasses.retainAll(currentClasses);
                }

                for (String annotationClass : commonClasses) {
                    c.addAnnotationObject("annotation", lattice.getAnnotationObjectIndex(annotationClass));
                }
            }

            else {
                c.addAnnotation("annotation");
            }
        }

        // Second traversal: proper annotations
        for (int i = 0 ; i < lattice.getConceptsNumber() ; i++) {
            FormalConcept c = lattice.getConcept(i);

            ArrayList<Integer> properAnnotation = new ArrayList<>(c.getAnnotation("annotation"));

            for (FormalConcept parent : c.getParents()) {
                properAnnotation.removeAll(parent.getAnnotation("annotation"));
            }

            c.addAnnotation("proper-annotation");
            for (Integer properClass : properAnnotation) {
                c.addAnnotationObject("proper-annotation", properClass);
            }
        }
    }

    private List<String> getClasses(String object, OntologySettings ontologySettings) {
        List<String> list = new ArrayList<>();

        try {
            SparqlResponse classes = (new ServerQuerier()).runQuery(ontologySettings.getTypePrefixes() +
                    " select distinct ?class where {" +
                    "<" + object + "> " + ontologySettings.getTypePredicate() +
                    "/" + ontologySettings.getParentsPredicate() + "* ?class . " +
                    "FILTER (REGEX(STR(?class), \"" + ontologySettings.getUriPrefix() + "\", \"i\")) ." +
                    "}");

            for (SparqlRecord r : classes.getRecords()) {
                SparqlValue value = r.getFields().get("class");

                if (value != null)
                    list.add(value.getValue());
            }
        }

        catch (IOException e) {
            System.err.println("[ERROR] Error while annotating the lattice and getting classes for " + object
                    + " (" + e.getMessage() + ")");
        }

        return list;
    }

}
