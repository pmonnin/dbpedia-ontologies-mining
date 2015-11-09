package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import dbpediaobjects.DBYagoClass;

/**
 * Class computing statistics over DB Yago classes hierarchy
 * 
 * @author Pierre Monnin
 *
 */
public class DBYagoClassesStatistics {
	private HashMap<String, DBYagoClass> yagoClasses;
	private int yagoClassesNumber;
	private int orphansNumber;
	private int depth;
	private int directSubsumptions;
	private int inferredSubsumptions;
	
	public DBYagoClassesStatistics(HashMap<String, DBYagoClass> yagoClasses) {
		this.yagoClasses = yagoClasses;
		this.yagoClassesNumber = 0;
		this.orphansNumber = 0;
		this.depth = 0;
		this.directSubsumptions = 0;
		this.inferredSubsumptions = 0;
	}
	
	public void computeStatistics() {
		// Yago classes number
		this.yagoClassesNumber = this.yagoClasses.size();
		
		// Orphans number and direct subsumptions number
		ArrayList<DBYagoClass> orphans = new ArrayList<>();
		for(String key : this.yagoClasses.keySet()) {
			if(this.yagoClasses.get(key).getParentsNumber() == 0) {
				// Orphans
				this.orphansNumber++;
				orphans.add(this.yagoClasses.get(key));
			}
			
			// Direct subsumptions
			this.directSubsumptions += this.yagoClasses.get(key).getParentsNumber();
		}
		
		// Depth
		computeDepth(orphans);
		
		// Inferred subsumptions
		computeInferredSubsumptions();
	}
	
	public void displayStatistics() {
		System.out.println("== DB Yago classes hierarchy statistics ==");
		System.out.println("Classes number: " + this.yagoClassesNumber);
		System.out.println("Direct subsumptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsumptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
	
	private void computeDepth(ArrayList<DBYagoClass> orphans) {
		LinkedList<DBYagoClass> queue = new LinkedList<>();
		this.depth = 1;
		
		// Algorithm initialization
		for(String yago : this.yagoClasses.keySet()) {
			this.yagoClasses.get(yago).setDepth(-1);
		}
		
		for(DBYagoClass orphan : orphans) {
			orphan.setDepth(1);
			queue.add(orphan);
		}
		
		// Algorithm computation
		while(!queue.isEmpty()) {
			DBYagoClass yago = queue.pollFirst();

			for(DBYagoClass child : yago.getChildren()) {
				if(child.getDepth() == -1) {
					child.setDepth(yago.getDepth() + 1);
					queue.add(child);
				}
			}

			if(yago.getDepth() > this.depth) {
				this.depth = yago.getDepth();
			}
		}
	}
	
	private void computeInferredSubsumptions() {
		for(String key : this.yagoClasses.keySet()) {
			
			// Algorithm initialization
			for(String yago : this.yagoClasses.keySet()) {
				this.yagoClasses.get(yago).setSeen(false);
			}
			
			LinkedList<DBYagoClass> queue = new LinkedList<>();
			this.yagoClasses.get(key).setSeen(true);
			for(DBYagoClass parent : this.yagoClasses.get(key).getParents()) {
				parent.setSeen(true);
				queue.add(parent);
			}
			
			// Algorithm computation
			while(!queue.isEmpty()) {
				DBYagoClass yago = queue.pollFirst();

				for(DBYagoClass parent : yago.getParents()) {
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
