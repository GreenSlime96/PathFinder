package algorithms;

import java.awt.Point;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import core.Algorithm;
import core.Heuristic;
import core.Grid;

public class Search {

	// ==== Properties ====

	private final Set<Point> opened;
	private final Set<Point> closed;
	private final Stack<Point> solution;
	
	private BiConsumer<Search, BiFunction<Integer, Integer, Double>> search;
	private BiFunction<Integer, Integer, Double> heuristic;
	
	private Grid startState;
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

	public Grid getStartState() {
		return startState;
	}

	public void setStartState(Grid startState) {
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
		this.diagonalMovement = diagonalMovement;
	}

	public int getSearchHeuristic() {
		return searchHeuristic;
	}

	public void setSearchHeuristic(int searchHeuristic) {
		this.searchHeuristic = searchHeuristic;

		switch (searchHeuristic) {
		case Heuristic.MANHATTAN_DISTANCE:
			heuristic = Heuristic::manhattanDistance;
			break;
		case Heuristic.EUCLIDEAN_DISTANCE:
			heuristic = Heuristic::euclideanDistance;
			break;
		case Heuristic.OCTILE_DISTANCE:
			System.out.println("here");
			heuristic = Heuristic::octileDistance;
			break;
		case Heuristic.CHEBYSHEV_DISTANCE:
			heuristic = Heuristic::chebyshevDistance;
			break;
		default:
			heuristic = Heuristic::manhattanDistance;
			break;
		}
	}

	public int getSearchAlgorithm() {
		return searchAlgorithm;
	}

	public void setSearchAlgorithm(int searchAlgorithm) {
		this.searchAlgorithm = searchAlgorithm;
		
		switch (searchAlgorithm) {
		case Algorithm.A_STAR:
			search = AStar::search;
			break;
		case Algorithm.BREADTH_FIRST:
			search = Algorithm::BreadthFirst;
			break;
		case Algorithm.DEPTH_FIRST:
			search = Algorithm::DepthFirst;
			break;
		case Algorithm.BEST_FIRST:
			search = BestFirst::search;
			break;
		case Algorithm.DIJKSTRA:
			search = Dijkstra::search;
			break;
		default:
			search = Algorithm::AStar;
			break;
		}
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
		} catch (InterruptedException e) {
		}
	}

	public void start() {
		Search original = this;

		thread = new Thread() {
			@Override
			public void run() {
				search.accept(original, heuristic);
			}
		};

		thread.start();
	}
}
