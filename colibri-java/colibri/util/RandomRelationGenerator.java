package colibri.util;

import java.util.Random;

import colibri.lib.Relation;
import colibri.lib.TreeRelation;




/**
 * Creates a random binary relation.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class RandomRelationGenerator {
	public RandomRelationGenerator() {
		
	}
	
	
	private String objectString(int object) {
		return "o".concat(Integer.toString(object));
	}
	
	
	private String attributeString(int attribute) {
		return "a".concat(Integer.toString(attribute));
	}
	
	
	/**
	 * generates a new Relation with random items
	 * @param objects number of objects that shall be contained in the relation
	 * @param attributes number of attributes that shall be contained in the relation
	 * @param density determines, how many pairs the relation will contain (must be a value between 0 and 1)
	 * @return Relation generated according to the values of the arguments
	 * @throws IllegalArgumentException
	 */
	public Relation generate(int objects, int attributes, float density) throws IllegalArgumentException {
		return generate(objects, attributes, density, true);
	}
	
	
	/**
	 * generates a new Relation with random items
	 * @param objects number of objects that shall be contained in the relation
	 * @param attributes number of attributes that shall be contained in the relation
	 * @param density determines, how many pairs the relation will contain (must be a value between 0 and 1)
	 * @param discardLonelyItems if true, only objects with at least one attribute and attributes with at least one object will be contained in the generated relation.
	 * @return Relation generated according to the values of the arguments
	 * @throws IllegalArgumentException
	 */
	public Relation generate(int objects, int attributes, float density, boolean discardLonelyItems) throws IllegalArgumentException {
		Relation relation = new TreeRelation();
		
		if (density < 0 || density > 1)
			throw new IllegalArgumentException("density must be between 0 and 1");
		
		if (!discardLonelyItems) {
			//add all objects to the relation
			for (int i = 0; i < objects; i++) {
				relation.add(objectString(i), null);
			}
			
			//add all attributes to the relation
			for (int i = 0; i < attributes; i++) {
				relation.add(null, attributeString(i));
			}			
		}
		
		//compute the number of pairs that will be contained in the relation
		long pairs = Math.round(objects * attributes * density);
		Random random = new Random();
		
		//try not to have any lonely items
		if (discardLonelyItems) {
			for (int i = 0; i < objects && pairs > 0; i++) {
				int attribute = random.nextInt(attributes);
				relation.add(objectString(i), attributeString(attribute));
				pairs--;
			}
			
			for (int i = 0; i < attributes && pairs > 0; i++) {
				if (!relation.contains(null, attributeString(i))) {
					int object = random.nextInt(objects);
					relation.add(objectString(object), attributeString(i));
					pairs--;
				}
			}
		}
		
		if (density <= 0.5) {
			for(int i = 0; i < pairs; i++) {
				int object = random.nextInt(objects);
				int attribute = random.nextInt(attributes);
				
				//if the pair is already contained in the relation search for another one
				while (relation.contains(objectString(object), attributeString(attribute))) {
					object = random.nextInt(objects);
					attribute = random.nextInt(attributes);
				}
				
				relation.add(objectString(object), attributeString(attribute));
			}
		}
		else {
			//add all pairs to the relation
			for (int i = 0; i < objects; i++) {
				for (int j = 0; j < attributes; j++) {
					relation.add(objectString(i), attributeString(j));
				}
			}
			
			
			for(int i = objects * attributes; i > pairs; i--) {
				int object = random.nextInt(objects);
				int attribute = random.nextInt(attributes);
				//if the pair has already been removed from the relation search for another one
				while (relation.contains(objectString(object), attributeString(attribute)) == false) {
					object = random.nextInt(objects);
					attribute = random.nextInt(attributes);
				}
				
				relation.remove(objectString(object), attributeString(attribute));
			}
		}
		
		return relation;
	}
}
