package ui;

import java.awt.Color;
import java.util.HashSet;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import content.Document;
import content.Table;
import styles.Property;

public class AddTableButton extends ActionButton {
	Editor e;
	public AddTableButton(Editor e) {
		this.e = e;
	}
	
	public void action(){
		e.getDocumentModel().addElement(new Table(2, 3), e.getCaretPosition());
	}
}
