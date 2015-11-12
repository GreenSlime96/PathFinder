package algorithms;

import java.awt.Point;
import java.util.Set;
import java.util.Stack;

import core.Grid;
import core.Node;

public abstract class GenericSearch {
	
	// ==== Static Variables ====
	
	public static Set<Point> opened, closed;
	public static Stack<Point> solution;
	
	public static int diagonalMovement;
	public static int nodesProcessed;
	
	public static long startTime;
	
	public static Grid grid;
	
	// ==== Utility Methods ====
	
	public static final void backtrace(Node node) {
		System.out.println("Time:\t\t" + (System.nanoTime() - startTime) / 1000000f + "ms");
		System.out.println("Operations:\t" + nodesProcessed);

		double distance = 0;

		while (node != null) {
			if (node.parent != null)
				distance += node.data.distance(node.parent.data);
			solution.push(node.data);
			node = node.parent;
		}

		System.out.println("Distance:\t" + distance);
		System.out.println();
	}
}
