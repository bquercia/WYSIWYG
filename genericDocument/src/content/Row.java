/**
 * 
 */
package content;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Bruno Quercia
 *
 */
public class Row {
	private LinkedList<Cell> cells;
	private Table table;
	private int length;
	/**
	 * 
	 */
	public Row(Table table) {
		cells = new LinkedList<Cell>();
		this.table = table;
	}
	
	public Row(Table table, int nbCells) {
		cells = new LinkedList<Cell>();
		for(int i = 0 ; i < nbCells ; i++){
			cells.add(new Cell(this));
		}
		this.table = table;
	}
	
	public void addCell(int position){
		Cell c = this.findPosition(position);
		Cell newCell = new Cell(this);
		if(c == null)
			cells.add(newCell);
		else
			cells.add(cells.indexOf(c)+  1, newCell);
		table.addColumn(position, cells.size());
	}
	
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
	
	public int getSize(){
		int currentSize = 0;
		for(Cell c: cells){
			currentSize += c.getColSpan();
		}
		return currentSize;
	}
	
	public Row getSuccessor(){
		return table.getNextRow(this);
	}
	
	public LinkedList<Cell> getCells(){
		return this.cells;
	}
	
	public int getPosition(Cell cell){
		int currentPosition = 0;
		for(Cell c: cells){
			if(c == cell)
				return currentPosition;
			currentPosition += c.getColSpan();
		}
		return -1;
	}
	
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
	
	public boolean contains(Cell cell){
		return cells.contains(cell);
	}
	
	protected int getNumber(){
		return this.table.getRowNumber(this);
	}
	
	public Row getPredecessor(){
		return this.table.getPreviousRow(this);
	}
	
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
