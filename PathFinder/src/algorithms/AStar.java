package algorithms;

import java.awt.Point;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

import core.Node;
import core.State;

public class AStar {
	
	// TODO: the whole thing is incomplete! :(
	public static final void search(Search search, Function<State, Double> heuristic) {
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

			List<Node> nodes = search.expand(node);

			for (Node n : nodes) {
				Point p = n.getState().getStart();
				
//				if (n.getHeuristic() != node.getDepth() + heuristic.apply(n.getState()))
//					System.err.println("we have problem");

				if (closed.contains(p) || opened.contains(p))
					continue;

				opened.add(p);
				queue.add(n);
			}
		}

		System.out.println("no solution found!");
	}
}
