package algorithms;

import java.awt.Point;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

import core.Node;
import core.State;

public class DepthFirst {

	public static final void search(Search search, Function<State, Double> heuristic) {
		State startState = search.getStartState();

		Stack<Point> solution = search.getSolution();
		Set<Point> opened = search.getOpened();
		Set<Point> closed = search.getClosed();
		
		Stack<Node> stack = new Stack<Node>();
		stack.add(new Node(null, startState, 0, 0));
		
		long startTime = System.nanoTime();
		int nodesProcessed = 0;
	
		while (!stack.isEmpty()) {
			Node node = stack.pop();
			State state = node.getState();
			Point point = state.getStart();
			
			opened.remove(state.getStart());
			closed.add(state.getStart());
						
			nodesProcessed++;
			
			if (point.equals(state.getGoal())) {
				System.out.print("Search complete in : " + (System.nanoTime() - startTime) / 1000000f + "ms");
				System.out.print("\t");
				System.out.println("Nodes processed: " + nodesProcessed + "\tMemory: " + stack.size());

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
				
				opened.add(p);
				stack.push(n);
			}
		}
		
		System.out.println("no solution found!");
	}
}
