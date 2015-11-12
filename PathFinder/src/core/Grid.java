package core;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Grid {

	// ==== Properties ====

	public final Dimension dimension;

	public final Set<Point> walls;

	public final Point start;
	public final Point goal;

	// ==== Constructor ====

	public Grid(Dimension dimension, Point start, Point goal, Set<Point> walls) {
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
		final Point point = new Point(x, y);
		return isWalkable(point);
	}

	public boolean isWalkable(Point point) {
		return isInside(point) && !(walls.contains(point));
	}

	public boolean isInside(Point point) {
		return (point.getX() >= 0 && point.getX() < dimension.getWidth() && point.getY() >= 0
				&& point.getY() < dimension.getHeight());
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

	// ==== Static Methods ====

	public final List<Point> expand(final int diagonalMovement) {
		return expand(start, diagonalMovement);
	}

	public final List<Point> expand(final Point point, final int diagonalMovement) {
		final List<Point> points = new ArrayList<Point>(8);

		boolean s0, s1, s2, s3;
		boolean d0, d1, d2, d3;

		s0 = s1 = s2 = s3 = false;
		d0 = d1 = d2 = d3 = false;

		final int x = point.x;
		final int y = point.y;

		// ↑
		if (isWalkable(x, y - 1)) {
			points.add(new Point(x, y - 1));
			s0 = true;
		}

		// →
		if (isWalkable(x + 1, y)) {
			points.add(new Point(x + 1, y));
			s1 = true;
		}

		// ↓
		if (isWalkable(x, y + 1)) {
			points.add(new Point(x, y + 1));
			s2 = true;
		}

		// ←
		if (isWalkable(x - 1, y)) {
			points.add(new Point(x - 1, y));
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
		if (d0 && isWalkable(x - 1, y - 1))
			points.add(new Point(x - 1, y - 1));

		// ↗
		if (d1 && isWalkable(x + 1, y - 1))
			points.add(new Point(x + 1, y - 1));

		// ↘
		if (d2 && isWalkable(x + 1, y + 1))
			points.add(new Point(x + 1, y + 1));

		// ↙
		if (d3 && isWalkable(x - 1, y + 1))
			points.add(new Point(x - 1, y + 1));

		// TODO: shuffle or not to shuffle?
		// Collections.shuffle(points);
		return points;
	}
}
