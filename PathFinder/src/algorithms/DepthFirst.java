package algorithms;

import java.awt.Point;
import java.util.Stack;
import java.util.function.BiFunction;

import core.Node;

public class DepthFirst extends Search {

	// ==== Search Method ====

	public static final void search(Search search) {
		final Stack<Node> stack = new Stack<Node>();
		stack.push(new Node(grid.getStart(), null));

		while (!stack.isEmpty()) {
			final long startTime = System.nanoTime();
			
			Node node = stack.pop();
			Point point = node.data;

			nodesProcessed++;

			if (point.equals(grid.getGoal())) {
				backtrace(node);
				return;
			}
			
			if (!closed.add(point))
				continue;
			
			opened.remove(point);

			for (Point p : grid.expand(point, diagonalMovement)) {
				if (closed.contains(p))
					continue;
				
				stack.add(new Node(p, node));
				opened.add(p);
			}
			
			timeElapsed += System.nanoTime() - startTime;
			
			if (Search.sleepTime > 0)
				try {
					Thread.sleep(Search.sleepTime);
				} catch (InterruptedException e) {
					break;
				}
		}
	}

}
