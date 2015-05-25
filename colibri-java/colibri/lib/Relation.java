package colibri.lib;

import java.util.Collection;
import java.util.Iterator;

/**
 * Representation of a context, i.e.&nbsp;a binary relation between a set
 * of objects and a set of attributes.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public interface Relation {
	
	/**
	 * Activates write protection on this relation.
	 * <p>
	 * After this method has been called, this relation shall be unmodifiable,
	 * i.e.&nbsp;if there is a call attempting to change the contents of 
	 * this set, an exception will be thrown.
	 * 
	 * Note that the implementation of this method is optional in classes
	 * that implement this interface.
	 * @return <code>true</code> iff write protection is activated.
	 */
	boolean disallowChanges();
	
	/**
	 * Returns the number of objects contained in the object set of this relation.
	 * @return the number of objects contained in the object set of this relation.
	 */
	public int getSizeObjects();
	
	
	/**
	 * Returns the number of attributes contained in the attribute set of this relation.
	 * @return the number of attributes contained in the attribute set of this relation.
	 */
	public int getSizeAttributes();
	
	
	/**
	 * Returns a copy of the set of all objects contained in this relation.
	 * <p>
	 * The set returned by this method is modifiable and modifying 
	 * that set does not affect the contents of this relation.
	 * @return the set of all objects contained in this relation.
	 */
	public ComparableSet getAllObjects ();
	
	/**
	 * Returns a copy of the set of all attributes contained in this relation.
	 * <p>
	 * The set returned by this method is modifiable and modifying 
	 * that set does not affect the contents of this relation.
	 * @return the set of all attributes contained in this relation.
	 */
	public ComparableSet getAllAttributes ();
	
	
	/**
	 * Returns an iterator over the objects which have the specified
	 * <code>attribute</code>.
	 * <p>
	 * More formally, this method returns an iterator iterating over
	 * all objects <code>o</code> for which it is true that the pair
	 * (<code>o</code>, <code>attribute</code>)
	 * is contained in this relation.
	 * <p>
	 * There are no guarantees concerning the order in which the objects
	 * are returned (unless this relation is an instance of a class that
	 * provides a guarantee).
	 * @param attribute the attribute whose objects shall be returned.
	 * @return an iterator over the objects of <code>attribute</code>.
	 */
	public Iterator<Comparable> getObjects (Comparable attribute);
	
	/**
	 * Returns an iterator over the attributes the specified
	 * <code>object</code> has.
	 * <p>
	 * More formally, this method returns an iterator iterating over
	 * all attributes <code>a</code> for which it is true that the pair 
	 * (<code>object</code>, <code>a</code>) is contained in this relation.
	 * <p>
	 * There are no guarantees concerning the order in which the attributes
	 * are returned (unless this relation is an instance of a class that
	 * provides a guarantee).
	 * @param object the object whose objects shall be returned.
	 * @return an iterator over the attributes of <code>object</code>.
	 */
	public Iterator<Comparable> getAttributes (Comparable object);
	
	
	/**
	 * Returns a set that contains all objects which have the
	 * specified <code>attribute</code>. 
	 * Note that the set that is returned by this method might be
	 * unmodifiable.
	 * @param attribute the attribute whose objects shall be returned.
	 * @return a set that contains all objects of <code>attribute</code>.
	 */
	public ComparableSet getObjectSet (Comparable attribute);
	
	/**
	 * Returns a set that contains all attributes the specified
	 * <code>object</code> has. 
	 * Note that the set that is returned by this method might be
	 * unmodifiable.
	 * @param object the object whose attributes shall be returned.
	 * @return a set that contains all attributes of <code>object</code>.
	 */
	public ComparableSet getAttributeSet (Comparable object);
	
	/**
	 * Returns a set of objects which have all the specified attributes.
	 * <p>
	 * More formally, this method returns a set that contains all objects
	 * <code>o</code> for which it is true that for all attributes
	 * <code>a</code> that are contained in <code>coll</code>
	 * the pair (<code>o</code>, <code>a</code>) is contained in
	 * this relation.
	 * @param attributes a collection of attributes. 
	 * @return the set of objects that have all the attributes
	 * contained in the collection <code>attributes</code>.
	 * @throws IllegalArgumentException if <code>attributes</code> is <code>null</code>
	 * one of the elements contained in <code>attributes</code> is not
	 * contained in the attribute set of this relation.
	 */
	public ComparableSet commonObjects(Collection<Comparable> attributes) throws IllegalArgumentException;
	
	
	/**
	 * Returns a set of attributes which are associated with all the
	 * specified objects.
	 * <p>
	 * More formally, this method returns a set that contains all attributes
	 * <code>a</code> for which it is true that for all objects
	 * <code>o</code> that are contained in <code>coll</code>
	 * the pair (<code>o</code>, <code>a</code>) is contained in
	 * this relation.
	 * @param objects a collection of objects.
	 * @return the set of attributes all objects contained in the
	 * collection <code>objects</code> have.
	 * @throws IllegalArgumentException if <code>objects</code> is <code>null</code>
	 * or one of the elements contained in <code>objects</code> is not
	 * contained in the object set of this relation.
	 */
	public ComparableSet commonAttributes(Collection<Comparable> objects) throws IllegalArgumentException;
	
	
	/**
	 * Adds the pair (<code>object</code>, <code>attribute</code>) to this relation.
	 * <p>
	 * If <code>object</code> is not yet contained in the object set of this relation
	 * it will be added to that set. Similarly, if <code>attribute</code> is not yet
	 * contained in the attribute set of this relation it will be added to that
	 * set.
	 * <p>
	 * If <code>object</code> is <code>null</code> no pair will be added to this
	 * relation but <code>attribute</code> will be added to the attribute set of this relation.
	 * Similarly, if <code>attribute</code> is <code>null</code> no pair will be
	 * added to this relation but <code>object</code> will be added to the
	 * object set of this relation.
	 * @param object the object. If this is <code>null</code>, only the <code>attribute</code> will be added
	 * to the attribute set.
	 * @param attribute the attribute. If this is null, only the <code>object</code> will be added
	 * to the object set
	 * @throws ClassCastException if <code>object</code> is not mutually comparable with the objects contained
	 * in the object set of this relation or <code>attribute</code> is not mutually comparable with the
	 * attributes contained in the attribute set of this relation.
	 * @throws UnsupportedOperationException if this relation is write protected.
	 */
	public void add(Comparable object, Comparable attribute) throws ClassCastException, UnsupportedOperationException;
	
	
	/**
	 * Removes the pair (<code>object</code>, <code>attribute</code>) from this relation.
	 * <p>
	 * If <code>object</code> is <code>null</code> then <code>attribute</code> will
	 * be removed from the attribute set of this relation and all pairs whose attribute
	 * is <code>attribute</code> will be removed from this relation.
	 * Similarly, if <code>attribute</code> is <code>null</code> then <code>object</code> will
	 * be removed from the attribute set of this relation and all pairs whose object
	 * is <code>object</code> will be removed from this relation.
	 * @param object the object.
	 * @param attribute the attribute.
	 * @throws UnsupportedOperationException if this relation is write protected.
	 */
	public void remove(Comparable object, Comparable attribute) throws UnsupportedOperationException;
	
	
	/**
	 * Returns <code>true</code> if and only if the pair
	 * (<code>object</code>, <code>attribute</code>) is contained in this relation.
	 * <p>
	 * If <code>object</code> is <code>null</code> this method will return
	 * <code>true</code> if and only if <code>attribute</code> is contained
	 * in the attribute set of this relation.
	 * Similarly, if <code>attribute</code> is <code>null</code> this method will return
	 * <code>true</code> if and only if <code>object</code> is contained
	 * in the object set of this relation.
	 * @param object the object.
	 * @param attribute the attribute.
	 * @return <code>true</code>, iff the pair (<code>object</code>, <code>attribute</code>)
	 * is contained in the relation.
	 */
	public boolean contains (Object object, Object attribute);
}
