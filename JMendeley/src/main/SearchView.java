package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import util.JMendeleyApiUrls;
import util.JMendeleyUIUtils;
import util.SpringUtilities;

public class SearchView implements ActionListener {
	
	private SearchManager _searchManager = null;
	private AccountManager _accountManager = null;
	private AuthenticationManager _authManager = null;
	private Account _account = null;
	private ArrayList<Paper> _papers = null;
	
	//UI Elements
	
	/** The container for the UI **/
	private JFrame _frame = null;
	private JPanel _panel = null;
	
	//Results Panel elements
	private SearchResultsTable _resultsTable;
	private JPanel _searchResultsActionPanel;
	private JButton _searchResultsActionButton;
	
	//Search Panel elements
	private JButton _searchButton = null;
	private JTextField _searchBarField = null;
	private JTextField _titleField = null;
	private JTextField _authorField = null;
	private JTextField _yearField = null;
	private JTextField _pubRefField = null;
	private JTextField _searchNumResults;
	
	//ODL Checkboxes.
	private JCheckBox _apiMendeleyBox = null;
	private JCheckBox _apiArXivBox = null;

	
	
	/**
	 * Basic private constructor for the SearchView.
	 * @param sm
	 * @param am
	 */
	private SearchView (SearchManager sm, AccountManager am, AuthenticationManager auth){
		
		this._searchManager = sm;
		this._accountManager = am;
		this._authManager = auth;
		this._account = this._accountManager.getAccount();
		this._papers = new ArrayList <Paper> ();
		
	}
	
	
	public static void loadView(SearchManager sm, AccountManager am, AuthenticationManager auth){
		
		SearchView view = new SearchView (sm, am, auth);
		view.makeGUI();
	}
	


