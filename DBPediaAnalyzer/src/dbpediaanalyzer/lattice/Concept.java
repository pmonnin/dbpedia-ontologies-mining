package dbpediaanalyzer.lattice;

import dbpediaanalyzer.dbpediaobject.Page;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class Concept {
    private ArrayList<Page> objects;
    private ArrayList<String> attributes;
    private ArrayList<Concept> parents;
    private ArrayList<Concept> children;

    public Concept(colibri.lib.Concept colibriConcept, HashMap<String, Page> dataSet) {
        for(Object o : colibriConcept.getObjects()) {
            this.objects.add(dataSet.get(o));
        }

        for(Object a : colibriConcept.getAttributes()) {
            this.attributes.add((String) a);
        }
    }

    void addParent(Concept parent) {
        if(!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
    }

    public ArrayList<Concept> getParents() {
        return new ArrayList<>(this.parents);
    }

    void addChild(Concept child) {
        if(!this.children.contains(child)) {
            this.children.add(child);
        }
    }

    public ArrayList<Concept> getChildren() {
        return new ArrayList<>(this.children);
    }
}
