package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import core.Model;

public class Controls extends Box implements Observer, ActionListener {
	
	private static final long serialVersionUID = 4393439353746474082L;
	
	// ==== Properties ====
	
	private final Model model;
	
	private final JComboBox<String> algorithmComboBox = new JComboBox<String>();
	private final JComboBox<String> diagonalComboBox = new JComboBox<String>();
	private final JComboBox<String> heuristicComboBox = new JComboBox<String>();
	private final JButton startButton = new JButton("<html><center>Start<br/>Search<center></html>");
	private final JButton pauseButton = new JButton("<html><center>Pause<br/>Search<center></html>");
	private final JButton clearButton = new JButton("<html><center>Clear<br/>Walls<center></html>");
	
	// ==== Constructor ====
	
	public Controls(Model model) {
		super(BoxLayout.Y_AXIS);
		
        setPreferredSize(new Dimension(280, 600));
        setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 1, 0, 0, new Color(150, 150, 150)),
            new EmptyBorder(20, 20, 20, 20)));
        
		this.model = model;
		model.addObserver(this);
		
		algorithmComboBox.addItem("A-Star");
		algorithmComboBox.addItem("Breadth First");
		algorithmComboBox.addItem("Depth First");

		diagonalComboBox.addItem("Always");
		diagonalComboBox.addItem("Never");
		diagonalComboBox.addItem("If At Most One Obstacle");
		diagonalComboBox.addItem("Only When No Obstacles");
		
		heuristicComboBox.addItem("Manhattan Distance");
		heuristicComboBox.addItem("Euclidean Distance");
		
		addSetting(algorithmComboBox, "Algorithm", "The type of search algorithm to use");
		addSetting(heuristicComboBox, "Heuristic", "The type of heuristic to use");
		addSetting(diagonalComboBox, "Diagonals", "Whether or not diagonal searching is allowed");
		addSetting(startButton, "Start Search", "Starts the search");
		addSetting(clearButton, "Clear Walls", "Removes the walls");
		
		// vertical spacing
		add(new JPanel(new GridBagLayout()));
		
//		JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
//		buttonsPanel.add(startButton);
//		buttonsPanel.add(pauseButton);
//		buttonsPanel.add(clearButton);
//		
		algorithmComboBox.addActionListener(this);
		heuristicComboBox.addActionListener(this);
		diagonalComboBox.addActionListener(this);
		startButton.addActionListener(this);
		clearButton.addActionListener(this);
		pauseButton.addActionListener(this);
		
//		
//		add(buttonsPanel);
	}
	
	// ==== Private Helper Methods ====
	
	protected void addSetting(JComponent control, String title, String text) {
		JLabel label = new JLabel(title);
		label.setFont(label.getFont().deriveFont(Font.BOLD));

		control.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		
		JTextArea help = createHelpLabel(text);
		help.setFont(label.getFont().deriveFont(Font.ITALIC, 10));
	
		add(label);
		add(Box.createRigidArea(new Dimension(0, 3)));
		add(control);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(help);
		add(Box.createRigidArea(new Dimension(0, 15)));
	}
	
    private static JTextArea createHelpLabel(String text) {
        JTextArea textArea = new JTextArea();
        textArea.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        textArea.setEditable(false);
        textArea.setCursor(null);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        textArea.setText(text);
        textArea.setMaximumSize(new Dimension(300, 400));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

	// ==== Observer Implementaiton ====
	
	@Override
	public void update(Observable o, Object arg) {
		if (o == model) {
			
		}
		
	}
	
	
	// ==== ActionListener Implementation ====
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		
		if (source == algorithmComboBox) {
			model.setSearchAlgorithm(algorithmComboBox.getSelectedIndex());
		} else if (source == diagonalComboBox) {
			model.setDiagonalMovement(diagonalComboBox.getSelectedIndex() + 1);
		} else if (source == heuristicComboBox) {
			model.setSearchHeuristic(heuristicComboBox.getSelectedIndex());
		} else if (source == startButton) {
			model.startSearch();
		} else if (e.getSource() == clearButton) {
			model.clearWalls();
		}
		
	}

}
