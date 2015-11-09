package core;

import java.awt.Point;

public final class Heuristic {
	
	// ==== Constants ====
	
	public static final int MANHATTAN_DISTANCE = 0;
	public static final int EUCLIDEAN_DISTANCE = 1;
	public static final int OCTILE_DISTANCE = 2;
	public static final int CHEBYSHEV_DISTANCE = 3;
	
	public static final double SQRT_2 = Math.sqrt(2);

	
	// ==== Static Methods ====
	
	public static final double manhattanDistance(State state) {
		final Point start = state.getStart();
		final Point goal = state.getGoal();
		
		return Math.abs(start.x - goal.x) + Math.abs(start.y - goal.y);
	}
	
	public static final double euclideanDistance(State state) {
		final Point start = state.getStart();
		final Point goal = state.getGoal();	
		
		return start.distance(goal);
	}
	
	public static final double octileDistance(State state) {
		final double F = SQRT_2 - 1;
		
		final Point start = state.getStart();
		final Point goal = state.getGoal();		

		final int dx = Math.abs(start.x - goal.x);
		final int dy = Math.abs(start.y - goal.y);
		
		return (dx < dy) ? F * dx + dy : F * dy + dx; 
	}
	
	public static final double chebyshevDistance(State state) {
		final Point start = state.getStart();
		final Point goal = state.getGoal();		

		final int dx = Math.abs(start.x - goal.x);
		final int dy = Math.abs(start.y - goal.y);
		
		return Math.max(dx, dy); 
	}

}
