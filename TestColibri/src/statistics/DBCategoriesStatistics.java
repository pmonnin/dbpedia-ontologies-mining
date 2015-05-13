package statistics;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

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
		// Children relationship creation
		Set<String> keys = this.categories.keySet();
		for(String key : keys) {
			for(String parent : this.categories.get(key).getParents()) {
				DBCategory parentCat = this.categories.get(parent);
				
				if(parentCat != null)
					parentCat.addChild(key);
			}
		}
		
		// Categories number
		this.categoriesNumber = this.categories.size();
		
		// Orphans number, direct subsumptions number, 
		for(String key : keys) {			
			// Orphans
			if(this.categories.get(key).getParentsNumber() == 0) {
				this.orphansNumber++;
				
				// If it's an orphan we can compute depth from it
				int computedDepth = computeDepth(key);
				if(computedDepth > this.depth)
					this.depth = computedDepth;
			}
			
			// Direct subsumptions
			this.directSubsumptions += this.categories.get(key).getParentsNumber();
			
			// Inferred subsumptions
			this.inferredSubsumptions += computeInferredSubsumptions(key);
		}
	}
	
	public void displayStatistics() {
		System.out.println("== DBPedia categories statistics ==");
		System.out.println("Classes number: " + this.categoriesNumber);
		System.out.println("Direct subsomptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsomptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
	
	private int computeDepth(String key) {
		int currentDepth = 0;
		Stack<String> stack = new Stack<String>();
		stack.push(key);
		
		while(!stack.isEmpty()) {
			String category = stack.pop();
			DBCategory dbCategory = this.categories.get(category);
			
			if(dbCategory != null) {
				if(dbCategory.getChildrenNumber() == 0 && dbCategory.getDepth() > currentDepth) {
					currentDepth = dbCategory.getDepth();
				}
				
				else {
					for(String child : dbCategory.getChildren()) {
						DBCategory childCat = this.categories.get(child);
						
						if(childCat != null && childCat.getDepth() <= dbCategory.getDepth()) {
							childCat.setDepth(dbCategory.getDepth() + 1);
							stack.push(child);
						}
					}
				}
			}
		}
		
		return currentDepth;
	}
	
	private int computeInferredSubsumptions(String key) {
		int inferredSubsumptions = 0;
		Stack<String> stack = new Stack<String>();
		
		for(String parent : this.categories.get(key).getParents()) {
			stack.push(parent);
		}
		
		while(!stack.isEmpty()) {
			DBCategory cat = this.categories.get(stack.pop());
			
			if(cat != null) {
				inferredSubsumptions += cat.getParentsNumber();
				
				for(String child : cat.getChildren()) {
					stack.push(child);
				}
			}
		}
		
		return inferredSubsumptions;
	}
}
