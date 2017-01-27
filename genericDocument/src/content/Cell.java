/**
 * 
 */
package content;

import java.util.LinkedList;

/**
 * @author Bruno Quercia
 *
 */
public class Cell {
	private LinkedList<Paragraph> paragraphs;
	private int rowspan;
	private int colspan;
	Row row;
	/**
	 * 
	 */
	public Cell(Row row) {
		this.rowspan = 1;
		this.colspan = 1;
		this.row = row;
		this.paragraphs = new LinkedList<Paragraph>();
	}
	
	public Cell(Row row, int colspan, int rowspan) {
		this.row = row;
		this.colspan = colspan;
		this.rowspan = rowspan;
		this.paragraphs = new LinkedList<Paragraph>();
	}
	
	public boolean addParagraph(Paragraph paragraph){
		return this.paragraphs.add(paragraph);
	}
	
	public LinkedList<Paragraph> getParagraphs(){
		return paragraphs;
	}

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
			r.getSuccessor().addCellByRowSpanFusion(this);
		}
		return true;
	}
	
	protected boolean splitHorizontally(int firstBlockSize, int secondBlockSize){
		if(firstBlockSize + secondBlockSize == rowspan){
			this.rowspan = firstBlockSize;
			Row r = row;
			for(int i = 0 ; i < firstBlockSize ; i++){
				r = r.getSuccessor();
			}
			for(int i = 0 ; i < secondBlockSize ; i++){
				r.changeCellByHorizontalSplit(this);
			}
			return true;
		}
		return false;
	}
	
	public int getColSpan(){
		return this.colspan;
	}
	public int getRowSpan(){
		return this.rowspan;
	}
	public Row getRow(){
		return this.row;
	}

}
