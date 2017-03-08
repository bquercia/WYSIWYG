/**
 * 
 */
package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import styles.Property;

/**
 * @author Bruno Quercia
 *
 */
public class EditorButtonListener implements ActionListener {

	private EditorPanel panel;
	private EditorButton button;
	/**
	 * 
	 */
	public EditorButtonListener(EditorPanel panel, EditorButton button) {
		this.panel = panel;
		this.button = button;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Editor editor = panel.getEditor();
		int start = editor.getSelectionStart() - 1;
		int end = editor.getSelectionEnd() - 1;
		Property p = button.getProperty();
		System.out.println("La propriété renvoyée est : "+p.getValue());
		editor.getDocumentModel().setProperty(p, start, end);
		System.out.println(panel.getTranslator().generateHTML(editor.getDocumentModel()));
		editor.setText(panel.getTranslator().generateHTML(editor.getDocumentModel()));
	}

}
