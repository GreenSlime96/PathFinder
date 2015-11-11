package algorithms;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Supplier;

import core.Node;
import core.State;

public class AStar {
	
	public static final void search(State grid) {
		
		final PriorityQueue<Node> queue = new PriorityQueue<Node>();
		
		final Set<Point> opened = new HashSet<Point>();
		final Set<Point> closed = new HashSet<Point>();
		
		final Point startNode = grid.getStart();
		final Point endNode = grid.getGoal();

		queue.add(new Node());
		
		while (!queue.isEmpty()) {
			
		}


		System.out.println("no solution found!");
	}
	
	static class Node<T> {
		double f, g, h;
		T data;
		
	}
}
