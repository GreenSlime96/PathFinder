package algorithms;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import core.Node;

public class AStar extends GenericSearch {

	// ==== Constants ====

	public static final double SQRT_2 = Math.sqrt(2);
	
	
	// ==== Public Static Methods ====

	public static final void search(Search search, BiFunction<Integer, Integer, Double> heuristic) {
		final PriorityQueue<Node> queue = new PriorityQueue<Node>();
		final Map<Point, Node> map = new HashMap<Point, Node>();

		startTime = System.nanoTime();
		nodesProcessed = 1;

		queue.add(new Node(grid.start, null));

		while (!queue.isEmpty()) {
			Node node = queue.poll();
			Point point = node.data;

			nodesProcessed++;

			opened.remove(point);
			closed.add(point);

			if (point.equals(grid.goal)) {
				backtrace(node);
				return;
			}

			for (Point p : grid.expand(point, diagonalMovement)) {
				if (closed.contains(p))
					continue;

				int dx = Math.abs(p.x - grid.goal.x);
				int dy = Math.abs(p.y - grid.goal.y);

				double ng = node.g + ((p.x - point.x == 0 || p.y - point.y == 0) ? 1 : SQRT_2);
				double f, g, h;

				if (!map.containsKey(p)) {
					g = Double.MAX_VALUE;
				} else {
					g = map.get(p).g;
				}

				if (opened.add(p) || ng < g) {
					g = ng;
					h = heuristic.apply(dx, dy);
					f = g + h;

					Node n = new Node(p, node);

					n.f = f;
					n.g = g;
					n.h = h;

					if (opened.contains(p))
						queue.remove(n);

					map.put(p, n);
					queue.add(n);
				}
			}
			
			if (GenericSearch.sleepTime > 0)
				try {
					Thread.sleep(GenericSearch.sleepTime);
				} catch (InterruptedException e) {
					break;
				}
		}

		System.out.println("no solution found!");
	}
}
