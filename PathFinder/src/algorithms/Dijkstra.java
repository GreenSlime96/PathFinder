package algorithms;

public class Dijkstra extends Search {

	public static final void search(Search search) {
		heuristic = (t, u) -> 0d;
		AStar.search(search);
	}
}
