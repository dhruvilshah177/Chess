package controller;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * helper class for Collections utility class
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Helper {

	/**
	 * @param tColl input collection
	 * @param u parameter
	 * @param v parameter
	 * @param triP TriPredicate from interface
	 * @param <T> type parameter
	 * @param <U> type parameter
	 * @param <V> type parameter
	 * @return if predicate is met at least by one in Collection, then true; otherwise, false
	 */
	public static <T,U,V> boolean findOne(Collection<T> tColl, U u, V v, Tripredicate_Interface<T,U,V> triP) {
        return tColl.stream().anyMatch(t -> triP.test(t, u, v));
    }

	/**
	 * @param tColl input
	 * @param pred predicate
	 * @param <T> type parameter
	 * @return if predicate is met at least by one in Collection, then true; otherwise, false
	 */
	public static <T> boolean findOne(Collection<T> tColl, Predicate<T> pred) {
        return tColl.stream().anyMatch(pred);		    
	}

    /**
     * @param tColl input collection
     * @param u parameter
     * @param biP BiPredicate from Collection
     * @param <T> type parameter
     * @param <U> type parameter
     * @return list of elements in Collection that meets set criteria
     */
    public static <T,U> List<T> filter(Collection<T> tColl, U u, BiPredicate<T,U> biP) {
        return tColl.stream().filter(t -> biP.test(t, u)).collect(Collectors.toList());
    }
}