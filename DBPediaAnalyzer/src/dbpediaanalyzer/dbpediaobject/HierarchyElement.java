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

    public Collection<HierarchyElement> getParents() {
        return new ArrayList<>(this.parents);
    }

    public Collection<HierarchyElement> getChildren() {
        return new ArrayList<>(this.children);
    }

    public Collection<HierarchyElement> getAncestors() {
        return HierarchyElement.getAccessibleUpwardElements(this.parents);
    }

    public int getDistanceFromAncestor(HierarchyElement ancestor) {
        if(this.parents.contains(ancestor)) {
            return 1;
        }

        HashMap<HierarchyElement, Integer> distances = new HashMap<>();
        Queue<HierarchyElement> queue = new LinkedList<>();

        for(HierarchyElement parent : this.parents) {
            distances.put(parent, 1);
            queue.add(parent);
        }

        while(!queue.isEmpty()) {
            HierarchyElement element = queue.poll();

            for(HierarchyElement parent : element.getParents()) {
                if(parent == ancestor) {
                    return distances.get(element) + 1;
                }

                if(!distances.containsKey(parent)) {
                    distances.put(parent, distances.get(element) + 1);
                    queue.add(parent);
                }
            }
        }

        return -1;
    }

    protected static Collection<HierarchyElement> getAccessibleUpwardElements(Collection<? extends HierarchyElement> fromSubset) {
        HashMap<String, HierarchyElement> accessible = new HashMap<>();
        Queue<HierarchyElement> queue = new LinkedList<>();

        for(HierarchyElement he : fromSubset) {
            queue.add(he);
            accessible.put(he.getUri(), he);
        }

        while(!queue.isEmpty()) {
            HierarchyElement he = queue.poll();

            for(HierarchyElement parent : he.getParents()) {
                if(!accessible.containsKey(parent.getUri())) {
                    accessible.put(parent.getUri(), parent);
                    queue.add(parent);
                }
            }
        }

        return accessible.values();
    }
}