	/**
	 * Method for constructing all of the various parts that make up the JMendeley Search GUI.
	 */
	private void makeGUI() {
		
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
		JPanel profilePanel = new JPanel();
		profilePanel.setLayout(new SpringLayout());
		profilePanel.setBorder(BorderFactory.createLineBorder(Color.black));

		//JLabels for the Profile Panel
		ImageIcon icon = new ImageIcon(JMendeleyUIUtils.MENDELEY_ICON);
		JLabel iconImage = new JLabel(icon);
		
		Account account = this._account;
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
		
		
		//First Panel for searchPanel
		JPanel searchFieldsPanel = new JPanel();
		searchFieldsPanel.setLayout(new SpringLayout());
		
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
		
		//Max Results Field
		JTextField searchNumResults = this._searchNumResults = new JTextField(1);
		JLabel searchNumResultsLabel = new JLabel("Max Results:", JLabel.TRAILING);
		searchNumResultsLabel.setLabelFor(searchNumResults);
		
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
		searchFieldsPanel.add(searchNumResultsLabel);
		searchFieldsPanel.add(searchNumResults);
		
		//Lay out the panel.
        SpringUtilities.makeCompactGrid(searchFieldsPanel, 6, 2, 10, 10, 5, 5);
        
		//Layout the Search Actions Panel.
		JPanel searchActionsPanel = new JPanel();
		searchActionsPanel.setLayout(new SpringLayout());
        
		//apiPanel configuration - Panel that holds API checkboxes.
		JPanel apiPanel = new JPanel();
		apiPanel.setBackground(Color.gray);
		
		//ODL Checkboxes.
		JCheckBox arxivBox = this._apiArXivBox = new JCheckBox("ArXiv");
		JCheckBox mendBox =  this._apiMendeleyBox = new JCheckBox ("Mendeley");
		JLabel apiBoxLabel = new JLabel("Digital Library Selection");
		
		apiPanel.add(apiBoxLabel);
		apiPanel.add(arxivBox);
		apiPanel.add(mendBox);
		
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
		this._panel.setBackground(Color.GRAY);
		
		this._frame.add(this._panel);
		this._frame.setBackground(Color.GRAY);
		this._frame.pack();
		this._frame.setVisible(true);
		this._frame.setFocusable(true);
		
		
		//Make sure to terminate the program when the GUI is closed.
		this._frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		SearchResultsTable table = this._resultsTable;
		
		//If the User hits the search button...
		if (arg0.getSource() == this._searchButton){
			
			//Our list of search terms and refinements.
			ArrayList <String> terms = new ArrayList <String> ();
			
			//Add search terms (if need be).
			if (this._searchBarField.getText().equals("") == false)
				terms.add(JMendeleyApiUrls.JMEND_SEARCH_TERM + this._searchBarField.getText());
			if (this._authorField.getText().equals("") == false)
				terms.add(JMendeleyApiUrls.JMEND_AUTHOR_TERM + this._authorField.getText());	
			if (this._titleField.getText().equals("") == false)
				terms.add(JMendeleyApiUrls.JMEND_TITLE_TERM + this._titleField.getText());
			if (this._yearField.getText().equals("") == false)
				terms.add(JMendeleyApiUrls.JMEND_YEAR_TERM + this._yearField.getText());
			if (this._pubRefField.getText().equals("") == false)
				terms.add(JMendeleyApiUrls.JMEND_PUBREF_TERM + this._pubRefField.getText());
		
			//Our list of selected connection strategies.
			ArrayList <ConnectionStrategy> connections = new ArrayList <ConnectionStrategy>();
			
		
			//Add the connections (if need be).
			if(this._apiArXivBox.isSelected() == true)
				connections.add(new ArXivConnectionStrategy());
			if (this._apiMendeleyBox.isSelected() == true)
				connections.add(new MendeleyConnectionStrategy(this._authManager));
		
			
			//Add Num results.
			int numResults = 10;
			String numResultsText = this._searchNumResults.getText();
			
			try {
				
				if (numResultsText.equals("") == false)
					numResults = Integer.parseInt(numResultsText);
				
			} catch (NumberFormatException e) { 
				JMendeleyUIUtils.showMessageDialog("Please enter a valid number for 'Max Results'.", 
						"Max results not valid", JOptionPane.INFORMATION_MESSAGE);
				return; }
				
			if (connections.isEmpty() == false){
				
				//The results of our search!
				ArrayList <Paper> papers = this._papers = (ArrayList<Paper>) this._searchManager.searchForPapers(terms, connections, numResults);
				
				if (papers.isEmpty() == true)
					JMendeleyUIUtils.showMessageDialog("Your search yielded no results!", "No Search Results", JOptionPane.INFORMATION_MESSAGE);
				else
					this._resultsTable.updatePapers(papers); //Go ahead and update the table to contain the papers' information.
				
			} else{
				
				JMendeleyUIUtils.showMessageDialog("Please select at least one digital library to search.", 
						"No Digital Library Selected", JOptionPane.INFORMATION_MESSAGE);
			}
			
			
			

		}//end if SEARCH
		
		
		//TODO: If we're ready to send papers.
		if (arg0.getSource() == this._searchResultsActionButton){
			
			ArrayList <Paper> selectedPapers = table.getSelectedPapers();
			
			if (this._papers.isEmpty() == false && selectedPapers.isEmpty() == false){
				
				int answer = JMendeleyUIUtils.showConfirmYesNoDialog("Are you sure you want to send the selected papers to your Mendeley Account?", "Send Papers to Mendeley");
				
				//The user answered "Yes".
				if (answer == 0){

					
					try {
						
						this._searchManager.sendPapersToMendeley(selectedPapers);
						JMendeleyUIUtils.showMessageDialog("The selected papers were sent your Mendeley Account successfully.", "Successful", JOptionPane.PLAIN_MESSAGE);
						
					} catch (Exception e){
						JMendeleyUIUtils.showMessageDialog("An Unexpected Error occurred when sending selected papers to Mendeley. Please try again.", "Error occurred", JOptionPane.ERROR_MESSAGE);
					}
					
				}
				
			}
			else
				JMendeleyUIUtils.showMessageDialog("There are no papers to send to Mendeley. Please select papers or try a search that yields results.", "No Papers to Send", JOptionPane.INFORMATION_MESSAGE);
			
		}//end conditional
		
	}
}
