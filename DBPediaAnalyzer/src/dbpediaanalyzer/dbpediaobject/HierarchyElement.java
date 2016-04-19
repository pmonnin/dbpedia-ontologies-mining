package dbpediaanalyzer.dbpediaobject;

import java.util.*;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public abstract class HierarchyElement {
    private String uri;
    private List<HierarchyElement> parents;
    private List<HierarchyElement> children;
    private Map<HierarchyElement, Integer> ancestorsAndDistances;

    public HierarchyElement(String uri) {
        this.uri = uri;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.ancestorsAndDistances = null;
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

    public List<HierarchyElement> getParents() {
        return new ArrayList<>(this.parents);
    }

    public List<HierarchyElement> getChildren() {
        return new ArrayList<>(this.children);
    }

    public Map<HierarchyElement, Integer> getAncestorsAndDistances() {
        if(this.ancestorsAndDistances == null) {
            computeAncestorsAndDistances();
        }

        return new HashMap<>(this.ancestorsAndDistances);
    }

    public boolean hasAncestor(HierarchyElement element) {
        if(this.ancestorsAndDistances == null) {
            computeAncestorsAndDistances();
        }

        return this.ancestorsAndDistances.containsKey(element);
    }

    private void computeAncestorsAndDistances() {
        this.ancestorsAndDistances = new HashMap<>();

        Queue<HierarchyElement> queue = new LinkedList<>();
        for(HierarchyElement parent : this.parents) {
            this.ancestorsAndDistances.put(parent, 1);
            queue.add(parent);
        }

        while(!queue.isEmpty()) {
            HierarchyElement ancestor = queue.poll();

            for(HierarchyElement parent : ancestor.getParents()) {
                if(!this.ancestorsAndDistances.containsKey(parent) || this.ancestorsAndDistances.get(ancestor) + 1 < this.ancestorsAndDistances.get(parent)) {
                    this.ancestorsAndDistances.put(parent, this.ancestorsAndDistances.get(ancestor) + 1);
                    queue.add(parent);
                }
            }
        }
    }

    protected static List<HierarchyElement> getAccessibleUpwardElements(Collection<? extends HierarchyElement> fromSubset) {
        HashMap<HierarchyElement, Integer> accessibleElements = new HashMap<>();

        for(HierarchyElement element : fromSubset) {
            accessibleElements.put(element, 0);
            accessibleElements.putAll(element.getAncestorsAndDistances());
        }

        return new ArrayList<>(accessibleElements.keySet());
    }
}
