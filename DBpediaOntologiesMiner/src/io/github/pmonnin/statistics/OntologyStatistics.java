package io.github.pmonnin.statistics;

import io.github.pmonnin.semanticwebobjects.OntologyClass;

import java.util.*;

/**
 * Compute and store statistics about the ontology given to the constructor
 * @author Pierre Monnin
 */
public class OntologyStatistics {
    /**
     * Represent a path in the ontology hierarchy
     * @author Pierre Monnin
     */
    private class OntologyPath {
        private Map<OntologyClass, OntologyClass> predecessors;
        private OntologyClass last;
        private OntologyClass root;

        OntologyPath(OntologyClass root) {
            predecessors = new HashMap<>();
            last = root;
            this.root = root;
        }

        OntologyPath(OntologyPath p) {
            predecessors = new HashMap<>(p.predecessors);
            last = p.last;
            root = p.root;
        }

        void add(OntologyClass c) {
            predecessors.put(c, last);
            last = c;
        }

        OntologyClass getLast() {
            return last;
        }

        boolean contains(OntologyClass c) {
            return c == root || predecessors.containsKey(c);
        }

        List<OntologyClass> getPath() {
            ArrayList<OntologyClass> path = new ArrayList<>();

            OntologyClass temp = last;
            while (temp != null) {
                path.add(0, temp);
                temp = predecessors.get(temp);
            }

            return path;
        }
    }

    private long elementsNumber;
    private long topLevelClassesNumber;
    private long depth;
    private long directSubsumptionsNumber;
    private long inferredSubsumptionsNumber;
    private List<List<OntologyClass>> cycles;

    public OntologyStatistics(Map<String, OntologyClass> ontology) {
        this.elementsNumber = ontology.size();

        this.topLevelClassesNumber = 0;
        List<OntologyClass> topLevelClasses = new ArrayList<>();
        for (OntologyClass ontologyClass : ontology.values()) {
            if (!ontologyClass.hasParents()) {
                this.topLevelClassesNumber++;
                topLevelClasses.add(ontologyClass);
            }
        }

        computeCycles(ontology);
        computeDepth(ontology, topLevelClasses);
        computeDirectSubsumptionsNumber(ontology);
        computeInferredSubsumptionsNumber(ontology);
    }

    private void computeDepth(Map<String, OntologyClass> ontology, List<OntologyClass> topLevelClasses) {
        this.depth = 0;
        Map<OntologyClass, Long> depths = new HashMap<>();
        for (OntologyClass ontologyClass : ontology.values()) {
            depths.put(ontologyClass, -1L);
        }

        Queue<OntologyClass> queue = new LinkedList<>();
        for (OntologyClass ontologyClass : topLevelClasses) {
            queue.add(ontologyClass);
            depths.put(ontologyClass, 0L);
        }

        while (!queue.isEmpty()) {
            OntologyClass currentClass = queue.poll();
            long currentDepth = depths.get(currentClass);

            for (OntologyClass child : currentClass.getChildren()) {
                if (this.cycles.isEmpty() && depths.get(child) < currentDepth + 1 || depths.get(child) == -1) {
                    depths.put(child, currentDepth + 1);
                    queue.add(child);

                    if (currentDepth + 1 > this.depth) {
                        this.depth = currentDepth + 1;
                    }
                }
            }
        }
    }

    private void computeDirectSubsumptionsNumber(Map<String, OntologyClass> ontology) {
        this.directSubsumptionsNumber = 0L;

        for (OntologyClass ontologyClass : ontology.values()) {
            this.directSubsumptionsNumber += ontologyClass.getParentsNumber();
        }
    }

