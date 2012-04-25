package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class LoginView implements ActionListener, ListSelectionListener{

	private JFrame _frame = null;
	
	
	private LoginView (){
		
		this._frame = new JFrame("Log-in to Mendeley using JMendeley");
		this._frame.setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		this._frame.add(northPanel, BorderLayout.NORTH);
				
	}
	
	
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
