package core;

import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import algorithms.Search;

public class Algorithm {
	
	// ==== Constants ====
	
	public static final int A_STAR = 0;
	public static final int BREADTH_FIRST = 1;
	public static final int DEPTH_FIRST = 2;
	public static final int BEST_FIRST = 3;
	public static final int DIJKSTRA = 4;
	
	
	// ==== Static Methods ====
	
	public static final void AStar(Search search, BiFunction<Integer, Integer, Double> heuristic) {
		Collection<Node> struct = new PriorityQueue<Node>();
		Predicate<Point> predicate = ((p) -> (search.getClosed().contains(p) || search.getOpened().contains(p)));
		
		Option option = new Option(search, predicate, struct, heuristic);
		search(option);
	}
	
	public static final void BreadthFirst(Search search, BiFunction<Integer, Integer, Double> heuristic) {
		Collection<Node> struct = new LinkedList<Node>();
		Predicate<Point> predicate = ((p) -> (search.getClosed().contains(p) || search.getOpened().contains(p)));
		
		Option option = new Option(search, predicate, struct, (dx, dy) -> 0d);
		search(option);
	}
	
	public static final void DepthFirst(Search search, BiFunction<Integer, Integer, Double> heuristic) {
		Collection<Node> struct = new Stack<Node>();
		Predicate<Point> predicate = ((p) -> (search.getClosed().contains(p)));
		
		Option option = new Option(search, predicate, struct, (dx, dy) -> 0d);
		search(option);
	}
	
	// ==== Private Helper Methods ====
	
	private static final void search(final Option option) {
		
		// TODO: this looks UGLY here, could we perhaps move this elsewhere?
		// ==== Properties ====
		
		final BiFunction<Integer, Integer, Double> heuristic = option.heuristic;
		
		final Supplier<Node> getter = option.getter;
		final Consumer<Node> setter = option.setter;
		
		final Predicate<Point> predicate = option.predicate;
		final Collection<Node> struct = option.struct;
		
		final Grid grid = option.startState;
		final Point start = grid.getStart();
		final Point goal = grid.getGoal();

		final Stack<Point> solution = option.solution;
		final Set<Point> opened = option.opened;
		final Set<Point> closed = option.closed;
				
		final int diagonalMovement = option.diagonalMovement;
		
		// ==== The Algorithm! ====
		
		setter.accept(new Node(null, start, 0, 0));

		long startTime = System.nanoTime();
		int nodesProcessed = 0;

		while (!struct.isEmpty()) {
			Node node = getter.get();
			Point point = node.getData();

			opened.remove(point);
			closed.add(point);

			nodesProcessed++;

			if (point.equals(goal)) {
				System.out.print("Search complete in : " + (System.nanoTime() - startTime) / 1000000f + "ms");
				System.out.print("\t");
				System.out.println("Nodes processed: " + nodesProcessed + "\tMemory: " + struct.size());

				double distance = 0;
				
				while (node != null) {
					if (node.getParent() != null)
						distance += node.getData().distance(node.getParent().getData());
					solution.push(node.getData());
					node = node.getParent();
				}
				
				System.out.println(distance);
				
				return;
			}

			List<Point> points = grid.expand(point, diagonalMovement);
			
			for (Point p : points) {
				if (predicate.test(p))
					continue;
				
				// THE FOLLOWING ONLY APPLIES TO A_STAR
				// TODO: fuck this shit will deal tomorrow
				
				int dx = Math.abs(p.x - goal.x);
				int dy = Math.abs(p.y - goal.y);
				
				// TODO: move Heuristic.SQRT_2 elsewhere -_-
				double ng = node.g + ((p.x - point.x == 0 || p.y - point.y == 0) ? 1 : Heuristic.SQRT_2);
				
				// END
				
				Node n = new Node(node, p, node.getDepth() + 1, node.getDepth() + heuristic.apply(dx, dy));

				opened.add(p);				
				setter.accept(n);
			}
		}

		System.out.println("no solution found!");
	}
	
	static class Option {
		// determined by the Search implementation
		final BiFunction<Integer, Integer, Double> heuristic;
		
		// definitely provided
		final Predicate<Point> predicate;
		final Collection<Node> struct;
		
		// passed on by the Model
		final Search search;
		
		// determined by the Option class
		final Supplier<Node> getter;
		final Consumer<Node> setter;
		
		// other miscellaneous things
		final Stack<Point> solution;
		final Set<Point> opened;
		final Set<Point> closed; 
		
		final Grid startState;
		
		final int diagonalMovement;
		
		// TODO: implement proper heuristics grabbing
		public Option(Search search, Predicate<Point> predicate, Collection<Node> struct) {
			this(search, predicate, struct, Heuristic::manhattanDistance);
		}
		
		public Option(Search search, Predicate<Point> predicate, Collection<Node> struct, BiFunction<Integer, Integer, Double> heuristic) {
			this.heuristic = heuristic;
			this.predicate = predicate;
			this.struct = struct;
			this.search = search;
			
			diagonalMovement = search.getDiagonalMovement();
			
			startState = search.getStartState();

			solution = search.getSolution();
			opened = search.getOpened();
			closed = search.getClosed();
			
			if (struct instanceof Queue) {
				getter = ((Queue<Node>) struct)::poll;
				setter = ((Node x) -> ((Queue<Node>) struct).offer(x));
			} else if (struct instanceof Stack) {
				getter = ((Stack<Node>) struct)::pop;
				setter = ((Node x) -> ((Stack<Node>) struct).push(x));
			} else {
				throw new IllegalArgumentException(struct + " is not an accepted type");
			}
		}
		
	}
}
