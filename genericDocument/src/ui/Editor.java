/**
 * 
 */
package ui;

import javax.swing.JEditorPane;

import content.Document;

/**
 * @author Bruno Quercia
 *
 */
public class Editor extends JEditorPane {
	private Document document;
	
	/**
	 * Creates an empty Editor.
	 */
	public Editor(){
		super("text/html", "");
		this.document = new Document();
	}
	
	/**
	 * Creates an Editor with a Document to be displayed and edited.
	 * @param d the document.
	 */
	public Editor(Document d){
		super("text/html", "");
		this.document = d;
	}

}
