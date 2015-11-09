package me.bpek.PathFinder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Controls extends Box implements Observer, ActionListener {
	
	private static final long serialVersionUID = 4393439353746474082L;
	
	// ==== Properties ====
	
	private final Model model;
	
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
		
		// vertical spacing
		add(new JPanel(new GridBagLayout()));
		
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
		buttonsPanel.add(startButton);
		buttonsPanel.add(pauseButton);
		buttonsPanel.add(clearButton);
		
		startButton.addActionListener(this);
		
		add(buttonsPanel);
	}

	// ==== Observer Implementaiton ====
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	
	// ==== ActionListener Implementation ====
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
