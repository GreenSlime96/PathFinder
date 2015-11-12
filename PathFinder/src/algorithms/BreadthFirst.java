package algorithms;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiFunction;

import core.Node;

public class BreadthFirst extends GenericSearch {
	
	// ==== Search Method ====
	
	public static final void search(Search search, BiFunction<Integer, Integer, Double> heuristic) {
		final Queue<Node> queue = new LinkedList<Node>();
		queue.add(new Node( null, grid.getStart(), 0));

		while (!queue.isEmpty()) {
			Node node = queue.poll();
			Point point = node.data;

			nodesProcessed++;
			
			opened.remove(point);
			closed.add(point);
			
			if (point.equals(grid.getGoal())) {
				backtrace(node);				
				return;
			}

			for (Point p : grid.expand(point, diagonalMovement)) {
				
				// if either closed or opened sets contain
				if (closed.contains(p) || !opened.add(p))
					continue;

				queue.add(new Node(node, p, node.depth + 1));
			}
			
			if (queue.isEmpty()) {
				System.out.println(node.data);
				return;
			}
		}
	}

}
