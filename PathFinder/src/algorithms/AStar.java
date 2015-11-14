package algorithms;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import core.Node;

public class AStar extends Search {

	// ==== Constants ====

	public static final double SQRT_2 = Math.sqrt(2);

	// ==== Public Static Methods ====

	public static final void search(Search search) {
		final PriorityQueue<Node> queue = new PriorityQueue<Node>();
		final Map<Point, Node> map = new HashMap<Point, Node>();

		queue.add(new Node(new Point(grid.start), null));

		while (!queue.isEmpty()) {
			final long startTime = System.nanoTime();
			
			nodesProcessed++;

			Node node = queue.poll();
			Point point = node.data;

			opened.remove(point);
			closed.add(point);

			if (point.equals(grid.goal)) {
				backtrace(node);
				return;
			}

			for (Point p : grid.expand(point, diagonalMovement)) {
				if (closed.contains(p))
					continue;
				
				double ng = node.g + ((p.x - point.x == 0 || p.y - point.y == 0) ? 1 : SQRT_2);
				double f, g, h;

				// store this in a boolean so we need not do multiple operations
				final boolean contains = !opened.add(p);
				final Node n;

				if (contains)
					n = map.get(p);
				else
					n = new Node(p, node);

				if (!contains || ng < n.g) {
					nodesProcessed++;
					
					final int dx = Math.abs(p.x - grid.goal.x);
					final int dy = Math.abs(p.y - grid.goal.y);

					g = ng;
					h = heuristic.apply(dx, dy);
					f = g + h;

					n.f = f;
					n.g = g;
					n.h = h;

					if (contains)
						queue.remove(n);
					else
						map.put(p, n);

					n.parent = node;
					queue.add(n);

				}
			}

			timeElapsed += System.nanoTime() - startTime;

			if (Search.sleepTime > 0)
				try {
					Thread.sleep(Search.sleepTime);
				} catch (InterruptedException e) {
					break;
				}
		}

		System.out.println("no solution found!");
	}
}
