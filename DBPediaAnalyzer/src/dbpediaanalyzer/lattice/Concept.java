package dbpediaanalyzer.lattice;

import java.util.ArrayList;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class Concept {
    private ArrayList<Concept> parents;
    private ArrayList<Concept> children;

    public Concept() {
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public ArrayList<Concept> getParents() {
        return new ArrayList<>(this.parents);
    }

    public ArrayList<Concept> getChildren() {
        return new ArrayList<>(this.children);
    }
}
