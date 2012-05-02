package main;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class PaperAbstractView extends JFrame implements ActionListener {
	
	private String abst;
	private String title;
	private JButton closeButton;
	
	public PaperAbstractView(Paper p) {
		
		title = p.getTitle();
		if(p.getAbstract() == null || p.getAbstract().equals("")) {
			abst = "No abstract available for " + p.getTitle() + ", sorry!";
		} else abst = p.getAbstract();
		
		setVisible(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void display() {
		Container contentPane = getContentPane();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel abstPanel = new JPanel();
		contentPane.add(abstPanel);
		
		setTitle(title);
		JTextArea area = new JTextArea(15, 60);
		JScrollPane scrollPane = new JScrollPane(area); 
		area.setEditable(false);
		area.setText(abst);
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		abstPanel.add(scrollPane);
		
		JButton close = this.closeButton = new JButton("Close");
		close.setActionCommand("close");
		close.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(close);
		abstPanel.add(buttonPanel);
		
		pack();
		setVisible(true);
	}
	

	public void actionPerformed(ActionEvent arg0) {
		
		if(arg0.getSource() == this.closeButton){
			setVisible(false);
			dispose();
		}
	}
}
