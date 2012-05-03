package main;
 
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
 
@SuppressWarnings("serial")
public class SearchResultsTable extends JPanel {

    private SearchResultsModel _model = null;
 
    public SearchResultsTable () {
    	
        super(new GridLayout(1,0));
        
        SearchResultsModel model = this._model = new SearchResultsModel();
        JTable table = new JTable(model);
        model.setTable(table);
        
        table.setName("JMendeley Search Results");
        table.setCellSelectionEnabled(true);
        table.setPreferredScrollableViewportSize(new Dimension(800, 600));
        table.setMinimumSize(new Dimension(1200,800));
        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);
        table.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(true);
        table.setShowGrid(true);
        table.setGridColor(Color.gray);
        table.setEnabled(true);
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        this.initColumns(table);
 
        //Add the scroll pane to this panel.
        this.add(scrollPane);
    }
   
    
    public void updatePapers(ArrayList <Paper> papers){ this._model.updateRows(papers);}
    
    public ArrayList <Paper> getSelectedPapers () { return this._model.getSelectedPapers();}
    
    
    /*
     * This method picks good column sizes.
     * If all column heads are wider than the column's cells'
     * contents, then you can just use column.sizeWidthToFit().
     */
    private void initColumns(JTable table) {
    	
        SearchResultsModel model = (SearchResultsModel) table.getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
       
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
 
        //for each column, set it up.
        for (int i = 0; i < model.columnNames.length; i++) {
        	
            column = table.getColumnModel().getColumn(i);
 
            comp = headerRenderer.getTableCellRendererComponent(table, column.getHeaderValue(), true, true, 0, i);
       
            headerWidth = comp.getPreferredSize().width;
 
            comp = table.getDefaultRenderer(model.getColumnClass(i)).getTableCellRendererComponent(table, model.defaultColumnvalues[i], true, true, 0, i);
            
            cellWidth = comp.getPreferredSize().width;
            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }
    
    
    /**
     * The MyTableModel inner class within the SearchResultsTable provides an abstraction for a two-dimensional array of data values for papers.
     * 
     * @author mbarrenecheajr
     *
     */
    class SearchResultsModel extends AbstractTableModel {
 
    	private ButtonColumn buttonColumn = null;
    	private Action selectPaperAbstract = null;   	
    	private static final int COLNUM_SELECT_PAPER = 0;
    	private static final int COLNUM_TITLE = 1;
    	private static final int COLNUM_AUTHOR = 2;
    	private static final int COLNUM_YEAR = 3;
    	private static final int COLNUM_TYPE = 4;
    	private static final int COLNUM_VENUE = 5;
    	private static final int COLNUM_HASPDF = 6;
    	private static final int COLNUM_ABSTRACT = 7;
    	
        protected String[] columnNames = 			{"Select Paper", 	"Title", 			"Author(s)", 	"Year", 		"Type",		"Venue", 	"Has PDF", 	"Abstract"};
        private Object [] defaultColumnvalues = 	{Boolean.FALSE, 	"title of paper", 	"author(s)", 	"paper year", 	"generic",	"venue", 	false,		"Abstract"};
        
        private ArrayList <Paper> papers = null;
        
        //Create an empty data array initially.
        private Object [][] data = null;
        
        public SearchResultsModel (){
        	
        	this.papers = new ArrayList <Paper> ();
        	
        	this.selectPaperAbstract = new AbstractAction() {
        		
        	    public void actionPerformed(ActionEvent e){
        	    	
        	    	//Grab the event source (table) and its table model.
        	        JTable table = (JTable)e.getSource();
        	        SearchResultsModel model = (SearchResultsModel) table.getModel();
        	        
        	        //The table row that was activated.
        	        int modelRow = Integer.valueOf(e.getActionCommand());
        	        
        	        //Grab the paper for this row.
        	        Paper paper = model.getPaperFromTable(modelRow);
        	        PaperAbstractView view = new PaperAbstractView(paper);
        	        
        	        //Display the paper abstract.
        	        view.display();
        	    }
        	};
        	
        }
        
        /**
         * This method iterates through all of the rows in the table and returns a list
         * of rows with selected papers (from user input).
         * @return
         */
        public ArrayList<Paper> getSelectedPapers() {
        	
        	//The result list to return.
        	ArrayList <Paper> results = new ArrayList <Paper> ();
        	
        	if (this.data == null || this.data.length == 0)
        		return results;
        	
        	for (int i = 0; i < this.data.length; i++){
        		
        		Boolean selectPaperValue = (Boolean) this.data[i][COLNUM_SELECT_PAPER];
        		if (selectPaperValue == true)
        			results.add(this.getPaperFromTable(i));
        	}
        	
        	return results;
		}

		public void setTable(JTable table) { 
        	this.buttonColumn = new ButtonColumn(table, this.selectPaperAbstract, this.columnNames.length - 1);
        }
        
        
        protected Paper getPaperFromTable(int row){
        	return this.papers.get(row);
        }
        
        public int getColumnCount() {return columnNames.length;}
        public int getRowCount() {return (data == null) ? 0: data.length;}
        public String getColumnName(int col) {return columnNames[col];}
        
        //Getter, setter methods for table data.
        public Object getValueAt(int row, int col) {
        
        	if (this.data == null){
        		switch (col){
        		
        		//Select Paper
        		case COLNUM_SELECT_PAPER: return Boolean.FALSE;
        		//Title
        		case COLNUM_TITLE: return "";
        		//Author(s)
        		case COLNUM_AUTHOR: return "";
        		//Year
        		case COLNUM_YEAR: return "";
        		//Type
        		case COLNUM_TYPE: return "";
        		//Venue
        		case COLNUM_VENUE: return "";
        		//Has PDF
        		case COLNUM_HASPDF: return true;
        		//Abstract
        		case COLNUM_ABSTRACT: return "";
        		default: return null;
        		}
        	}
        	
        	return data[row][col];
        	
        }
        
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col != 0 && col != this.columnNames.length - 1) {
                return false;
            } else {
                return true;
            }
        }
        
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) { return this.getValueAt(0, c).getClass(); }
        
        public void setValueAt(Object value, int row, int col){
        	
        	data [row][col] = value; 
        	this.fireTableCellUpdated(row, col);
        	
        }
        
        /**
         * This function accepts a non-empty ArrayList of papers and updates the rows in the table with their information.
         * @param papers
         */
        protected void updateRows(ArrayList <Paper> papers){
        	
        	ArrayList <Paper> tablePapers = this.papers;
        	this.data = new Object [papers.size()][this.columnNames.length];
        	
        	//Only if we want to empty the table completely before continuing.
        	tablePapers.clear();
        	
        	//For each paper in the incoming list, we iterate.
        	for (int i = 0; i < papers.size(); i++){
        		
        		Paper paper = papers.get(i);

           		//Select Paper
        		this.setValueAt(new Boolean (false), i, COLNUM_SELECT_PAPER);
        		//Title
        		this.setValueAt(paper.getTitle(), i, COLNUM_TITLE);
        		
        		//Author(s)
        		String [] authorNames = paper.getAuthors();
        		String authors = (authorNames.length == 0) ? "Unknown" : "[";
        		
        		for (int j = 0; j < authorNames.length; j++){
        			if (j != authorNames.length - 1)
        				authors += authorNames [j] + ", ";
        			else
        				authors += authorNames [j] + "]";
        		}
        			
        		this.setValueAt(authors, i, COLNUM_AUTHOR);
        		//Year
        		this.setValueAt(paper.getYear(), i, COLNUM_YEAR);
        		//Type
        		this.setValueAt(paper.getType(), i, COLNUM_TYPE);
        		//Venue
        		this.setValueAt(paper.getVenue(), i, COLNUM_VENUE);
        		//Has PDF
    		
    			if (paper.getPDF() == null)
    				this.setValueAt(false, i, COLNUM_HASPDF);
    			else
    				this.setValueAt(true, i, COLNUM_HASPDF);
    			
    			//Abstract
    			this.setValueAt("Abstract", i, COLNUM_ABSTRACT);
    			
    			tablePapers.add(paper);
        		
        	}
        	
        	this.fireTableDataChanged();
        	
        }
        

 
    }
 
}
