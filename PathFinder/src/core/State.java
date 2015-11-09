package core;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;

public class State {
	
	// ==== Properties ====
	
	private final Dimension dimension;
	
	private final Set<Point> walls;	
	
	private final Point start;
	private final Point goal;
		
	// ==== Constructor ====
	
	public State(Dimension dimension, Point start, Point goal, Set<Point> walls) {
		this.dimension = dimension;
		this.start = start;
		this.goal = goal;
		this.walls = walls;

		if (!isWalkable(start))
			throw new IllegalArgumentException(start + " is not walkable");
		
		if (!isWalkable(goal))
			throw new IllegalArgumentException(goal + " is not walkable");
		
		for (Point p : walls)
			if (!isInside(p))
				throw new IllegalArgumentException(p + " not in " + dimension);
	}
	
	
	// ==== Public Helper Methods ====
	
	public boolean isWalkable(int x, int y) {
		Point point = new Point(x, y);
		return isWalkable(point);
	}
	
	public boolean isWalkable(Point point) {
		return isInside(point) && !(walls.contains(point));
	}
	
	public boolean isInside(Point point) {
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
