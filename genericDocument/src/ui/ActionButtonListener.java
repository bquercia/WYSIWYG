package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An actionButtonListener listens to an ActionButton.
 * Its primary role is to fire the action when the button is clicked.
 * @author Bruno Quercia
 *
 */
public class ActionButtonListener implements ActionListener {

	ActionButton button;
	EditorPanel panel;
	public ActionButtonListener(ActionButton button, EditorPanel panel){
		this.button = button;
		this.panel = panel;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		button.action();
		panel.editor.setText(panel.getTranslator().generateHTML(panel.editor.getDocumentModel()));
	}

}
