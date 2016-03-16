package dbpediaanalyzer.lattice;

import colibri.lib.Edge;
import colibri.lib.Traversal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class Lattice {
    private ArrayList<Concept> concepts;
    private Concept top;
    private Concept bottom;

    public Lattice(colibri.lib.Lattice colibriLattice) {
        this.concepts = new ArrayList<>();

        // Traversing colibri lattice and creating custom lattice
        Iterator<Edge> latticeIterator = colibriLattice.edgeIterator(Traversal.BOTTOM_ATTRSIZE);
        HashMap<colibri.lib.Concept, Concept> processed = new HashMap<>();
        while(latticeIterator.hasNext()) {
            Edge edge = latticeIterator.next();

            colibri.lib.Concept colibriUpperConcept = edge.getUpper();
            colibri.lib.Concept colibriLowerConcept = edge.getLower();

            if(!processed.containsKey(colibriUpperConcept)) {
                Concept c = createConceptFromColibriConcept(colibriUpperConcept);
                processed.put(colibriUpperConcept, c);
                this.concepts.add(c);
            }

            if(!processed.containsKey(colibriLowerConcept)) {
                Concept c = createConceptFromColibriConcept(colibriLowerConcept);
                processed.put(colibriLowerConcept, c);
                this.concepts.add(c);
            }

            Concept upperConcept = processed.get(colibriUpperConcept);
            Concept lowerConcept = processed.get(colibriLowerConcept);

            upperConcept.addChild(lowerConcept);
            lowerConcept.addParent(upperConcept);
        }

        // Top & bottom detection
        for(Concept c : this.concepts) {
            if(c.getParents().size() == 0) {
                this.top = c;
            }

            if(c.getChildren().size() == 0) {
                this.bottom = c;
            }
        }
    }

    private Concept createConceptFromColibriConcept(colibri.lib.Concept colibriConcept) {
        Concept concept = new Concept();

        // TODO under development

        return concept;
    }

    public Concept getTop() {
        return this.top;
    }

    public Concept getBottom() {
        return this.bottom;
    }
}
