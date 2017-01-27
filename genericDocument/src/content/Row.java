/**
 * 
 */
package content;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A row is a container of cells. It is part of a table.
 * A row should not be created outside its parent table. If it were, it would be ignored.
 * @author Bruno Quercia
 *
 */
public class Row {
	private LinkedList<Cell> cells;
	private Table table;
	private int length;
	/**
	 * Creates a new row with a parent table
	 * @param table the parent
	 */
	public Row(Table table) {
		cells = new LinkedList<Cell>();
		this.table = table;
	}
	
	/**
	 * Creates a new row with a parent table and a given number of cells
	 * @param table the parent
	 * @param nbCells the number of cells
	 */
	public Row(Table table, int nbCells) {
		cells = new LinkedList<Cell>();
		for(int i = 0 ; i < nbCells ; i++){
			cells.add(new Cell(this));
		}
		this.table = table;
	}
	
	/**
	 * Adds a cell at a given position in the row.
	 * If this operation makes the row bigger than the other ones of its table, then all others will append cells to compensate.
	 * @param position the position of the new cell within the row
	 */
	public void addCell(int position){
		Cell c = this.findPosition(position);
		Cell newCell = new Cell(this);
		if(c == null)
			cells.add(newCell);
		else
			cells.add(cells.indexOf(c)+  1, newCell);
		table.addColumn(position, cells.size());
	}
	
	/**
	 * Fetches the cell that starts at a given position
	 * or the first to start after this position if no cell starts there.
	 * @param position the position of the cell within the line
	 * @return
	 */
	public Cell findPosition(int position){
		int currentPosition = 0;
		System.out.println("Je recherche la position" + position);
		for(Cell c: cells){
			if(currentPosition >= position){
				return c;
			}
			currentPosition += c.getColSpan();
		}
		return null;
	}
	
	/**
	 * 
	 * @return the spatial size of the row.
	 */
	public int getSize(){
		int currentSize = 0;
		for(Cell c: cells){
			currentSize += c.getColSpan();
		}
		return currentSize;
	}
	
	/**
	 * 
	 * @return the next row in the table, or null if it is last.
	 */
	public Row getSuccessor(){
		return table.getNextRow(this);
	}
	
	/**
	 * 
	 * @return all the cells that occupy this row
	 */
	public LinkedList<Cell> getCells(){
		return this.cells;
	}
	
	/**
	 * 
	 * @param cell the cell we want to know the position of
	 * @return the spatial position of the cell
	 */
	public int getPosition(Cell cell){
		int currentPosition = 0;
		for(Cell c: cells){
			if(c == cell)
				return currentPosition;
			currentPosition += c.getColSpan();
		}
		return -1;
	}
	
	/**
	 * This function should only be called by a cell
	 * Its purpose is to make the necessary changes after a cell has increased rowspan
	 * @param cell the cell that has changed
	 */
	protected void addCellByRowSpanFusion(Cell cell){
		//Let's see if the cell can pretend to join us
		//If its start is closer to us than its rowspan, yes.
		//But first, let's check that the cell is not already one of us.
		System.out.println("Je suis la ligne " + this.getNumber() + " et on me demande d'entériner une fusion");
		if(!cells.contains(cell)){
			int start = table.getRowNumber(cell.getRow());
			if(start != -1){
				int distance = table.getRowNumber(this) - start;
				System.out.println("Le rowspan est de " + cell.getRowSpan());
				if(cell.getRowSpan() > distance){
					//Now we know we should indeed add this cell.
					//Let's clean the place by deleting as many cells as we need
					//so that there is a hole to put the new cell in.
					int currentPosition = 0;
					start = cell.getRow().getPosition(cell);
					int end = start + cell.getColSpan();
					ListIterator<Cell> i = (ListIterator<Cell>) cells.iterator();
					while(i.hasNext()){
						Cell c = i.next();
						//currentPosition is the start position of cell c
						//If it starts at the same place than the cell we want to insert
						//we insert the cell and remove the old one
						if(currentPosition == start){
							i.remove();
							i.add(cell);
						}
						//If it is in the zone but not at the start popsition
						//we simply remove it
						else if(currentPosition > start && currentPosition < end){
							i.remove();
						}
						currentPosition += c.getColSpan();
					}
					//All pre-existing cells have been deleted.
				}
			}
		}
	}
	
	/**
	 * This function should only be called by a cell.
	 * Its purpose is to make the necessary changes after a cell has been divided in two horizontally.
	 * @param cell the cell that was split
	 */
	protected void changeCellByHorizontalSplit(Cell cell){
		//A cell tells us it has shortened and no longer reaches down to us
		//Let's first check that
		int gap = this.getNumber() - cell.getRow().getNumber() + cell.getRowSpan();
		if(gap > 0){
			//Let's see where the old cell was
			int index = cells.indexOf(cell);
			//Now, let's see if we are the first row impacted
			if(gap == 1){
				//If so, we must append a new cell at the same index
				cells.remove(index);
				//In order to create the new cell, we must determine its new rowspan
				//Let's see how many rows are affected
				//That is, how many successors contained the old cell
				Row r = this;
				int rowspan = 1;
				while(r.getSuccessor() != null && r.getSuccessor().contains(cell)){
					rowspan ++;
					r = r.getSuccessor();
				}
				//Ok, now let's add the cell
				cells.add(index, new Cell(this, cell.getColSpan(), rowspan));
			}
			else{
				//If we are not the first row impacted, a new cell has been created
				//we just need to fetch it
				Row r = this;
				for(int i = gap ; i > 1 ; i++){
					r = r.getPredecessor();
				}
				//The new cell has the same position than the old one
				int position = this.getPosition(cell);
				//We can get rid of cell now
				cells.remove(cell);
				cells.add(index, r.findPosition(position));
				//And we're done!
			}
		}
	}
	
	/**
	 * 
	 * @param cell the cell we want to find
	 * @return true if and only if the cell occupies this row
	 */
	public boolean contains(Cell cell){
		return cells.contains(cell);
	}
	
	/**
	 * 
	 * @return the index within the table
	 */
	protected int getNumber(){
		return this.table.getRowNumber(this);
	}
	
	/**
	 * 
	 * @return the previous row within the table, or null if first row
	 */
	public Row getPredecessor(){
		return this.table.getPreviousRow(this);
	}
	
	/**
	 * 
	 * @return the cells that start at this row (not the cells that occupy it)
	 */
	public LinkedList<Cell> getOwnCells(){
		LinkedList<Cell> l = new LinkedList<Cell>();
		for(Cell c: cells){
			if(c.getRow() == this){
				l.add(c);
			}
		}
		return l;
	}

}
