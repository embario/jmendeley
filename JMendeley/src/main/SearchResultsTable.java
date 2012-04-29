package main;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

 
/*
 * TableDemo.java requires no other files.
 */
 
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import main.TableRenderDemo.MyTableModel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
 
/** 
 * TableDemo is just like SimpleTableDemo, except that it
 * uses a custom TableModel.
 */
public class SearchResultsTable extends JPanel {
	
    private boolean DEBUG = false;
    
    private ArrayList <Paper> papers = null;
    private JTable _table = null;
    private MyTableModel _model = null;
 
    public SearchResultsTable () {
    	
        super(new GridLayout(1,0));
 
        papers = new ArrayList <Paper> ();
        
        MyTableModel model = this._model = new MyTableModel();
        JTable table = this._table = new JTable(model);
        model.setTable(table);
        
        table.setPreferredScrollableViewportSize(new Dimension(800, 600));
        table.setFillsViewportHeight(true);
        table.setColumnSelectionAllowed(true);
        table.setShowGrid(false);
        table.setEnabled(false);
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        
        this.initColumns(table);
 
        //Add the scroll pane to this panel.
        this.add(scrollPane);
    }
    
    public void updatePaper(ArrayList <Paper> papers){ this._model.updateRows(papers);}
    
    
    /*
     * This method picks good column sizes.
     * If all column heads are wider than the column's cells'
     * contents, then you can just use column.sizeWidthToFit().
     */
    private void initColumns(JTable table) {
    	
        MyTableModel model = (MyTableModel) table.getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        
        Object[] defaultValues  = model.defaultColumnvalues;
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
 
        //for each column, set it up.
        for (int i = 0; i < 5; i++) {
        	
            column = table.getColumnModel().getColumn(i);
 
            comp = headerRenderer.getTableCellRendererComponent( null, column.getHeaderValue(), false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;
 
            comp = table.getDefaultRenderer(model.getColumnClass(i)).getTableCellRendererComponent(table, defaultValues [i], false, false, 0, i);
            
            cellWidth = comp.getPreferredSize().width;
 
            if (DEBUG) {
                System.out.println("Initializing width of column "
                                   + i + ". "
                                   + "headerWidth = " + headerWidth
                                   + "; cellWidth = " + cellWidth);
            }
 
            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }
    
    
 
    class MyTableModel extends AbstractTableModel {
  
    	private JTable table;
    	private static final int NUM_COLUMNS = 6;
    	private static final int NUM_ROWS = 10;
        private String[] columnNames = {"Select Paper", "Title", "Author(s)", "Year", "Publication Reference", "Abstract"};
        
        public final Object [] defaultColumnvalues = {Boolean.FALSE, "title of paper", "author(s)", new Integer (2000), "pub ref", new JButton("Abstract")}; 
        
        //Create an empty data array initially.
        private Object[][] data = {{"", "", "", "", "", ""}};
        
        public MyTableModel (){
        	
        	/*Action delete = new AbstractAction()
        	{
        	    public void actionPerformed(ActionEvent e)
        	    {
        	        JTable table = (JTable)e.getSource();
        	        int modelRow = Integer.valueOf( e.getActionCommand() );
        	        ((DefaultTableModel)table.getModel()).removeRow(modelRow);
        	    }
        	}; */
        	
        }
        
        public void setTable(JTable table) { 
        	this.table = table;
        	this.setValueAt(new ButtonColumn (table, null, data.length - 1, new Paper()), 0, data.length - 1);
        
        }
        
        
        public int getColumnCount() {return columnNames.length;}
        public int getRowCount() {return data.length;}
        public String getColumnName(int col) {return columnNames[col];}
        
        //Getter, setter methods for table data.
        public Object getValueAt(int row, int col) {return data[row][col];}
        public void setValueAt(Object value, int row, int col){
        	
        	data [row][col] = value; 
        	this.fireTableCellUpdated(row, col);
        	
        }
        
        protected void updateRows(ArrayList <Paper> papers){
        	
        	
        	
        	
        }
        
        
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return this.getValueAt(0, c).getClass();
        }
 
 
        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
 
            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }
 
}