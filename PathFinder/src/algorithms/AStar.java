package algorithms;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BiFunction;

import core.Node;
import core.Grid;

public class AStar {

	// ==== Constants ====

	public static final double SQRT_2 = Math.sqrt(2);

	public static final void search(Search search, BiFunction<Integer, Integer, Double> heuristic) {
		final Grid grid = search.getStartState();
		final int diagonalMovement = search.getDiagonalMovement();

		final PriorityQueue<Node> queue = new PriorityQueue<Node>();

		final Map<Point, Node> opened = new HashMap<Point, Node>();

//		final Set<Point> opened = search.getOpened();
		final Set<Point> closed = search.getClosed();

		final Point start = grid.getStart();
		final Point goal = grid.getGoal();

		long startTime = System.nanoTime();
		int nodesProcessed = 0;
		int weight = 1;

		queue.add(new Node(null, start, 0, 0));

		while (!queue.isEmpty()) {
			Node node = queue.poll();
			Point point = node.getData();

			nodesProcessed++;

			opened.remove(point);
			closed.add(point);

			if (point.equals(goal)) {
				System.out.println("Time:\t\t" + (System.nanoTime() - startTime) / 1000000f + "ms");
				System.out.println("Operations:\t" + nodesProcessed);
				System.out.println("Memory:\t\t" + queue.size());

				double distance = 0;

				while (node != null) {
					if (node.getParent() != null)
						distance += node.getData().distance(node.getParent().getData());
					search.getSolution().push(node.getData());
					node = node.getParent();
				}
				
				search.getOpened().addAll(opened.keySet());

				System.out.println("Distance:\t" + distance);
				System.out.println("-----");

				return;
			}

			for (Point p : grid.expand(point, diagonalMovement)) {
				if (closed.contains(p))
					continue;

				int dx = Math.abs(p.x - goal.x);
				int dy = Math.abs(p.y - goal.y);

				double ng = node.g + ((p.x - point.x == 0 || p.y - point.y == 0) ? 1 : SQRT_2);
				double f, g, h;

				if (!opened.containsKey(p)) {
					g = Double.MAX_VALUE;
				} else {
					g = opened.get(p).g;
				}

				if (!opened.containsKey(p) || ng < g) {
					g = ng;
					h = weight * heuristic.apply(dx, dy);
					f = g + h;

					Node n = new Node(node, p, node.depth + 1, f);

					n.f = f;
					n.g = g;
					n.h = h;

					if (opened.containsKey(p)) 
						queue.remove(n);

					opened.put(p, n);
					queue.add(n);

					nodesProcessed++;
				}
			}
		}
		
		System.out.println("no solution found!");
	}
}
