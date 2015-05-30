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
		
		// Orphans number anddirect subsumptions number
		ArrayList<String> orphans = new ArrayList<String>();
		for(String key : this.yagoClasses.keySet()) {
			if(this.yagoClasses.get(key).getParentsNumber() == 0) {
				// Orphans
				this.orphansNumber++;
				orphans.add(key);
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
	
	private void computeDepth(ArrayList<String> orphans) {
		LinkedList<String> queue = new LinkedList<String>();
		this.depth = 1;
		
		// Algorithm initialization
		for(String yago : this.yagoClasses.keySet()) {
			this.yagoClasses.get(yago).setDepth(-1);
		}
		
		for(String orphan : orphans) {
			this.yagoClasses.get(orphan).setDepth(1);
			queue.add(orphan);
		}
		
		// Algorithm computation
		while(!queue.isEmpty()) {
			String yago = queue.pollFirst();
			DBYagoClass dbYago = this.yagoClasses.get(yago);
			
			if(dbYago != null) {
				boolean childrenModified = false;
				for(String child : dbYago.getChildren()) {
					DBYagoClass childClass = this.yagoClasses.get(child);
					
					if(childClass != null && childClass.getDepth() == -1) {
						childClass.setDepth(dbYago.getDepth() + 1);
						queue.add(child);
						childrenModified = true;
					}
				}
				
				if(!childrenModified && dbYago.getDepth() > this.depth) {
					this.depth = dbYago.getDepth();
				}
				
			}
		}
	}
	
	private void computeInferredSubsumptions() {
		for(String key : this.yagoClasses.keySet()) {
			
			// Algorithm initialization
			for(String yago : this.yagoClasses.keySet()) {
				this.yagoClasses.get(yago).setSeen(false);
			}
			
			LinkedList<String> queue = new LinkedList<String>();
			this.yagoClasses.get(key).setSeen(true);
			for(String parent : this.yagoClasses.get(key).getParents()) {
				DBYagoClass yago = this.yagoClasses.get(parent);
				
				if(yago != null) {
					yago.setSeen(true);
					queue.add(parent);
				}
			}
			
			// Algorithm computation
			while(!queue.isEmpty()) {
				String yago = queue.pollFirst();
				DBYagoClass dbYago = this.yagoClasses.get(yago);
				
				if(dbYago != null) {
					for(String parent : dbYago.getParents()) {
						DBYagoClass dbParent = this.yagoClasses.get(parent);
						
						if(dbParent != null && !dbParent.getSeen()) {
							dbParent.setSeen(true);
							queue.add(parent);
							this.inferredSubsumptions++;
						}
					}
				}
			}
		
		}
	}
}
