package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import util.JMendeleyUIUtils;
import util.SpringUtilities;

public class SearchView implements ActionListener {

	/** The Singleton for this class **/
	private static SearchView _singleton = null;
	
	private SearchManager _searchManager = null;
	private AccountManager _accountManager = null;
	private Account _account = null;
	
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
	private JTextField _searchBarField = null;
	private JPanel _apiBoxPanel = null;
	private JTextField _titleField = null;
	private JTextField _authorField = null;
	private JTextField _yearField = null;
	private JTextField _pubRefField = null;

	private SearchResultsTable _resultsTable;

	private JPanel _searchResultsActionPanel;

	private JButton _searchResultsActionButton;
	
	
	
	private SearchView (SearchManager sm, AccountManager am){
		
		this._searchManager = sm;
		this._accountManager = am;
		Account account = this._accountManager.getAccount();
		
		//Instantiate the GUI.
		this._frame = new JFrame("JMendeley");
		this._panel = new JPanel();
		this._panel.setLayout(new BorderLayout());
		this._frame.setBounds(500, 300, 1200, 800);
		
		/** GUI is split in two panels - east and west panels. **/
		//East Panel - will hold the search results panel
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BorderLayout());
		
		//West Panel - will hold profile and search panels
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new GridLayout(2,1));
		
		/** West Panel is split in two panels - profile and search panels **/
		
		//Profile Panel Configuration - prints out Account information.
		JPanel profilePanel = this._profilePanel = new JPanel();
		profilePanel.setLayout(new SpringLayout());
		profilePanel.setBorder(BorderFactory.createLineBorder(Color.black));

		//JLabels for the Profile Panel
		ImageIcon icon = this._icon = new ImageIcon(JMendeleyUIUtils.MENDELEY_ICON);
		JLabel iconImage = new JLabel(icon);
		
		JLabel accName = new JLabel (account.getName());
		JLabel accProfileID = new JLabel("Profile ID: " + account.getProfileID());
		JLabel accAcademicStatus = new JLabel ("Academic Status: " + account.getAcademicStatus());
		JLabel accDiscipline = new JLabel("Discipline: " + account.getDiscipline());
		JLabel accURL = new JLabel("User URL: " + account.getURL());
		
		ArrayList <JLabel> accResearchInterests = new ArrayList <JLabel> ();
		String [] researchInterests = account.getResearchInterests();
		for (String ri : researchInterests){
			JLabel ril = new JLabel(ri);
			ril.setFont(Font.getFont(Font.MONOSPACED));
			accResearchInterests.add(ril);
		}
			
		profilePanel.add(iconImage);
		profilePanel.add(accProfileID);
		profilePanel.add(accName);
		profilePanel.add(accAcademicStatus);
		profilePanel.add(accDiscipline);
		profilePanel.add(accURL);
		profilePanel.add(new JLabel("Research Interests: "));
		for (JLabel ri : accResearchInterests)
			profilePanel.add(ri);
		
		
        SpringUtilities.makeCompactGrid(profilePanel, 7 + accResearchInterests.size() , 1, 10, 10, 5, 5);
		
		
		//Search Panel Configuration - splits search panel by separating the text field elements from the action elements.
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(2,0));
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//Search Bar
		JTextField searchBar = this._searchBarField = new JTextField();
		JLabel searchBarLabel = new JLabel ("Search:", JLabel.TRAILING);
		searchBar.setBackground(Color.yellow);
		searchBarLabel.setLabelFor(searchBar);
		
		//Search Button
		JButton searchButton = this._searchButton = new JButton ("Search");
		searchButton.addActionListener(this);
		
		//Title Field
		JTextField titleField = this._titleField = new JTextField();
		JLabel titleFieldLabel = new JLabel("Title:", JLabel.TRAILING);
		titleFieldLabel.setLabelFor(titleField);
		
		//Author Field
		JTextField authorField = this._authorField = new JTextField();
		JLabel authorFieldLabel = new JLabel("Author:", JLabel.TRAILING);
		authorFieldLabel.setLabelFor(authorField);
		
		//Year Field
		JTextField yearField = this._yearField = new JTextField();
		JLabel yearFieldLabel = new JLabel ("Year:", JLabel.TRAILING);
		yearFieldLabel.setLabelFor(yearField);
		
		//Publication Reference Field
		JTextField pubField = this._pubRefField = new JTextField();
		JLabel pubFieldLabel = new JLabel ("Publication Reference:", JLabel.TRAILING);
		pubFieldLabel.setLabelFor(pubField);
		
		//apiPanel configuration - Panel that holds API checkboxes.
		JPanel apiPanel = this._apiBoxPanel = new JPanel();
		apiPanel.setBackground(Color.gray);
		
		//API Box Label
		JLabel apiBoxLabel = new JLabel("Digital Library Selection");
		//ArXiv CheckBox
		JCheckBox arxivBox = new JCheckBox("ArXiv");
		//Mendeley Checkbox
		JCheckBox mendBox =  new JCheckBox ("Mendeley");
		
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
		searchFieldsPanel.add(yearFieldLabel);
		searchFieldsPanel.add(yearField);
		searchFieldsPanel.add(pubFieldLabel);
		searchFieldsPanel.add(pubField);
		
		//Lay out the panel.
        SpringUtilities.makeCompactGrid(searchFieldsPanel, 5, 2, 10, 10, 5, 5);
		
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
        
        //Now, add the profilePanel and searchPanel to the West Panel
        westPanel.add(profilePanel);
        westPanel.add(searchPanel);
    
        /** East Panel Configuration **/
        
		//Results Panel Configuration - Set up the JTable of type SearchResultsTable.
		JPanel resultsPanel = new JPanel();
		resultsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//Create the JTable and its Scroll Pane.
		SearchResultsTable table = this._resultsTable = new SearchResultsTable();
		
        //Add the scrollpane to the resultsPanel
        resultsPanel.add(table);
        
        //Search Results Action Configuration
        JPanel resultsActionPanel = this._searchResultsActionPanel = new JPanel();
        resultsActionPanel.setLayout(new BorderLayout());
        
        //JButton for submitting papers to Mendeley.
        JButton srSubmitPapersButton = this._searchResultsActionButton = new JButton("Send Papers to Mendeley");
        srSubmitPapersButton.addActionListener(this);
        
        //Now, add the Submit Papers Button to the SRActionPanel.
        resultsActionPanel.add(srSubmitPapersButton, BorderLayout.CENTER);
        
		
		//Now, add the results results Action Panel to the eastPanel.
		eastPanel.add(resultsPanel, BorderLayout.CENTER);
		eastPanel.add(resultsActionPanel, BorderLayout.PAGE_END);

		this._panel.add(westPanel, BorderLayout.LINE_START);
		this._panel.add(eastPanel, BorderLayout.CENTER);
		this._panel.setBackground(Color.DARK_GRAY);
		
		this._frame.add(this._panel);
		this._frame.setBackground(Color.DARK_GRAY);
		this._frame.pack();
		this._frame.setVisible(true);
		this._frame.setFocusable(true);
		
		
		//Make sure to terminate the program when the GUI is closed.
		this._frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
	}
	
	
	public static SearchView getInstance(SearchManager sm, AccountManager am){
		
		if (_singleton == null)
			_singleton = new SearchView(sm, am);
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
