package algorithms;

import java.util.function.BiFunction;

public class BestFirst {
	
	public static final void search(Search search, BiFunction<Integer, Integer, Double> heuristic) {
		// pass onto AStar but bloat the heuristic as to eclipse g(x)
		AStar.search(search, (t, u) -> heuristic.apply(t, u) * 1000000);
	}

}
