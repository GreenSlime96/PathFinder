package core;

import java.awt.Point;

// TODO: parameterize, later
// ==== Node Implementation ====

public class Node implements Comparable<Node> {
	private final Node parent;
	private final Point state;
	
	private final int depth;
	
	private final double heuristic;

	public Node(Node parent, Point state, int depth, double heuristic) {
		this.parent = parent;
		this.state = state;
		this.depth = depth;
		this.heuristic = heuristic;
	}

	// ==== Accessors ====

	public Node getParent() {
		return parent;
	}

	public Point getState() {
		return state;
	}

	public int getDepth() {
		return depth;
	}

	public double getHeuristic() {
		return heuristic;
	}

	// ==== Object Overrides ====

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (!(o instanceof Node))
			return false;

		return ((Node) o).state.equals(this.state);
	}

	@Override
	public String toString() {
		return this.state.toString();
	}

	// ==== Comparable Implementation ====

	@Override
	public int compareTo(Node o) {
		if (this.heuristic > o.heuristic)
			return 1;
		else if (this.heuristic < o.heuristic)
			return -1;
		
		return 0;
	}		
}