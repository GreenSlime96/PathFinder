package core;

import java.awt.Point;

import algorithms.Search;

// ==== Node Implementation ====

public class Node implements Comparable<Node> {	
	
	// ==== Constants ====
	
	static final int BIT_WALKABLE = 0;
	static final int BIT_OPENED = 1;
	static final int BIT_CLOSED = 2;
	static final int BIT_OPENED_BY = 3;
	
	// ==== Properties ====
	
	// the real data of the node
	public final int x, y;
	
	// whether or not the node can be "walked"
	public boolean walkable;
	
	// heuristics of the node
	public double f, g, h;

	// whether or not the node has been opened or not
	public int opened, closed;
	
	// the current node's parent
	public Node parent;
	
	// bit: property
	// 0: walkable
	// 1: opened
	// 2: closed
	// 3: opened_by
	private byte state = 0;
	
	// ==== Constructor ====
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Node(Point data, Node parent) {
		this(data.x, data.y);
		this.parent = parent;		
	}
	
	// ==== Helper Methods ====
	
	public void setWalkable(boolean walkable) {
		state = (byte) (walkable ? state | 1 << 0 : state & ~(1 << 0));
	}
	
	public boolean isWalkable() {
		return isBitSet(0);
	}
	
	public void close() {
		Search.nodesProcessed++;
		closed = 1;
	}
	
	public void open() {
		Search.nodesProcessed++;
		opened = 1;
	}
	
	public boolean closed() {
		return isBitSet(2);
	}
	
	public boolean opened() {
		return isBitSet(1);
	}
	
	// ==== Private Helper Methods ====
	
	private boolean isBitSet(int position) {
		return ((state >> position) & 1) == 1;
	}

	// ==== Object Overrides ====

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof Node))
			return false;

		Node other = (Node) obj;

		if (x != other.x)
			return false;

		if (y != other.y)
			return false;

		return true;
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