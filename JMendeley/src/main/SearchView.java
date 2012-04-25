package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SearchView implements ActionListener, ListSelectionListener{

	/** The Singleton for this class **/
	private static SearchView _singleton = null;
	
	private SearchManager _searchManager = null;
	
	//UI Elements
	
	/** The container for the UI **/
	private JFrame _frame = null;
	
	
	private SearchView (SearchManager sm){
		
		this._searchManager = sm;
		
		//Instantiate the GUI.
		this._frame = new JFrame("JMendeley");
		this._frame.setLayout(new BorderLayout());
		this._frame.setBounds(800, 600, 800, 600);
		
		//North Panel
		JPanel northPanel = new JPanel();
		northPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//Center Panel
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel resultsPanel = new JPanel();
		resultsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		centerPanel.add(searchPanel, BorderLayout.LINE_START);
		centerPanel.add(resultsPanel, BorderLayout.LINE_END);
		
		this._frame.add(northPanel, BorderLayout.PAGE_START);
		this._frame.add(centerPanel, BorderLayout.CENTER);
		this._frame.setVisible(true);
		this._frame.setFocusable(true);
				
	}
	
	
	public static SearchView getInstance(SearchManager sm){
		
		if (_singleton == null)
			_singleton = new SearchView(sm);
		return _singleton;
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
