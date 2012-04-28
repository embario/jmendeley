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

public class AbstractView extends JFrame implements ActionListener {
	private String abst;
	private String title;
	
	public AbstractView(Paper p) {
		title = p.title;
		if(p.abst == null || p.abst.equals("")) {
			abst = "No abstract available for " + p.title + ", sorry!";
		} else abst = p.abst;
		setVisible(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void display() {
		Container contentPane = getContentPane();		
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
		
		JButton close = new JButton("Close");
		close.setActionCommand("close");
		close.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(close);
		abstPanel.add(buttonPanel);
		
		pack();
		setVisible(true);
	}
	
	public void close() {
		setVisible(false);
		dispose();
	}
	
	public static void main(String[] args) {
		Paper p = new Paper();
		p.title = "Effects for Funargs";
		p.abst = "Stack allocation and first-class functions don't naturally mix together. In this paper we show that a type and effect system can be the detergent that helps these features form a nice emulsion. Our interest in this problem comes from our work on the Chapel language, but this problem is also relevant to lambda expressions in C++ and blocks in Objective C. The difficulty in mixing first-class functions and stack allocation is a tension between safety, efficiency, and simplicity. To preserve safety, one must worry about functions outliving the variables they reference: the classic upward funarg problem. There are systems which regain safety but lose programmer-predictable efficiency, and ones that provide both safety and efficiency, but give up simplicity by exposing regions to the programmer. In this paper we present a simple design that combines a type and effect system, for safety, with function-local storage, for control over efficiency. ";
		AbstractView v = new AbstractView(p);
		v.display();

	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals("close"))
			close();
	}
}
