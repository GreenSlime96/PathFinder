package core;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;

public class State {
	
	// ==== Properties ====
	
	private final Dimension dimension;
	
	private final Point start;
	private final Point goal;
	
	private final Set<Point> walls;	
	
	// ==== Constructor ====
	
	public State(Dimension dimension, Point start, Point goal, Set<Point> walls) {
		this.dimension = dimension;
		this.start = start;
		this.goal = goal;
		this.walls = walls;

		if (!isValidPoint(start))
			throw new IllegalArgumentException(start + " not in " + dimension);
		
		if (!isValidPoint(goal))
			throw new IllegalArgumentException(goal + " not in " + dimension);
		
		for (Point p : walls)
			if (!isValidPoint(p))
				throw new IllegalArgumentException(p + " not in " + dimension);
	}
	
	
	// ==== Private Helper Methods ====
	
	private boolean isValidPoint(Point point) {
//		return !(point.getX() < 0 ||
//				point.getX() >= dimension.getWidth() ||
//				point.getY() < 0 ||
//				point.getY() >= dimension.getHeight());
		
		// one less operator
		return (point.getX() >= 0 && 
				point.getX() < dimension.getWidth() &&
				point.getY() >= 0 &&
				point.getY() < dimension.getHeight());
	}
	
	
	// ==== Accessors ====
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public Point getStart() {
		return start;
	}
	
	public Point getGoal() {
		return goal;
	}
	
	public Set<Point> getWalls() {
		return walls;
	}
}
