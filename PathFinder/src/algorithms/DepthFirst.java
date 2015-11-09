package algorithms;

import java.awt.Point;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import core.Node;
import core.State;

public class DepthFirst {

	public static final void search(Search search) {
		State startState = search.getStartState();

		Stack<Point> solution = search.getSolution();
		Set<Point> opened = search.getOpened();
		Set<Point> closed = search.getClosed();
		
		Stack<Node> stack = new Stack<Node>();
		stack.add(new Node(null, startState, 0, 0));
		
		long startTime = System.currentTimeMillis();
		int nodesProcessed = 0;
	
		while (!stack.isEmpty()) {
			Node node = stack.pop();
			State state = node.getState();
			
			if (state.getStart().equals(state.getGoal())) {
				System.out.print("Search complete in : " + (System.currentTimeMillis() - startTime) + "ms");
				System.out.print("\t");
				System.out.println("Nodes processed: " + nodesProcessed + "\tMemory: " + stack.size());

				while (node != null) {
					solution.push(node.getState().getStart());
					node = node.getParent();
				}
				
				return;
			}
			
			opened.remove(state.getStart());
			closed.add(state.getStart());
						
			nodesProcessed++;
			
			List<Node> nodes = search.expand(node);
			
			for (Node n : nodes) {
				if (closed.contains(n.getState().getStart()))
					continue;
				
				opened.add(n.getState().getStart());
				stack.add(n);
			}
		}
		
		System.out.println("no solution found!");
	}
}
