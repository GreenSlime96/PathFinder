package algorithms;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import core.Node;
import core.State;

public class AStar {

	// TODO: the whole thing is incomplete! :(
	public static final void search(Search search) {
		State startState = search.getStartState();
		
		Map<Point, Double> testMap = new HashMap<Point, Double>();

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

				if (closed.contains(p))
					continue;
				
	            // get the distance between current node and the neighbor
	            // and calculate the next g score
	            double ng = node.getDepth() + ((p.x - point.x == 0 || p.y - point.y == 0) ? 1 : Math.sqrt(2));

				if (!opened.contains(p) || ng < n.getDepth()) {

					if (!opened.contains(p)) {
						testMap.put(p, n.getHeuristic());
						opened.add(p);
						queue.add(n);
					} else {
						System.out.println("we here bro");
					}


				}
				
				if (n.getHeuristic() < testMap.get(p)) {
					System.out.println("hmmm");
					testMap.put(p, n.getHeuristic());
				}
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("no solution found!");
	}
}
