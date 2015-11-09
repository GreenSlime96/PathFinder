package me.bpek.PathFinder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

public class View extends JComponent implements Observer {

	private static final long serialVersionUID = 8596333059804667076L;
	
	// ==== Constants ====
	
	public static final int PIXEL_WIDTH = 30;
	
	public static final Color COLOUR_BORDER = new Color(0, 0, 0, 51);	
	public static final Color COLOUR_NODE = new Color(0xafeeee);
	public static final Color COLOUR_FRINGE = new Color(0x98fb98);
	public static final Color COLOUR_START = new Color(0x00dd00);
	public static final Color COLOUR_GOAL = new Color(0xee4400);
	public static final Color COLOUR_WALL = new Color(0x808080);
	
	
	// ==== Properties ====
	
	public final Model model;
	
	
	// ==== Constructor ====
	
	public View(Model model) {
		this.model = model;
		
		model.addObserver(this);
		
		setMinimumSize(new Dimension(13 * PIXEL_WIDTH, 5 * PIXEL_WIDTH));
		setPreferredSize(new Dimension(30 * PIXEL_WIDTH, 20 * PIXEL_WIDTH));
		
		// resizing
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {				
				final int sizeX = getSize().width / PIXEL_WIDTH + 1;
				final int sizeY = getSize().height / PIXEL_WIDTH + 1;
				
				model.setSize(new Dimension(sizeX, sizeY));
				
//				timer.restart();
			}

//			@Override
//			public void componentShown(ComponentEvent e) {
//				final Timer timer = new Timer(200, new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						final int sizeX = getSize().width / PIXEL_WIDTH;
//						final int sizeY = getSize().height / PIXEL_WIDTH;
//						
//						model.setSize(new Dimension(sizeX, sizeY));
////						model.fit();
//					}
//				});
//				
//				timer.setRepeats(false);
//				timer.start();
//			}
		});
		
		MouseAdapter mouseAdapter = new MouseAdapter() {
			private int state = 0;
			
			// reset every time a click is registered
			public void mousePressed(MouseEvent e) {
				Point point = convertPoint(e.getPoint());
				
				if (point.equals(model.getStart()))
					state = 1;
				else if (point.equals(model.getGoal()))
					state = 2;
				else if (model.getWalls().contains(point))
					state = 3;
				else
					state = 4;
				
				// simulate a drag
				mouseDragged(e);
			}
			
			public void mouseReleased(MouseEvent e) {
				state = 0;
			}
			
			public void mouseDragged(MouseEvent e) {
				if (state == 0)
					return;
				
				Point newPoint = convertPoint(e.getPoint());
				
				if (state == 1)
					model.setStart(newPoint);
				else if (state == 2)
					model.setGoal(newPoint);
				else if (state == 3)
					model.clearWall(newPoint);
				else if (state == 4)
					model.addWall(newPoint);
				
				repaint();
			}
			
			private Point convertPoint(Point point) {
				return new Point(point.x / PIXEL_WIDTH, point.y / PIXEL_WIDTH);
			}
		};
		
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}
	
	// ==== JComponent Override ====
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Dimension dimension = model.getSize();
		Point start = model.getStart();
		Point goal = model.getGoal();
		
		g.setColor(Color.WHITE);		
		g.fillRect(0, 0, dimension.width * PIXEL_WIDTH, dimension.height * PIXEL_WIDTH);
		
		g.setColor(COLOUR_START);
		g.fillRect(start.x * PIXEL_WIDTH, start.y * PIXEL_WIDTH, PIXEL_WIDTH, PIXEL_WIDTH);
		
		g.setColor(COLOUR_GOAL);
		g.fillRect(goal.x * PIXEL_WIDTH, goal.y * PIXEL_WIDTH, PIXEL_WIDTH, PIXEL_WIDTH);
		
		g.setColor(COLOUR_WALL);
		for (Point wall : model.getWalls())
			g.fillRect(wall.x * PIXEL_WIDTH, wall.y * PIXEL_WIDTH, PIXEL_WIDTH, PIXEL_WIDTH);
		
		g.setColor(COLOUR_NODE);
		for (Point opened : model.getClosed())
			g.fillRect(opened.x * PIXEL_WIDTH, opened.y * PIXEL_WIDTH, PIXEL_WIDTH, PIXEL_WIDTH);
		
		g.setColor(COLOUR_FRINGE);
		for (Point opened : model.getOpened())
			g.fillRect(opened.x * PIXEL_WIDTH, opened.y * PIXEL_WIDTH, PIXEL_WIDTH, PIXEL_WIDTH);
			

		g.setColor(COLOUR_BORDER);
		for (int y = 0; y < dimension.height; y++) {
			final int yPos = y * PIXEL_WIDTH;

			for (int x = 0; x < dimension.width; x++) {
				final int xPos = x * PIXEL_WIDTH;

				g.drawRect(xPos, yPos, PIXEL_WIDTH, PIXEL_WIDTH);
			}
		}
	}

	// ==== Observer Implementation ====

	@Override
	public void update(Observable o, Object arg) {		
		if (o == model) {
			repaint();
		}		
	}
}
