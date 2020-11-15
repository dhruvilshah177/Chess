package controller;

/**
 * interface for TriPredicate
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */

public interface Tripredicate_Interface<T, U, V> {
	/**
	 * @param t parameter
	 * @param u oarameter
	 * @param v parameter
	 * @return if TriPredicate criteria is met, then true; otherwise, false
	 */
	boolean test(T t, U u, V v);
}