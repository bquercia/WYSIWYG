/**
 * 
 */
package content;

import java.util.LinkedList;

/**
 * A table is an element that contains cells, organized in rows.
 * It is similar to an HTML table in its structure, but contains enriched functions.
 * @author Bruno Quercia
 *
 */
public class Table extends Element {
	private int nbRows;
	private int nbCols;
	private LinkedList<Row> rows;
	/**
	 * Creates a new table with dimensions (0, 0).
	 */
	public Table() {
		this.nbRows = 0;
		this.nbCols = 0;
		this.rows = new LinkedList<Row>();
	}
	
	/**
	 * Creates a new table with a given number of rows and columns.
	 * @param nbRows number of rows
	 * @param nbCols number of columns
	 */
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
	
	/**
	 * Adds a new row at a given position
	 * @param position the position
	 */
	public void addRow(int position){
		this.rows.add(position, new Row(this));
		//TODO c'est plus compliqué que ça, il faut voir si on peut ajouter une ligne
		//à cet endroit. À terminer.
	}
	
	public boolean mergeCells(int startRow, int startCol, int endRow, int endCol){

		for(int i = startRow ; i <= endRow ; i++){
			Row r = rows.get(i);
			if(r == null) return false; //Oops... No such row! Something went wrong.
			
			boolean foundStart = false;
			boolean foundEnd = false;
			for(Cell c: r.getCells()){
				int start = r.getPosition(c);
				int end = start + c.getColSpan();
				
				//First row? Let's check that no interesting cell starts higher than us.
				if(i == startRow && start >= startCol && end <= endCol){
					int upperOverFlow = i - c.getRow().getNumber();
					if(upperOverFlow > 0) return false;
				}
				
				//Last row? Let's check that no interesting cell ends lower than us.
				if(i == startRow && start >= startCol && end <= endCol){
					int lowerOverFlow = c.getRowSpan() + c.getRow().getNumber() - i;
					if(lowerOverFlow > 1) return false;
				}
				
				//Normal row? Don't care what's inside.
				
				//Check that we found a cell that starts at the right place
				foundStart = foundStart || (start == startCol);
				//And one that ends at the right place
				foundEnd = foundEnd || (end == endCol);
			}
		
			//Ok, by now we know that we can merge those cells.
			//We'll start horizontally by increasing the colspan of our first cell.
			//But to do that, we will ensure that our action only happens on the first line.
			//So let's first split every cell in the line
			r = rows.get(startRow);
			for(Cell c: r.getCells()){
				int start = r.getPosition(c);
				int end = start + c.getColSpan();
				if(start >= startCol && end <= endCol){
					c.splitHorizontally(1, c.getRowSpan() - 1);
				}
			}
			
			//Now the actual extension
			Cell c = r.findPosition(startCol);
			int increment = endCol - startCol - c.getColSpan();
			System.out.println("Je dois augmenter horizontalement de " + increment);
			c.incColSpan(increment);
				
			//Remaining cells have been automatically removed by the row.
			
			increment = endRow - startRow;
			//Now all we need to do is make our cell tall enough.
			c.incRowSpan(increment);
		
			//Remaining cells have been automatically removed by the cells.
		
			return true;
		}
		
		return false;
			 
	}
	
	/**
	 * Returns the successor of a row
	 * @param row the original row
	 * @return the successor
	 */
	public Row getNextRow(Row row){
		int pos = rows.indexOf(row);
		if(pos == -1)
			return null;
		if(pos == rows.size() - 1)
			return null;
		return rows.get(pos + 1);
	}
	
	/**
	 * Returns the predecessor of a row
	 * @param row the original row
	 * @return the successor
	 */
	public Row getPreviousRow(Row row){
		int pos = rows.indexOf(row);
		if(pos == -1)
			return null;
		if(pos == 0)
			return null;
		return rows.get(pos - 1);
	}
	
	/**
	 * Returns the index of the row within the table
	 * @param row the row
	 * @return the index
	 */
	public int getRowNumber(Row row){
		return rows.indexOf(row);
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @return the cell at this position
	 */
	public Cell getCell(int row, int col){
		return rows.get(row).findPosition(col);
	}
	
	/**
	 * 
	 * @return the rows within the table
	 */
	public LinkedList<Row> getRows(){
		return this.rows;
	}

}
