package algorithms;

import java.awt.Point;
import java.util.List;
import java.util.Stack;

import core.Grid;
import core.Node;

public class DepthFirst extends Search {

	// ==== Search Method ====

	public List<Point> search(int startX, int startY, int endX, int endY, Grid grid) {
		final Stack<Node> stack = new Stack<Node>();
		
		final Node startNode = grid.getNodeAt(startX, startY);
		final Node endNode = grid.getNodeAt(endX, endY);
		
		stack.push(startNode);
		startNode.open();

		while (!stack.isEmpty()) {
			final long startTime = System.nanoTime();
			
			Node node = stack.pop();
			
			nodesProcessed++;

			if (node == endNode) {
				return backtrace(node);
			}
			
			if (node.closed())
				continue;
			
			node.close();

			for (Node n : grid.expand(node, diagonalMovement)) {
				if (node.closed())
					continue;
				
				stack.add(n);
				node.open();
			}
			
			timeElapsed += System.nanoTime() - startTime;
			
			if (Search.sleepTime > 0)
				try {
					Thread.sleep(Search.sleepTime);
				} catch (InterruptedException e) {
					break;
				}
		}
		
		return null;
	}

}
