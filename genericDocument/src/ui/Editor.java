/**
 * 
 */
package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

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
	
	public LinkedList<content.Element> getElements(){
		return document.getElements(this.getSelectionStart(), this.getSelectionEnd());
	}

}

class EditorListener implements DocumentListener{
	
	private Editor e;
	private int docLength;
	
	public EditorListener(Editor e){
		this.e = e;
		this.docLength = e.getDocument().getLength();
	}
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		System.out.println("changement");
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
				e.document.insert(insertedText, offset - 1);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		this.docLength = e.getDocument().getLength();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		System.out.println(arg0);
		int offset = arg0.getOffset();
		int length = arg0.getLength();
		System.out.println("Une suppression de " + length + "caractères a été effectuée en position " + offset);
		if(offset != 0){
			Element body = e.getDocument().getDefaultRootElement()
					.getElement(1);
			Element paragraph = body.getElement(0);
			e.document.delete(offset - 1, length);
		}
		this.docLength -= length;
		System.out.println("Longueur enregistrée : " + docLength);
		System.out.println("Longueur réelle : " + e.getDocument().getLength());
	}
	
}
