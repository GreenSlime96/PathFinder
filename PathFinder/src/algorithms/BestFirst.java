package algorithms;

import java.util.function.BiFunction;

public class BestFirst extends Search {
	
	public static final void search(Search search) {
		BiFunction<Integer, Integer, Double> old_h = heuristic;
		heuristic = ((t, u) -> old_h.apply(t, u) * 1000000);
		AStar.search(search);
	}

}
