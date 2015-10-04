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
		ArrayList<String> orphans = new ArrayList<String>();
		for(String key : keys) {	
			// Orphans
			if(this.categories.get(key).getParentsNumber() == 0) {
				this.orphansNumber++;
				orphans.add(key);
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
	
	private void computeDepth(ArrayList<String> orphans) {
		LinkedList<String> queue = new LinkedList<String>();
		this.depth = 1;
		
		// Algorithm initialization
		for(String category : this.categories.keySet()) {
			this.categories.get(category).setDepth(-1);
		}
		
		for(String orphan : orphans) {
			this.categories.get(orphan).setDepth(1);
			queue.add(orphan);
		}
		
		// Algorithm computation
		while(!queue.isEmpty()) {
			String cat = queue.pollFirst();
			DBCategory dbCat = this.categories.get(cat);
			
			if(dbCat != null) {
				for(String child : dbCat.getChildren()) {
					DBCategory childClass = this.categories.get(child);
					
					if(childClass != null && childClass.getDepth() == -1) {
						childClass.setDepth(dbCat.getDepth() + 1);
						queue.add(child);
					}
				}
				
				if(dbCat.getDepth() > this.depth) {
					this.depth = dbCat.getDepth();
				}
				
			}
		}
	}
	
	private void computeInferredSubsumptions() {
		for(String key : this.categories.keySet()) {
			
			// Algorithm initialization
			for(String cat : this.categories.keySet()) {
				this.categories.get(cat).setSeen(false);
			}
			
			LinkedList<String> queue = new LinkedList<String>();
			this.categories.get(key).setSeen(true);
			for(String parent : this.categories.get(key).getParents()) {
				DBCategory cat = this.categories.get(parent);
				
				if(cat != null) {
					cat.setSeen(true);
					queue.add(parent);
				}
			}
			
			// Algorithm computation
			while(!queue.isEmpty()) {
				String cat = queue.pollFirst();
				DBCategory dbCat = this.categories.get(cat);
				
				if(dbCat != null) {
					for(String parent : dbCat.getParents()) {
						DBCategory dbParent = this.categories.get(parent);
						
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
