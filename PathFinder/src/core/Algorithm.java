package core;

import java.awt.Point;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

import algorithms.Search;

public class Algorithm {
	
	// ==== Constants ====
	
	public static final int A_STAR = 0;
	public static final int BREADTH_FIRST = 1;
	public static final int DEPTH_FIRST = 2;
	public static final int BEST_FIRST = 3;
	public static final int DIJKSTRA = 4;
	
	
	// ==== Static Methods ====
	
	public static final void AStar(Search search, Function<State, Double> heuristic) {
		State startState = search.getStartState();

		Stack<Point> solution = search.getSolution();
		Set<Point> opened = search.getOpened();
		Set<Point> closed = search.getClosed();

		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		queue.add(new Node(null, startState, 0, 0));

		long startTime = System.nanoTime();
		int nodesProcessed = 0;
		int counter = 0;

		while (!queue.isEmpty()) {
			Node node = queue.poll();
			State state = node.getState();
			Point point = state.getStart();

			opened.remove(point);
			closed.add(point);

			nodesProcessed++;

			if (point.equals(state.getGoal())) {
				System.out.print("Search complete in : " + (System.nanoTime() - startTime) / 1000000f + "ms");
				System.out.print("\t");
				System.out.println("Nodes processed: " + nodesProcessed + "\tMemory: " + queue.size() + "\t" + counter);

				while (node != null) {
					solution.push(node.getState().getStart());
					node = node.getParent();
				}

				return;
			}

			List<Point> points = search.expand(state);

			for (Point p : points) {
				if (closed.contains(p) || opened.contains(p))
					continue;
				
//				// TODO: convert to Functional Programming!
//				Node n = new Node(node, )

				opened.add(p);
//				queue.add(n);
			}
		}

		System.out.println("no solution found!");
	}
}
