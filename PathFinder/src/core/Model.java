package core;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.Stack;

import javax.swing.Timer;

import algorithms.Search;

public class Model extends Observable implements ActionListener {
	
	// ==== Properties ====
	
	private Dimension dimension;
	
	private final Timer timer = new Timer(50, this);
	
	private final Set<Point> opened = Collections.synchronizedSet(new LinkedHashSet<Point>());
	private final Set<Point> closed = Collections.synchronizedSet(new LinkedHashSet<Point>());
	private final Stack<Point> solution = new Stack<Point>();
	private final Search search = new Search(opened, closed, solution);

	private boolean active = true;
	private int diagonalMovement = DiagonalMovement.ALWAYS;
	private int searchAlgorithm = Algorithm.A_STAR;
	private int searchHeuristic = Heuristic.MANHATTAN_DISTANCE;
	
	private final Set<Point> walls = Collections.synchronizedSet(new HashSet<Point>());
	private final Point start = new Point();
	private final Point goal = new Point();
	
	// ==== Constructor ====
	
	public Model() {
		super();
		
		setActive(false);
		setSize(new Dimension(1, 1));
		setActive(true);
	}
	
	
	// ==== Accessors ====
	
	public synchronized final Dimension getSize() {
		return dimension;
	}
	
	public synchronized final void setSize(Dimension dimension) {
		if (this.dimension == null) {
			this.dimension = dimension;
			fit();
			return;
		}
		
		if (!dimension.equals(this.dimension)) {
			this.dimension = dimension;
			
			// this is fucking useful!
			walls.removeIf(p -> !isInside(p));
			
			// TODO: implement point repositioning
			if (!isInside(start) || !isInside(goal)) {
				fit();
			}
			
			setChanged();
			notifyObservers();
		}
	}
	
	public synchronized final void fit() {
		final int midX = (dimension.width - 1) / 2;
		final int midY = (dimension.height - 1) / 2;
		
		start.setLocation(midX - 5, midY);
		goal.setLocation(midX + 5, midY);
				
		setChanged();
		notifyObservers();
	}
	
	public synchronized final int getDiagonalMovement() {
		return diagonalMovement;
	}
	
	// TODO: implement error checking
	public synchronized final void setDiagonalMovement(int diagonalMovement) {
		if (diagonalMovement != DiagonalMovement.ALWAYS && 
			diagonalMovement != DiagonalMovement.NEVER &&
			diagonalMovement != DiagonalMovement.IF_AT_MOST_ONE_OBSTACLE &&
			diagonalMovement != DiagonalMovement.ONLY_WHEN_NO_OBSTACLES) {
			throw new IllegalArgumentException("diagonalMovement not recognised");
		}
		
		this.diagonalMovement = diagonalMovement;
	}
	
	public synchronized final int getSearchAlgorithm() {
		return searchAlgorithm;
	}
	
	// TODO: ref above
	public synchronized final void setSearchAlgorithm(int searchAlgorithm) {
		if (searchAlgorithm != Algorithm.A_STAR &&
			searchAlgorithm != Algorithm.BEST_FIRST	&& 
			searchAlgorithm != Algorithm.BREADTH_FIRST &&
			searchAlgorithm != Algorithm.DEPTH_FIRST && 
			searchAlgorithm != Algorithm.DIJKSTRA) {			
			throw new IllegalArgumentException("searchAlgorithm not recognised");
		}
		
		this.searchAlgorithm = searchAlgorithm;
	}
	
	public synchronized final int getSearchHeuristic() {
		return searchHeuristic;
	}
	
	public synchronized final void setSearchHeuristic(int searchHeuristic) {
		this.searchHeuristic = searchHeuristic;
	}
	
	public synchronized final boolean getActive() {
		return active;
	}
	
	public synchronized final void setActive(boolean active) {
		this.active = active;
	}
	
	public synchronized final Point getStart() {
		return start;
	}
	
	public synchronized final void setStart(Point start) {
		if (isUnoccupied(start))
			this.start.setLocation(start);
	}
	
	public synchronized final Point getGoal() {
		return goal;
	}
	
