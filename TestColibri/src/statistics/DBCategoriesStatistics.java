package statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import dbpediaobjects.DBCategory;

/**
 * Class computing statistics over the DB Categories hierarchy
 * 
 * @author Pierre Monnin
 *
 */
public class DBCategoriesStatistics {
	private HashMap<String, DBCategory> categories;
	private int categoriesNumber;
	private int orphansNumber;
	private int depth;
	private int directSubsumptions;
	private int inferredSubsumptions;
	
	public DBCategoriesStatistics(HashMap<String, DBCategory> categories) {
		this.categories = categories;
		this.categoriesNumber = 0;
		this.orphansNumber = 0;
		this.depth = 0;
		this.directSubsumptions = 0;
		this.inferredSubsumptions = 0;
	}
	
	public void computeStatistics() {
		// Categories number
		this.categoriesNumber = this.categories.size();
		
		// Orphans number, direct subsumptions number,
		Set<String> keys = this.categories.keySet();
		ArrayList<DBCategory> orphans = new ArrayList<>();
		for(String key : keys) {	
			// Orphans
			if(this.categories.get(key).getParentsNumber() == 0) {
				this.orphansNumber++;
				orphans.add(this.categories.get(key));
			}
			
			// Direct subsumptions
			this.directSubsumptions += this.categories.get(key).getParentsNumber();
		}
		
		// Depth computation
		computeDepth(orphans);
		
		// Inferred subsumptions
		computeInferredSubsumptions();
	}
	
	public void displayStatistics() {
		System.out.println("== DBPedia categories statistics ==");
		System.out.println("Classes number: " + this.categoriesNumber);
		System.out.println("Direct subsumptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsumptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
	
	private void computeDepth(ArrayList<DBCategory> orphans) {
		LinkedList<DBCategory> queue = new LinkedList<>();
		this.depth = 1;
		
		// Algorithm initialization
		for(String category : this.categories.keySet()) {
			this.categories.get(category).setDepth(-1);
		}
		
		for(DBCategory orphan : orphans) {
			orphan.setDepth(1);
			queue.add(orphan);
		}
		
		// Algorithm computation
		while(!queue.isEmpty()) {
			DBCategory cat = queue.pollFirst();

			for(DBCategory child : cat.getChildren()) {
				if(child.getDepth() == -1) {
					child.setDepth(cat.getDepth() + 1);
					queue.add(child);
				}
			}

			if(cat.getDepth() > this.depth) {
				this.depth = cat.getDepth();
			}
		}
	}
	
	private void computeInferredSubsumptions() {
		for(String key : this.categories.keySet()) {
			
			// Algorithm initialization
			for(String cat : this.categories.keySet()) {
				this.categories.get(cat).setSeen(false);
			}
			
			LinkedList<DBCategory> queue = new LinkedList<>();
			this.categories.get(key).setSeen(true);
			for(DBCategory parent : this.categories.get(key).getParents()) {
				parent.setSeen(true);
				queue.add(parent);
			}
			
			// Algorithm computation
			while(!queue.isEmpty()) {
				DBCategory cat = queue.pollFirst();

                for(DBCategory parent : cat.getParents()) {
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
