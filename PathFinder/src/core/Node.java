package core;

// TODO: take this out of here!
// ==== Node Implementation ====

public class Node implements Comparable<Node> {
	private final Node parent;
	private final State state;
	
	private final int depth;
	
	private final double heuristic;

	public Node(Node parent, State state, int depth, double heuristic) {
		this.parent = parent;
		this.state = state;
		this.depth = depth;
		this.heuristic = heuristic;
	}

	// ==== Accessors ====

	public Node getParent() {
		return parent;
	}

	public State getState() {
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
		
//		return this.heuristic - o.heuristic;
	}			
}