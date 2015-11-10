package algorithms;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import core.DiagonalMovement;
import core.Heuristic;
import core.Node;
import core.State;

public class Search {
	
	// ==== Properties ====
	
	private final Set<Point> opened;
	private final Set<Point> closed;
	private final Stack<Point> solution;
	
	private State startState;
	private Thread thread;
	
	private int diagonalMovement;
	private int searchHeuristic;
	private int searchAlgorithm;
	
	// ==== Constructor ====
	
	public Search(Set<Point> opened, Set<Point> closed, Stack<Point> solution) {
		this.opened = opened;
		this.closed = closed;
		this.solution = solution;
	}
	
	// ==== Accessors ====
	
	public State getStartState() {
		return startState;
	}
	
	public void setStartState(State startState) {
		this.startState = startState;
	}
	
	public Set<Point> getOpened() {
		return opened;
	}
	
	public Set<Point> getClosed() {
		return closed;
	}
	
	public Stack<Point> getSolution() {
		return solution;
	}
	
	public int getDiagonalMovement() {
		return diagonalMovement;
	}
	
	public void setDiagonalMovement(int diagonalMovement) {
		if (diagonalMovement < 1 || diagonalMovement > 4)
			return;
		
		this.diagonalMovement = diagonalMovement;
	}
	
	public int getSearchHeuristic() {
		return searchHeuristic;
	}
	
	public void setSearchHeuristic(int searchHeuristic) {
		this.searchHeuristic = searchHeuristic;
	}
	
	public int getSearchAlgorithm() {
		return searchAlgorithm;
	}
	
	public void setSearchAlgorithm(int searchAlgorithm) {
		this.searchAlgorithm = searchAlgorithm;
	}
	
	// ==== Public Helper Methods ====
	
	public boolean isActive() {
		return thread != null && thread.isAlive();
	}
	
	public void stop() {
		if (!isActive())
			return;
		
		thread.interrupt();
		
		try {
			thread.join();
		} catch (InterruptedException e) { }
	}
	
	public void start() {
		Search search = this;
		
		thread = new Thread() {
			@Override
			public void run() {
				switch (searchAlgorithm) {
				case 0:
					AStar.search(search);
					break;
				case 1:
					BreadthFirst.search(search);
					break;
				case 2:
					DepthFirst.search(search);
					break;
				}
			}
		};
		
		thread.start();
	}
	
	public final double heuristic(State state) {
		switch (searchHeuristic) {
		case Heuristic.MANHATTAN_DISTANCE:
			return Heuristic.manhattanDistance(state);
		case Heuristic.EUCLIDEAN_DISTANCE:
			return Heuristic.euclideanDistance(state);
		default:
			return Heuristic.manhattanDistance(state);
		}
	}
	
	public final List<Node> expand(Node node) {
		final List<Point> points = expand(node.getState());
		final List<Node> nodes = new ArrayList<Node>(points.size());
			
		final Dimension dimension = startState.getDimension();
		final Set<Point> walls = startState.getWalls();
		final Point goal = startState.getGoal();
		
		final int depth = node.getDepth();

		for (Point point : points) {
			State state = new State(dimension, point, goal, walls);
			nodes.add(new Node(node, state, depth + 1, depth + heuristic(state)));
		}
		
		return nodes;
	}
	
	public final List<Point> expand(State state) {
		final List<Point> points = new ArrayList<Point>(8);
		final Point point = state.getStart();
		
		boolean s0, s1, s2, s3;
		boolean d0, d1, d2, d3;
		
		s0 = s1 = s2 = s3 = false;
		d0 = d1 = d2 = d3 = false;
		
		// ↑
		if (state.isWalkable(point.x, point.y - 1)) {
			points.add(new Point(point.x, point.y - 1));
			s0 = true;
		}
		
		// →
		if (state.isWalkable(point.x + 1, point.y)) {
			points.add(new Point(point.x + 1, point.y));
			s1 = true;
		}
		
		// ↓
		if (state.isWalkable(point.x, point.y + 1)) {
			points.add(new Point(point.x, point.y + 1));
			s2 = true;
		}
		
		// ←
		if (state.isWalkable(point.x - 1, point.y)) {
			points.add(new Point(point.x - 1, point.y));
			s3 = true;
		}
				
		if (diagonalMovement == DiagonalMovement.NEVER)
			return points;
		
	    if (diagonalMovement == DiagonalMovement.ONLY_WHEN_NO_OBSTACLES) {
	        d0 = s3 && s0;
	        d1 = s0 && s1;
	        d2 = s1 && s2;
	        d3 = s2 && s3;
	    } else if (diagonalMovement == DiagonalMovement.IF_AT_MOST_ONE_OBSTACLE) {
	        d0 = s3 || s0;
	        d1 = s0 || s1;
	        d2 = s1 || s2;
	        d3 = s2 || s3;
	    } else if (diagonalMovement == DiagonalMovement.ALWAYS) {
	        d0 = true;
	        d1 = true;
	        d2 = true;
	        d3 = true;
	    } else {
	        throw new IllegalArgumentException("incorrect diagonal parameter");
	    }
	    
	    // ↖
	    if (d0 && state.isWalkable(point.x - 1, point.y - 1))
	    	points.add(new Point(point.x - 1, point.y - 1));
	    
	    // ↗
	    if (d1 && state.isWalkable(point.x + 1, point.y - 1))
	    	points.add(new Point(point.x + 1, point.y - 1));
	    
	    // ↘
	    if (d2 && state.isWalkable(point.x + 1, point.y + 1))
	    	points.add(new Point(point.x + 1, point.y + 1));
	    
	    // ↙
	    if (d3 && state.isWalkable(point.x - 1, point.y + 1))
	    	points.add(new Point(point.x - 1, point.y + 1));
	    		
//	    Collections.shuffle(points);
		return points;
	}

}
