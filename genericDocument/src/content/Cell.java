/**
 * 
 */
package content;

import java.util.LinkedList;

/**
 * A cell is the basis element of a table.
 * A cell has to be attached to a row.
 * A cell has a colspan and a rowspan.
 * A cell should not be created by anything else than its parent row.
 * A cell that has not been created by its parent will be ignored by it.
 * @author Bruno Quercia
 *
 */
public class Cell {
	private LinkedList<Paragraph> paragraphs;
	private int rowspan;
	private int colspan;
	private Row row;
	/**
	 * Creates a cell attached to a row, with a colspan and rowspan 1.
	 * @param row the parent row
	 */
	public Cell(Row row) {
		this.rowspan = 1;
		this.colspan = 1;
		this.row = row;
		this.paragraphs = new LinkedList<Paragraph>();
		paragraphs.add(new Paragraph());
	}
	
	/**
	 * Creates a cell with a given colspan and a given rowspan with a parent row.
	 * @param row
	 * @param colspan
	 * @param rowspan
	 */
	public Cell(Row row, int colspan, int rowspan) {
		this.row = row;
		this.colspan = colspan;
		this.rowspan = rowspan;
		this.paragraphs = new LinkedList<Paragraph>();
	}
	
	/**
	 * Adds a paragraph within the cell
	 * @param paragraph
	 * @return success
	 */
	public boolean addParagraph(Paragraph paragraph){
		return this.paragraphs.add(paragraph);
	}
	
	/**
	 * 
	 * @return the paragraphs contained in the cell
	 */
	public LinkedList<Paragraph> getParagraphs(){
		return paragraphs;
	}
	
	/**
	 * Increases the rowspan of the cell if this operation is validated by the parent row and the rows this cell will be extended to
	 * @param increment the value to add to the rowspan
	 * @return success
	 */
	public boolean incRowSpan(int increment){
		Row r = row;
		System.out.println("Je veux m'étendre de " + increment);
		//Let's calculate our position
		int start = row.getPosition(this);
		//Let's ask every row under this cell if increasing row span is possible
		for(int i = 0 ; i < increment ; i++){
			System.out.println("Nouvelle ligne trouvée : je devrai la prévenir");
			r = r.getSuccessor();
			if(r == null)
				return false;
			LinkedList<Cell> l = r.getCells();
			//Let's see if we can find a whole number of cells at the position we want to occupy.
			//For that, we must check that a cell starts at position start
			//and that another cell starts at position start + colspan.
			boolean startCellFound = false;
			boolean endCellFound = false;
			boolean allCellsCorrect = true;
			int currentPosition = 0;
			for(Cell c: l){
				startCellFound = startCellFound || currentPosition == start;
				//We must also check that the cells that will be replaced goes not too far down
				if(startCellFound && !endCellFound){
					allCellsCorrect = allCellsCorrect
									&& c.getRowSpan() + r.getNumber() - c.getRow().getNumber() <= increment - i;
				}
				endCellFound = endCellFound || currentPosition == start + colspan;
				currentPosition += c.getColSpan();
			}
			if(!startCellFound || !endCellFound)
				return false;
		}
		this.rowspan += increment;
		System.out.println("Mon nouveau rowspan est " + this.getRowSpan());
		r = row;
		for(int i = 0 ; i < increment ; i++){
			r = r.getSuccessor();
			r.addCellByRowSpanFusion(this);
		}
		return true;
	}
	
	/**
	 * Increases the colspan of a cell. This will trigger a fusion with overlapping cells.
	 * The cell will only grow if the structure of the table enables it.
	 * Otherwise, the operation fails and nothing is done.
	 * @param increment the increment
	 * @return success
	 */
	public boolean incColSpan(int increment){
		System.out.println("Je veux m'étendre horizontalement de " + increment);
		//This function is only valid for a simple cell of rowspan 1
		if(rowspan > 1) return false;
		//Let's check that a cell starts at this position
		//(ie that we are not trying to land in the middle of a cell)
		//For that we first determine where we are going to land
		//Then we find the cell associated to this position or as close as possible to it
		//Then we get the actual position of this cell
		//If the cell is exactly at the right place, then we can carry on.
		//If not, let's check, this could be the end of the line.
		int myPosition = row.getPosition(this);
		System.out.println("ma position : " + myPosition);
		int endPosition = row.getPosition(row.findPosition(myPosition - 1 + colspan + increment));
		System.out.println("Taille de la ligne : " + row.getSize());
		System.out.println("endPosition : " + endPosition);
		if(endPosition == myPosition + colspan + increment
		|| row.getSize() == myPosition - 1 + colspan + increment){
			System.out.println("Je peux");
			this.colspan += increment;
			row.removeCellsByColSpanFusion(this, increment);
			return true;
		}
		System.out.println("Je ne peux pas");
		return false;
	}
	
	/**
	 * Splits the cell into two cells above each other.
	 * The respective sizes of the two new cells, ie their rowspans, must add up to the of the original cell.
	 * @param firstBlockSize the size of the first block
	 * @param secondBlockSize the size of the second block
	 * @return success
	 */
	protected boolean splitHorizontally(int firstBlockSize, int secondBlockSize){
		//First, let's check that the parameters are correct
		if(firstBlockSize + secondBlockSize == rowspan && firstBlockSize > 0 && secondBlockSize > 0){
			this.rowspan = firstBlockSize;
			Row r = row;
			//Ok, we have reduced our rowspan.
			//We must now warn the rows we no longer occupy that they must append a new cell to replace us.
			for(int i = 0 ; i < firstBlockSize ; i++){
				r = r.getSuccessor();
			}
			//r is now the first line affected by our shrinking.
			for(int i = 0 ; i < secondBlockSize ; i++){
				r.changeCellByHorizontalSplit(this);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return the colspan of the cell
	 */
	public int getColSpan(){
		return this.colspan;
	}
	
	/**
	 * 
	 * @return the rowspan of the cell
	 */
	public int getRowSpan(){
		return this.rowspan;
	}
	
	/**
	 * 
	 * @return the parent row
	 */
	public Row getRow(){
		return this.row;
	}

}