	public synchronized final void setGoal(Point goal) {
		if (isUnoccupied(goal))
			this.goal.setLocation(goal);
	}
	
	public synchronized final Set<Point> getWalls() {
		return walls;
	}
	
	public synchronized final void addWall(Point point) {
		// TODO: checking for contains before adds is "less" efficient
		if (isUnoccupied(point))
			walls.add(point);
	}
	
	public synchronized final void clearWall(Point point) {
		walls.remove(point);
	}
	
	public synchronized final void clearWalls() {
		stopSearching();
		
		clearSearches();
		
		walls.clear();
		
		setChanged();
		notifyObservers();
	}
	
	public synchronized final Set<Point> getOpened() {
		return opened;
	}
	
	public synchronized final Set<Point> getClosed() {
		return closed;
	}
	
	public synchronized final void startSearch() {
		stopSearching();
		startSearching();
	}
	
	public synchronized final void pauseSearch() {
		pauseSearching();
	}
	
	public synchronized final Stack<Point> getSolution() {
		return solution;
	}
	
	public synchronized final void generateMaze() {
		// move start and goals to opposite ends of the map
		goal.setLocation(dimension.width - 2, dimension.height - 2);
		start.setLocation(1, 1);
		
		clearWalls();
		
		for (int x = 0; x < dimension.width; x++) {
			for (int y = 0; y < dimension.height; y++) {
				Point point = new Point(x, y);
				
				if (isUnoccupied(point))
					walls.add(point);
			}
		}
		
		Stack<Point> stack = new Stack<Point>();
		Set<Point> visited = new HashSet<Point>();
		
		stack.push(start);
		
		new Thread(){
			@Override
			public void run() {
		while (!stack.isEmpty()) {
			Point exit = stack.pop();
			
			walls.remove(exit);
			visited.add(exit);

			List<Point> points = new ArrayList<Point>(4);
			
			for (int i = 0; i < 4; i++) {
				Point newPoint = new Point(exit);
				
				switch(i) {
				case 0:
					newPoint.translate(0, -2);
					break;
				case 1:
					newPoint.translate(2, 0);
					break;
				case 2:
					newPoint.translate(0, 2);
					break;
				case 3:
					newPoint.translate(-2, 0);
					break;
				}
				
				if (!isInside(newPoint))
					continue;
				
				points.add(newPoint);
			}
						
			Collections.shuffle(points);
			
			for (Point p : points) {
				if (!visited.contains(p)) {
					walls.remove(new Point((p.x + exit.x) / 2, (p.y + exit.y) / 2));
					visited.add(p);
					stack.push(p);
				}
			}
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			setChanged();
			notifyObservers();
		}
			}
		}.start();
		

	}
	
	// ==== ActionListener Implementation ====
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		if (e.getSource() == timer) {
			setChanged();
			notifyObservers();
			
			if (search.isActive())
				return;
			
			timer.stop();
		}		
	}
	
	// ==== Private Helper Methods ====
	
	public boolean isWalkable(int x, int y) {
		Point point = new Point(x, y);
		return isWalkable(point);
	}
	
	public boolean isWalkable(Point point) {
		return isInside(point) && !(walls.contains(point));
	}
	
	public boolean isUnoccupied(Point point) {
		return isWalkable(point) && !start.equals(point) && !goal.equals(point);
	}
	
	public boolean isInside(Point point) {
		return (point.getX() >= 0 && 
				point.getX() < dimension.getWidth() &&
				point.getY() >= 0 &&
				point.getY() < dimension.getHeight());
	}
	
	private void stopSearching() {
		search.stop();
		timer.stop();
	}
	
	private void clearSearches() {
		opened.clear();
		closed.clear();
		solution.clear();
	}
	
	private void pauseSearching() {
		
	}
	
	private void startSearching() {
		if (active) {
			timer.start();
			
			Grid grid = new Grid(dimension, start, goal, walls);
			
			search.setDiagonalMovement(diagonalMovement);
			search.setSearchAlgorithm(searchAlgorithm);
			search.setSearchHeuristic(searchHeuristic);
			search.setStartState(grid);
			
			clearSearches();
			
			search.start();
		}
		
	}
}
