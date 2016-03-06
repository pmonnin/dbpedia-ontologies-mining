package dbpediaanalyzer.dbpediaobjects;

import java.util.ArrayList;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public abstract class HierarchyElement {
    private String uri;
    private ArrayList<HierarchyElement> parents;
    private ArrayList<HierarchyElement> children;

    public HierarchyElement(String uri) {
        this.uri = uri;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public void addParent(HierarchyElement parent) {
        if(!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
    }

    public void addChild(HierarchyElement child) {
        if(!this.children.contains(child)) {
            this.children.add(child);
        }
    }

    public String getUri() {
        return this.uri;
    }

    public ArrayList<HierarchyElement> getParents() {
        return new ArrayList<>(this.parents);
    }

    public ArrayList<HierarchyElement> getChildren() {
        return new ArrayList<>(this.children);
    }
}