    private void computeInferredSubsumptionsNumber(Map<String, OntologyClass> ontology) {
        this.inferredSubsumptionsNumber = 0L;

        for (OntologyClass ontologyClass : ontology.values()) {
            Map<OntologyClass, Boolean> seen = new HashMap<>();
            seen.put(ontologyClass, true);
            Queue<OntologyClass> queue = new LinkedList<>();

            for (OntologyClass parent : ontologyClass.getParents()) {
                seen.put(parent, true);
                queue.add(parent);
            }

            while (!queue.isEmpty()) {
                OntologyClass currentClass = queue.poll();

                for (OntologyClass parent : currentClass.getParents()) {
                    if (!seen.containsKey(parent)) {
                        seen.put(parent, true);
                        queue.add(parent);
                    }
                }
            }

            this.inferredSubsumptionsNumber += seen.size() - 1 - ontologyClass.getParentsNumber();
        }

    }

    private void computeCycles(Map<String, OntologyClass> ontology) {
        this.cycles = new ArrayList<>();

        int i = 1;
        for (OntologyClass ontologyClass : ontology.values()) {
            System.out.println("[INFO] Cycle computation on " + ontologyClass.getName() + " " + i + " / " + ontology.values().size() + ")");

            HashMap<OntologyClass, Boolean> ancestors = new HashMap<>();
            HashMap<OntologyClass, Boolean> descendants = new HashMap<>();
            Queue<OntologyClass> queue = new LinkedList<>();

            System.out.println("[INFO] Computing ancestors");
            queue.add(ontologyClass);
            while (!queue.isEmpty()) {
                OntologyClass c = queue.poll();

                for (OntologyClass parent : c.getParents()) {
                    if (!ancestors.containsKey(parent) && parent != ontologyClass) {
                        ancestors.put(parent, true);
                        queue.add(parent);
                    }
                }
            }

            System.out.println("[INFO] Computing descendants");
            queue.add(ontologyClass);
            while (!queue.isEmpty()) {
                OntologyClass c = queue.poll();

                for (OntologyClass child : c.getChildren()) {
                    if (!descendants.containsKey(child) && child != ontologyClass) {
                        descendants.put(child, true);
                        queue.add(child);
                    }
                }
            }

            System.out.println("[INFO] Computing intersection");
            HashMap<OntologyClass, Boolean> intersection = new HashMap<>();
            for (OntologyClass ancestor : ancestors.keySet()) {
                if (descendants.containsKey(ancestor))
                    intersection.put(ancestor, true);
            }
            descendants.clear();
            ancestors.clear();

            if (!intersection.isEmpty()) {
                System.out.println("[INFO] Computing cycles (intersection size: " + intersection.size() + ")");

                Deque<OntologyPath> stack = new LinkedList<>();
                stack.addFirst(new OntologyPath(ontologyClass));

                while (!stack.isEmpty()) {
                    OntologyPath p = stack.pollFirst();

                    for (OntologyClass c : p.getLast().getChildren()) {
                        if (c == ontologyClass) {
                            List<OntologyClass> cycle = p.getPath();
                            cycle.add(ontologyClass);
                            cycles.add(cycle);
                        }

                        else if (intersection.containsKey(c) && !p.contains(c)) {
                            OntologyPath toTest = new OntologyPath(p);
                            toTest.add(c);
                            stack.addFirst(toTest);
                        }
                    }
                }
            }

            i++;
        }
    }

    public long getElementsNumber() {
        return elementsNumber;
    }

    public long getTopLevelClassesNumber() {
        return topLevelClassesNumber;
    }

    public long getDepth() {
        return depth;
    }

    public long getDirectSubsumptionsNumber() {
        return directSubsumptionsNumber;
    }

    public long getInferredSubsumptionsNumber() {
        return inferredSubsumptionsNumber;
    }

    public int getCyclesNumber() {
        return cycles.size();
    }

    public List<List<OntologyClass>> getCycles() {
        ArrayList<List<OntologyClass>> copyCycles = new ArrayList<>();

        for (List<OntologyClass> cycle : cycles) {
            copyCycles.add(new ArrayList<>(cycle));
        }

        return copyCycles;
    }
}
