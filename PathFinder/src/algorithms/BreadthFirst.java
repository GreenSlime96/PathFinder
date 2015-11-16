package algorithms;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import core.Grid;
import core.Node;

public class BreadthFirst extends Search {
	
	// ==== Search Method ====
	
	public List<Point> search(int startX, int startY, int endX, int endY, Grid grid) {
		final Queue<Node> queue = new LinkedList<Node>();
		
		final Node startNode = grid.getNodeAt(startX, startY);
		final Node endNode = grid.getNodeAt(endX, endY);
		
		queue.add(startNode);
		startNode.open();

		while (!queue.isEmpty()) {
			final long startTime = System.nanoTime();
			
			Node node = queue.poll();
			node.close();

			nodesProcessed++;
			
			if (node == endNode) {								
				return backtrace(node);
			}

			for (Node n : grid.expand(node, diagonalMovement)) {				
				// if either closed or opened sets contain
				if (n.closed() || n.opened())
					continue;
				
				nodesProcessed++;

				queue.add(n);
				
				n.parent = node;
				n.open();
			}
			
			timeElapsed += System.nanoTime() - startTime;
			
			if (Search.sleepTime > 0)
				try {
					Thread.sleep(Search.sleepTime);
				} catch (InterruptedException e) {
					break;
				}
		}
		
		System.out.println("no solution found");
		return null;
	}

}
