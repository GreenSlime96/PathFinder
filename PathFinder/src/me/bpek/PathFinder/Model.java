package me.bpek.PathFinder;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import javax.swing.Timer;

public class Model extends Observable implements ActionListener {
	
	// ==== Properties ====
	
	private Dimension dimension;
	private State state;
	
	private final Timer timer = new Timer(50, this);
	private final Vector<Thread> threads = new Vector<Thread>();
	private final Set<Point> opened = new HashSet<Point>();
	private final Set<Point> closed = new HashSet<Point>();
	private final Stack<Point> solution = new Stack<Point>();
	
	private boolean active;
	
	private Point start;
	private Point goal;
	private Set<Point> walls;	
	
	// ==== Constructor ====
	
	public Model() {
		super();
		
		setActive(false);
		setSize(new Dimension(1, 1));
		setActive(true);
	}
	
	
	// ==== Accessors ====
	
	public Dimension getSize() {
		return dimension;
	}
	
	public void setSize(Dimension dimension) {
		if (dimension == null || !dimension.equals(this.dimension)) {
			this.dimension = dimension;
			
			final int midX = (dimension.width - 1) / 2;
			final int midY = (dimension.height - 1) / 2;
			
			start = new Point(midX - 5, midY);
			goal = new Point(midX + 5, midY);
			
			walls = new HashSet<Point>();
			
			setChanged();
			notifyObservers();
		}
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Point getStart() {
		return start;
	}
	
	public void setStart(Point start) {
		if (isValidPoint(start))
			this.start = start;
	}
	
	public Point getGoal() {
		return goal;
	}
	
	public void setGoal(Point goal) {
		if (isValidPoint(goal))
			this.goal = goal;
	}
	
	public Set<Point> getWalls() {
		return walls;
	}
	
	public void addWall(Point point) {
		// TODO: checking for contains before adds is "less" efficient
		if (isValidPoint(point))
			walls.add(point);
	}
	
	public void clearWall(Point point) {
		walls.remove(point);
	}
	
	public void clearWalls() {
		stopSearching();
		
		opened.clear();
		closed.clear();
		solution.clear();
		
		walls.clear();
		
		setChanged();
		notifyObservers();
	}
	
	public Set<Point> getOpened() {
		return opened;
	}
	
	public Set<Point> getClosed() {
		return closed;
	}
	
	public void startSearch() {
		stopSearching();
		startSearching();
	}
	
	public Stack<Point> getSolution() {
		return solution;
	}
	
	// ==== ActionListener Implementation ====
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		if (e.getSource() == timer) {
			setChanged();
			notifyObservers();
			
			for (Thread thread : threads) {
				if (thread.isAlive())
					return;
			}
			
			timer.stop();
		}		
	}
	
	// ==== Private Helper Methods ====
	
	private boolean isValidPoint(Point point) {
//		return !(point.getX() < 0 ||
//				point.getX() >= dimension.getWidth() ||
//				point.getY() < 0 ||
//				point.getY() >= dimension.getHeight());
		
		// TODO: the above tests are less demanding, use them?
		
		// if the point exists somewhere
		if (start.equals(point) || goal.equals(point) || walls.contains(point))
			return false;
		
		// one less operator
		return (point.getX() >= 0 && 
				point.getX() < dimension.getWidth() &&
				point.getY() >= 0 &&
				point.getY() < dimension.getHeight());
	}
	
	private void stopSearching() {
		// tell all threads to stop
		for (Thread thread : threads) {
			thread.interrupt();
		}
		
		// wait for threads to finish
		for (Thread thread : threads) {
			while (thread.isAlive()) {
				try {
					thread.join();
				} catch (InterruptedException e) { }
			}
		}
		
		// clear the list of threads
		threads.clear();
		
		// stop sending updates
		timer.stop();
	}
	
	private void startSearching() {
		if (active) {
			timer.start();
			
			opened.clear();
			closed.clear();
			solution.clear();
			
			// TODO: implement multithreading
			Thread thread = new Thread(new Search());
			thread.start();
			threads.add(thread);
		}
		
	}

	// ==== Search Thread ====
	
	private class Search implements Runnable {
		
		@Override
		public void run() {
			State startState = new State(dimension, start, goal, walls);			
			PriorityQueue<Node> queue = new PriorityQueue<Node>();
			
			queue.add(new Node(null, startState, 0, 0));
			
			long startTime = System.currentTimeMillis();

			int counter = 0;
			
			while (!queue.isEmpty()) {
				Node node = queue.poll();
				State state = node.getState();
				
				if (state.getStart().equals(state.getGoal())) {
					System.out.println("Search complete in : " + (System.currentTimeMillis() - startTime) + "ms\tNodes processed: " + counter + "\tMemory: " + queue.size());

					while (node != null) {
						solution.push(node.getState().getStart());
						node = node.getParent();
					}
					
					return;
				}
				
				opened.remove(state.getStart());
				closed.add(state.getStart());
				
				counter ++;
				
				List<Node> nodes = expand(node);				
				queue.addAll(nodes);
				
				for (Node n : nodes) {
					opened.add(n.getState().getStart());
				}
				
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
				}
			}
		}
		
		// TODO: take this out of here!
		// ==== Expands Implementation ====
		
		private List<Node> expand(Node node) {
			List<Node> list = new ArrayList<Node>(4);
			
			State state = node.getState();			
			int depth = node.getDepth();
			
			for (int i = 0; i < 4; i++) {
				Point newPoint = new Point(state.getStart());
				State newState = null;
				
				switch (i) {
				case 0:
					newPoint.translate(0, -1);
					break;
				case 1:
					newPoint.translate(0, 1);
					break;
				case 2:
					newPoint.translate(-1, 0);
					break;
				case 3:
					newPoint.translate(1,  0);
					break;
				}
				
				if (closed.contains(newPoint))
					continue;
								
				if (isValidPoint(newPoint) || newPoint.equals(state.getGoal())) {
					newState = new State(dimension, newPoint, goal, walls);
					
					list.add(new Node(node, newState, depth + 1, depth + manhattanDistance(newState)));
				}		
			}
			
			return list;
		}
		
		private int manhattanDistance(State state) {
			Point start = state.getStart();
			Point goal = state.getGoal();
			
			return (Math.abs(start.x - goal.x) + Math.abs(start.y - goal.y));
		}
		
		// TODO: take this out of here!
		// ==== Node Implementation ====
		
		private class Node implements Comparable<Node> {
			private Node parent;
			private State state;
			private int depth;
			private int heuristic;

			public Node(Node parent, State state, int depth, int heuristic) {
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

			public int getHeuristic() {
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
				return this.heuristic - o.heuristic;
			}			
		}

	}
}
