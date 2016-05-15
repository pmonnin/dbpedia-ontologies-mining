package dbpediaanalyzer.statistic;

import dbpediaanalyzer.dbpediaobject.HierarchyElement;

import java.util.*;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class HierarchyStatistics {
    private int elementsNumber;
    private int orphansNumber;
    private int depth;
    private int depthInaccessibleElements;
    private int directSubsumptions;
    private int inferredSubsumptions;
    private List<List<HierarchyElement>> cycles;

    public HierarchyStatistics(Map<String, ? extends HierarchyElement> hierarchy) {
        this.elementsNumber= -1;
        this.orphansNumber = -1;
        this.depth = -1;
        this.depthInaccessibleElements = -1;
        this.directSubsumptions = -1;
        this.inferredSubsumptions = -1;
        this.cycles = new ArrayList<>();

        computeStatistics(hierarchy);
    }

    private void computeStatistics(Map<String, ? extends HierarchyElement> hierarchy) {
        this.elementsNumber = hierarchy.size();

        this.orphansNumber = 0;
        this.directSubsumptions = 0;
        ArrayList<HierarchyElement> orphans = new ArrayList<>();
        for(String key : hierarchy.keySet()) {
            if(hierarchy.get(key).getParents().size() == 0) {
                this.orphansNumber++;
                orphans.add(hierarchy.get(key));
            }

            this.directSubsumptions += hierarchy.get(key).getParents().size();
        }

        computeCycles(hierarchy);
        computeDepth(hierarchy, orphans);
        computeInferredSubsumptions(hierarchy);
    }

    private void computeDepth(Map<String, ? extends HierarchyElement> hierarchy, ArrayList<HierarchyElement> orphans) {
        HashMap<HierarchyElement, ArrayList<HierarchyElement>> elementsMaxDepthPath = new HashMap<>();
        Queue<ArrayList<HierarchyElement>> queue = new LinkedList<>();

        for(HierarchyElement element : hierarchy.values()) {
            elementsMaxDepthPath.put(element, new ArrayList<>());
        }

        for(HierarchyElement orphan : orphans) {
            ArrayList<HierarchyElement> path = new ArrayList<>();
            path.add(orphan);

            elementsMaxDepthPath.put(orphan, path);
            queue.add(path);
        }

        while(!queue.isEmpty()) {
            ArrayList<HierarchyElement> path = queue.poll();

            for(HierarchyElement child : path.get(path.size() - 1).getChildren()) {
                if(!path.contains(child) && path.size() + 1 > elementsMaxDepthPath.get(child).size()) {
                    ArrayList<HierarchyElement> childPath = new ArrayList<>(path);
                    childPath.add(child);
                    elementsMaxDepthPath.put(child, childPath);
                    queue.add(childPath);
                }
            }
        }

        this.depth = -1;
        this.depthInaccessibleElements = 0;
        for(ArrayList<HierarchyElement> path : elementsMaxDepthPath.values()) {
            if(path.size() == 0) {
                this.depthInaccessibleElements++;
            }

            else if(path.get(path.size() - 1).getChildren().isEmpty() && path.size() > this.depth) {
                this.depth = path.size();
            }
        }
    }

    private void computeInferredSubsumptions(Map<String, ? extends HierarchyElement> hierarchy) {
        this.inferredSubsumptions = 0;

        for(String key : hierarchy.keySet()) {
            HashMap<String, Boolean> seen = new HashMap<>();
            for(String elementKey : hierarchy.keySet()) {
                seen.put(elementKey, false);
            }

            Queue<HierarchyElement> queue = new LinkedList<>();
            seen.put(key, true);
            for(HierarchyElement he : hierarchy.get(key).getParents()) {
                seen.put(he.getUri(), true);
                queue.add(he);
            }

            while(!queue.isEmpty()) {
                HierarchyElement he = queue.poll();

                for(HierarchyElement parent : he.getParents()) {
                    if(!seen.get(parent.getUri())) {
                        this.inferredSubsumptions++;
                        queue.add(parent);
                        seen.put(parent.getUri(), true);
                    }
                }
            }
        }
    }

    private void computeCycles(Map<String, ? extends HierarchyElement> hierarchy) {
        for(String entryUri : hierarchy.keySet()) {
            HierarchyElement origin = hierarchy.get(entryUri);

            Map<HierarchyElement, HierarchyElement> predecessors = new HashMap<>();
            Queue<HierarchyElement> queue = new LinkedList<>();
            queue.add(origin);

            // Cycle detection with parent relationship
            while(!queue.isEmpty()) {
                HierarchyElement element = queue.poll();

                for(HierarchyElement parent : element.getParents()) {
                    if(parent == origin) {
                        // Cycle detected
                        List<HierarchyElement> cycle = new ArrayList<>();
                        cycle.add(origin);
                        HierarchyElement current = element;

                        do {
                            cycle.add(0, current);
                            current = predecessors.get(current);
                        } while(current != null);

                        cycles.add(cycle);
                    }

                    else {
                        if(!predecessors.containsKey(parent)) {
                            predecessors.put(parent, element);
                            queue.add(parent);
                        }
                    }
                }
            }
        }
    }

    public int getElementsNumber() {
        return this.elementsNumber;
    }

    public int getOrphansNumber() {
        return this.orphansNumber;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getDepthInaccessibleElements() {
        return this.depthInaccessibleElements;
    }

    public int getDirectSubsumptions() {
        return this.directSubsumptions;
    }

    public int getInferredSubsumptions() {
        return this.inferredSubsumptions;
    }

    public List<List<HierarchyElement>> getCycles() {
        List<List<HierarchyElement>> retVal = new ArrayList<>();

        for(List<HierarchyElement> cycle : this.cycles) {
            retVal.add(new ArrayList<>(cycle));
        }

        return retVal;
    }
}
