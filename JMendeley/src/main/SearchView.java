package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import util.SpringUtilities;

public class SearchView implements ActionListener {

	/** The Singleton for this class **/
	private static SearchView _singleton = null;
	
	private SearchManager _searchManager = null;
	
	//UI Elements
	
	/** The container for the UI **/
	private JFrame _frame = null;
	private JPanel _panel = null;
	
	//TODO: Profile Panel elements
	private JPanel _profilePanel = null;
	private ImageIcon _icon = null;

	
	//TODO: Results Panel elements
	
	//Search Panel elements
	private JPanel _searchPanel = null;
	private JButton _searchButton = null;
	private JTextField _searchBar = null;
	private JPanel _apiBoxPanel = null;
	private JCheckBox _apiMendeleyBox = null;
	private JCheckBox _apiArXivBox = null;

	private JTextField _titleField = null;
	private JTextField _authorField = null;
	
	
	
	private SearchView (SearchManager sm){
		
		this._searchManager = sm;
		
		//Instantiate the GUI.
		this._frame = new JFrame("JMendeley");
		this._panel = new JPanel();
		this._panel.setLayout(new BorderLayout());
		this._frame.setBounds(800, 600, 800, 600);
		
		/** GUI is split in two panels - east and west panels. **/
		//East Panel - will hold the search results panel
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BorderLayout());
		
		//West Panel - will hold profile and search panels
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new GridLayout(2,1));
		
		/** West Panel is split in two panels - profile an search panels **/
		
		//Search Panel Configuration - splits search panel by separating the text field elements from the action elements.
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(2,0));
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		//Search Bar
		JTextField searchBar = this._searchBar = new JTextField();
		searchBar.setPreferredSize(new Dimension(100, 40));
		JLabel searchBarLabel = new JLabel ("Search:", JLabel.TRAILING);
		searchBarLabel.setLabelFor(searchBar);
		
		//Search Button
		JButton searchButton = this._searchButton = new JButton ("Search");
		searchButton.addActionListener(this);
		searchButton.setPreferredSize(new Dimension(200,40));
		
		//Title Field
		JTextField titleField = this._titleField = new JTextField();
		JLabel titleFieldLabel = new JLabel("Title:", JLabel.TRAILING);
		titleFieldLabel.setLabelFor(titleField);
		
		//Author Field
		JTextField authorField = this._authorField = new JTextField();
		JLabel authorFieldLabel = new JLabel("Author:", JLabel.TRAILING);
		authorFieldLabel.setLabelFor(authorField);
		
		//apiPanel configuration - Panel that holds API checkboxes.
		JPanel apiPanel = this._apiBoxPanel = new JPanel();
		apiPanel.setBackground(Color.gray);
		
		//API Box Label
		JLabel apiBoxLabel = new JLabel("Digital Library Selection");
		//ArXiv CheckBox
		JCheckBox arxivBox = this._apiArXivBox = new JCheckBox("ArXiv");
		//Mendeley Checkbox
		JCheckBox mendBox = this._apiMendeleyBox = new JCheckBox ("Mendeley");
		
		apiPanel.add(apiBoxLabel);
		apiPanel.add(arxivBox);
		apiPanel.add(mendBox);
		
		//First Panel for searchPanel
		JPanel searchFieldsPanel = new JPanel();
		searchFieldsPanel.setLayout(new SpringLayout());
		
		searchFieldsPanel.add(searchBarLabel);
		searchFieldsPanel.add(searchBar);
		searchFieldsPanel.add(titleFieldLabel);
		searchFieldsPanel.add(titleField);
		searchFieldsPanel.add(authorFieldLabel);
		searchFieldsPanel.add(authorField);
		
		//Lay out the panel.
        SpringUtilities.makeCompactGrid(searchFieldsPanel, 3, 2, 10, 10, 5, 5);
		
		JPanel searchActionsPanel = new JPanel();
		searchActionsPanel.setLayout(new SpringLayout());
		
		searchActionsPanel.add(apiPanel);
		searchActionsPanel.add(new JPanel());
		searchActionsPanel.add(searchButton);
		searchActionsPanel.add(new JPanel());
		
		//Lay out the panel.
        SpringUtilities.makeCompactGrid(searchActionsPanel, 2, 2, 10, 10, 5, 5);

        //Add these panels to the searchPanel.
        searchPanel.add(searchFieldsPanel);
        searchPanel.add(searchActionsPanel);
        
        
      
		//Results Panel Configuration
		JPanel resultsPanel = new JPanel();
		resultsPanel.setBorder(BorderFactory.createLineBorder(Color.green));

		this._panel.add(searchPanel, BorderLayout.LINE_START);
		this._panel.add(resultsPanel, BorderLayout.CENTER);
		
		this._frame.add(this._panel);
		this._frame.setBackground(Color.DARK_GRAY);
		//this._frame.pack();
		this._frame.setVisible(true);
		this._frame.setFocusable(true);
		
		
		//Make sure to terminate the program when the GUI is closed.
		this._frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
	}
	
	
	public static SearchView getInstance(SearchManager sm){
		
		if (_singleton == null)
			_singleton = new SearchView(sm);
		return _singleton;
	}
	


	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//If the User hits the search button...
		if (arg0.getSource() == this._searchButton){
			//this._searchManager.searchForPapers();
			
		}
		
	}

}
