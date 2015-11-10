package algorithms;

import java.util.function.Function;

import core.State;

public class BestFirst {
	
	// class to practice Java Lambda expressions
	public static final void search(Search search, Function<State, Double> heuristic) {
		Function<State, Double> original = heuristic;
		heuristic = (State state) -> {return original.apply(state) * 1000000;};
		
		AStar.search(search, heuristic);
	}
}
