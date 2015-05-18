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
		ArrayList<String> orphans = new ArrayList<>(); 
		for(String key : keys) {
			// Children relationship creation
			for(String parent : this.categories.get(key).getParents()) {
				DBCategory parentCat = this.categories.get(parent);
				
				if(parentCat != null)
					parentCat.addChild(key);
			}
			
			// Orphans
			if(this.categories.get(key).getParentsNumber() == 0) {
				this.orphansNumber++;
				orphans.add(key);
			}
			
			// Direct subsumptions
			this.directSubsumptions += this.categories.get(key).getParentsNumber();
			
			// Depth preparation
			this.categories.get(key).setDepth(-1);
		}
		
		// Compute depth
		System.out.println("Computing depth");
		computeDepth(orphans);
		
		// Compute inferred subsumptions
//		System.out.println("Computing inferred subsumptions");
//		computeInferredSubsumptions();
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
		LinkedList<String> stack = new LinkedList<String>();
		
		for(String key : orphans) {
			stack.add(key);
			this.categories.get(key).setDepth(0);
			
			int i = 0;
			while(!stack.isEmpty()) {
				String cat = stack.pollFirst();
				DBCategory dbCategory = this.categories.get(cat);
				
				if(dbCategory != null) {
					for(String child : dbCategory.getChildren()) {
						DBCategory dbChild = this.categories.get(child);
						
						if(dbChild != null && dbChild.getDepth() < dbCategory.getDepth() + 1) {
							dbChild.setDepth(dbCategory.getDepth() + 1);
							stack.add(child);
							
							if(dbChild.getDepth() > this.depth)
								this.depth = dbChild.getDepth();
						}
					}
				}
				
				i++;
				if(i % 500 == 0)
					System.out.println("i: " + i + " depth: " + this.depth);
			}
		}
	}
	
	private void computeInferredSubsumptions() {
		Set<String> keys = this.categories.keySet();
		
		// Algorithm for each category
		int i = 0;
		for(String cat : keys) {
			System.out.println("Inferred subsumptions : " + i + " / " + keys.size());
			
			// Algorithm initialization
			for(String key : keys) {
				this.categories.get(key).setInferredSubsumptionsToken(false);
			}
			
			boolean done = false;
			// We put the token to true on every reachable category 
			while(!done) {
				done = true;
				
				for(String key : keys) {
					if(this.categories.get(key).getInferredSubsumptions()) {
						for(String parent : this.categories.get(key).getParents()) {
							DBCategory parentCat = this.categories.get(parent);
							
							if(parentCat != null) {
								if(!parentCat.getInferredSubsumptions()) {
									parentCat.setInferredSubsumptionsToken(true);
									done = false;
								}
							}
						}
					}
				}
			}
			
			// Algorithm disarming
			for(String key : keys) {
				if(this.categories.get(key).getInferredSubsumptions())
					this.inferredSubsumptions++;
			}
			this.inferredSubsumptions -= this.categories.get(cat).getParentsNumber();
			i++;
		}
	}
}
