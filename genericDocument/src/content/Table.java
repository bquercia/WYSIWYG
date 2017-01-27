/**
 * 
 */
package content;

import java.util.LinkedList;

/**
 * A table is an element that contains cells, organized in lines.
 * It is similar to an HTML table in its structure, but contains enriched functions.
 * @author Bruno Quercia
 *
 */
public class Table extends Element {
	private int nbRows;
	private int nbCols;
	private LinkedList<Row> rows;
	/**
	 * 
	 */
	public Table() {
		this.nbRows = 1;
		this.nbCols = 1;
		this.rows = new LinkedList<Row>();
	}
	
	public Table(int nbRows, int nbCols){
		this.nbRows = nbRows;
		this.nbCols = nbCols;
		this.rows = new LinkedList<Row>();
		for(int i = 0 ; i < nbRows ; i++){
			Row r = new Row(this, nbCols);
			rows.add(r);
		}
	}
	
	/**
	 * This function enables a table to create a new cell at a given position on lines smaller than a given value.
	 * @param position
	 * @param size
	 */
	protected void addColumn(int position, int size){
		for(Row r: rows){
			if(r.getSize() < size){
				r.addCell(position);
			}
		}
	}
	
	public void addRow(int position){
		this.rows.add(position, new Row(this));
	}
	
	public Row getNextRow(Row row){
		int pos = rows.indexOf(row);
		if(pos == -1)
			return null;
		if(pos == rows.size() - 1)
			return null;
		return rows.get(pos + 1);
	}
	
	public Row getPreviousRow(Row row){
		int pos = rows.indexOf(row);
		if(pos == -1)
			return null;
		if(pos == 0)
			return null;
		return rows.get(pos - 1);
	}
	
	public int getRowNumber(Row row){
		return rows.indexOf(row);
	}
	
	public Cell getCell(int row, int col){
		return rows.get(row).findPosition(col);
	}
	
	public LinkedList<Row> getRows(){
		return this.rows;
	}

}
