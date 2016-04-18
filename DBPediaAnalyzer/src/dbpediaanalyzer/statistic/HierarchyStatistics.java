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
    private int directSubsumptions;
    private int inferredSubsumptions;

    public HierarchyStatistics(Map<String, ? extends HierarchyElement> hierarchy) {
        this.elementsNumber= -1;
        this.orphansNumber = -1;
        this.depth = -1;
        this.directSubsumptions = -1;
        this.inferredSubsumptions = -1;

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

        computeDepth(hierarchy, orphans);
        computeInferredSubsumptions(hierarchy);
    }

    private void computeDepth(Map<String, ? extends HierarchyElement> hierarchy, ArrayList<HierarchyElement> orphans) {
        HashMap<String, Integer> elementsDepth = new HashMap<>();
        Queue<HierarchyElement> queue = new LinkedList<>();

        for(String key : hierarchy.keySet()) {
            elementsDepth.put(key, -1);
        }

        for(HierarchyElement he : orphans) {
            elementsDepth.put(he.getUri(), 1);
            queue.add(he);
        }
        
        this.depth = -1;
        while(!queue.isEmpty()) {
            HierarchyElement he = queue.poll();
            int currentDepth = elementsDepth.get(he.getUri());

            if(currentDepth > this.depth) {
                this.depth = currentDepth;
            }

            for(HierarchyElement child : he.getChildren()) {
                if(elementsDepth.get(child.getUri()) == -1) {
                    elementsDepth.put(child.getUri(), currentDepth + 1);
                    queue.add(child);
                }
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

    public int getElementsNumber() {
        return this.elementsNumber;
    }

    public int getOrphansNumber() {
        return this.orphansNumber;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getDirectSubsumptions() {
        return this.directSubsumptions;
    }

    public int getInferredSubsumptions() {
        return this.inferredSubsumptions;
    }
}
