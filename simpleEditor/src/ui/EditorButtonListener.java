/**
 * 
 */
package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		editor.getDocumentModel().setProperty(button.getProperty(), start, end);
		editor.setText(panel.getTranslator().generateHTML(editor.getDocumentModel()));
	}

}
