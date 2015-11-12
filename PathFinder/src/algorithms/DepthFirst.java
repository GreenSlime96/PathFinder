package algorithms;

import java.awt.Point;
import java.util.Stack;
import java.util.function.BiFunction;

import core.Node;

public class DepthFirst extends GenericSearch {

	// ==== Search Method ====

	public static final void search(Search search, BiFunction<Integer, Integer, Double> heuristic) {
		final Stack<Node> stack = new Stack<Node>();
		stack.push(new Node(grid.getStart(), null));

		startTime = System.nanoTime();
		nodesProcessed = 0;

		while (!stack.isEmpty()) {
			Node node = stack.pop();
			Point point = node.data;

			nodesProcessed++;

			if (point.equals(grid.getGoal())) {
				backtrace(node);
				return;
			}
			
			if (closed.contains(point))
				continue;
			
			closed.add(point);
			opened.remove(point);

			for (Point p : grid.expand(point, diagonalMovement)) {
				if (closed.contains(p))
					continue;
				
				stack.add(new Node(p, node));
				opened.add(p);
			}
			
			if (GenericSearch.sleepTime > 0)
				try {
					Thread.sleep(GenericSearch.sleepTime);
				} catch (InterruptedException e) {
					break;
				}
		}
	}

}
