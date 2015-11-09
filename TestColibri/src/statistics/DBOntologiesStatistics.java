package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import dbpediaobjects.DBOntology;

/**
 * Class computing statistics over DB Ontology classes hierarchy
 * 
 * @author Pierre Monnin
 *
 */
public class DBOntologiesStatistics {
	private HashMap<String, DBOntology> ontologies;
	private int ontologiesNumber;
	private int orphansNumber;
	private int depth;
	private int directSubsumptions;
	private int inferredSubsumptions;
	
	public DBOntologiesStatistics(HashMap<String, DBOntology> ontologies) {
		this.ontologies = ontologies;
		this.ontologiesNumber = 0;
		this.orphansNumber = 0;
		this.depth = 0;
		this.directSubsumptions = 0;
		this.inferredSubsumptions = 0;
	}
	
	public void computeStatistics() {
		// Ontologies number
		this.ontologiesNumber = this.ontologies.size();
		
		// Orphans number, direct subsumptions number
		ArrayList<DBOntology> orphans = new ArrayList<>();
		for(String key : this.ontologies.keySet()) {
			// Orphans
			if(this.ontologies.get(key).getParentsNumber() == 0) {
				this.orphansNumber++;
				orphans.add(this.ontologies.get(key));
			}
		
			// Direct subsumptions
			this.directSubsumptions += this.ontologies.get(key).getParentsNumber();
		}
		
		// Depth
		computeDepth(orphans);
		
		// Inferred subsumptions
		computeInferredSubsumptions();
	}
	
	public void displayStatistics() {
		System.out.println("== DB Pedia Ontology classes statistics ==");
		System.out.println("Classes number: " + this.ontologiesNumber);
		System.out.println("Direct subsumptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsumptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
	
	private void computeDepth(ArrayList<DBOntology> orphans) {
		LinkedList<DBOntology> queue = new LinkedList<>();
		this.depth = 1;
		
		// Algorithm initialization
		for(String ontology : this.ontologies.keySet()) {
			this.ontologies.get(ontology).setDepth(-1);
		}
		
		for(DBOntology orphan : orphans) {
			orphan.setDepth(1);
			queue.add(orphan);
		}
		
		// Algorithm computation
		while(!queue.isEmpty()) {
			DBOntology ontology = queue.pollFirst();

			for(DBOntology child : ontology.getChildren()) {
				if(child.getDepth() == -1) {
					child.setDepth(ontology.getDepth() + 1);
					queue.add(child);
				}
			}

			if(ontology.getDepth() > this.depth) {
				this.depth = ontology.getDepth();
			}
		}
	}
	
	private void computeInferredSubsumptions() {
		for(String key : this.ontologies.keySet()) {
			
			// Algorithm initialization
			for(String onto : this.ontologies.keySet()) {
				this.ontologies.get(onto).setSeen(false);
			}
			
			LinkedList<DBOntology> queue = new LinkedList<>();
			this.ontologies.get(key).setSeen(true);
			for(DBOntology parent : this.ontologies.get(key).getParents()) {
				parent.setSeen(true);
				queue.add(parent);
			}
			
			// Algorithm computation
			while(!queue.isEmpty()) {
				DBOntology ontology = queue.pollFirst();

				for(DBOntology parent : ontology.getParents()) {
					if(!parent.getSeen()) {
						parent.setSeen(true);
						queue.add(parent);
						this.inferredSubsumptions++;
					}
				}
			}
		}
	}
}
