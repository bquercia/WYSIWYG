package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import content.Paragraph;

public class EditorKeyListener implements KeyListener {

	EditorPanel panel;
	public EditorKeyListener(EditorPanel panel){
		this.panel = panel;
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
			Editor e = panel.getEditor();
			if(arg0.getModifiers() == 0){
				e.getDocumentModel().addElement(new Paragraph(""), e.getCaretPosition());
				arg0.consume();
				e.setText(panel.getTranslator().generateHTML(e.getDocumentModel()));
			}
			else{
				e.getDocumentModel().insert("\n", e.getCaretPosition());
				e.setText(panel.getTranslator().generateHTML(e.getDocumentModel()));
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
