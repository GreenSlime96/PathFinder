package core;

import java.awt.Point;

// ==== Node Implementation ====

// TODO: do we need depth class?
// TODO: can we make Node abstract?
// TODO: does it need to implement Comparable?

public class Node implements Comparable<Node> {
	public final Node parent;
	public final Point data;	
	public final int depth;
	
	public double f, g, h;
	
	public Node(Node parent, Point data, int depth) {
		this.parent = parent;
		this.data = data;
		this.depth = depth;
	}

	// ==== Object Overrides ====

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (!(o instanceof Node))
			return false;

		return ((Node) o).data.equals(data);
	}

	@Override
	public String toString() {
		return this.data.toString();
	}

	// ==== Comparable Implementation ====

	@Override
	public int compareTo(Node o) {
		if (this.f > o.f)
			return 1;
		else if (this.f < o.f)
			return -1;
		
		return 0;
	}		
}