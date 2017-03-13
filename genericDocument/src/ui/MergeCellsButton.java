package ui;

import java.util.LinkedList;

import javax.swing.JButton;

import content.Cell;
import content.Element;
import content.Row;
import content.Table;

public class MergeCellsButton extends ActionButton {
	Editor e;
	public MergeCellsButton(Editor e) {
		this.e = e;
	}
	
	public void action(){
		LinkedList<Element> elements = e.getElements();
		if(elements.size() == 1 && elements.get(0).getClass() == Table.class){
			Table t = (Table)elements.get(0);
			int offset = e.getDocumentModel().getStartOffset(t);
			int start = e.getSelectionStart() - offset;
			int end = e.getSelectionEnd() - offset;
			System.out.println("start : " + start);
			System.out.println("end : " + end);
			Cell startCell = e.getDocumentModel().getCellFromOffset(t, start);
			Cell endCell = e.getDocumentModel().getCellFromOffset(t, end);
			System.out.println(startCell.getRow());
			System.out.println(endCell.getRow());
			Row startRow = startCell.getRow();
			Row endRow = endCell.getRow();
			System.out.println("Je veux fusionner les cellules " + startRow.getNumber() + ", " + startRow.getPosition(startCell) + " et " + endRow.getNumber() + ", " + endRow.getPosition(endCell));
			t.mergeCells(startRow.getNumber(), startRow.getPosition(startCell) - 1 , endRow.getNumber(), endRow.getPosition(endCell) - 1);
		}
	}
}
