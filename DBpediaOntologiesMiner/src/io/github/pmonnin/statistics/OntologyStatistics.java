package io.github.pmonnin.statistics;

import io.github.pmonnin.semanticwebobjects.OntologyClass;

import java.util.*;

/**
 * Compute and store statistics about the ontology given to the constructor
 * @author Pierre Monnin
 */
public class OntologyStatistics {
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

        for (OntologyClass ontologyClass : ontology.values()) {
            Queue<List<OntologyClass>> queue = new LinkedList<>();
            queue.add(new ArrayList<OntologyClass>() {{add(ontologyClass);}});

            while (!queue.isEmpty()) {
                List<OntologyClass> current = queue.poll();

                for (OntologyClass child : current.get(current.size() - 1).getChildren()) {
                    if (child == ontologyClass) {
                        List<OntologyClass> cycle = new ArrayList<>(current);
                        cycle.add(ontologyClass);
                        this.cycles.add(cycle);
                    }

                    else if (!current.contains(child)) {
                        List<OntologyClass> toInvestigate = new ArrayList<>(current);
                        toInvestigate.add(child);
                        queue.add(toInvestigate);
                    }
                }
            }
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
