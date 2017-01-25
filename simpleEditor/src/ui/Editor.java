/**
 * 
 */
package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import content.Document;

/**
 * @author Bruno Quercia
 *
 */
public class Editor extends JEditorPane {
	Document document;
	
	/**
	 * Creates an empty Editor.
	 */
	public Editor(){
		super("text/html", "");
		this.document = new Document();
		this.getDocument().addDocumentListener(new EditorListener(this));
	}
	
	/**
	 * Creates an Editor with a Document to be displayed and edited.
	 * @param d the document.
	 */
	public Editor(Document d){
		super("text/html", "");
		this.document = d;
	}
	
	public Document getDocumentModel(){
		return document;
	}

}

class EditorListener implements DocumentListener{
	
	private Editor e;
	
	public EditorListener(Editor e){
		this.e = e;
	}
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		int offset = arg0.getOffset();
		int length = arg0.getLength();
		System.out.println("Une insertion de " + length + "caractères a été effectuée en position " + offset);		
		Element body = e.getDocument().getDefaultRootElement()
						.getElement(1);
		Element paragraph = body.getElement(0);
		if(offset != 0){
			String insertedText;
			try {
				insertedText = e.getText(offset, length);
				e.document.insert(insertedText, offset - length);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		
	}
	
}
