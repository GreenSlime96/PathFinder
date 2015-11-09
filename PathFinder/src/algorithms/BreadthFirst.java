package algorithms;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import core.Node;
import core.State;

public class BreadthFirst {

	public static final void search(Search search) {
		State startState = search.getStartState();

		Stack<Point> solution = search.getSolution();
		Set<Point> opened = search.getOpened();
		Set<Point> closed = search.getClosed();

		Queue<Node> queue = new LinkedList<Node>();
		queue.add(new Node(null, startState, 0, 0));

		long startTime = System.nanoTime();
		int nodesProcessed = 0;

		while (!queue.isEmpty()) {
			Node node = queue.poll();
			State state = node.getState();

			opened.remove(state.getStart());
			closed.add(state.getStart());

			nodesProcessed++;

			if (state.getStart().equals(state.getGoal())) {
				System.out.print("Search complete in : " + (System.nanoTime() - startTime) / 1000000f + "ms");
				System.out.print("\t");
				System.out.println("Nodes processed: " + nodesProcessed + "\tMemory: " + queue.size());

				while (node != null) {
					solution.push(node.getState().getStart());
					node = node.getParent();
				}

				return;
			}

			List<Node> nodes = search.expand(node);

			for (Node n : nodes) {
				Point p = n.getState().getStart();

				if (closed.contains(p) || opened.contains(p))
					continue;

				opened.add(p);
				queue.add(n);
			}
		}

		System.out.println("no solution found!");
	}
}
