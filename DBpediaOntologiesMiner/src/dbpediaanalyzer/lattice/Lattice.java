package dbpediaanalyzer.lattice;

import java.util.ArrayList;
import java.util.List;

/**
 * Annotated lattice
 *
 * @author Pierre Monnin
 *
 */
public class Lattice {
    private List<Concept> concepts;
    private Concept top;
    private Concept bottom;

    public Lattice(List<Concept> concepts) {
        this.concepts = new ArrayList<>(concepts);
        topBottomInit();
    }

    private void topBottomInit() {
        for(Concept c : this.concepts) {
            if(c.getParents().size() == 0) {
                this.top = c;
            }

            if(c.getChildren().size() == 0) {
                this.bottom = c;
            }
        }
    }

    public Concept getTop() {
        return this.top;
    }

    public Concept getBottom() {
        return this.bottom;
    }

    public List<Concept> getConcepts() {
        return new ArrayList<>(this.concepts);
    }
}
