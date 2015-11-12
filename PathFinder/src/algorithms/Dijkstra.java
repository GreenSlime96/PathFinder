package algorithms;

import java.util.function.BiFunction;

public class Dijkstra {

	public static final void search(Search search, BiFunction<Integer, Integer, Double> heuristic) {
		// return AStar, but this time discard the heuristic -- interested in g(x)
		AStar.search(search, (t, u) -> 0d);
	}
}
