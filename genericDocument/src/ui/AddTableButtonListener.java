package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTableButtonListener implements ActionListener {

	AddTableButton button;
	EditorPanel panel;
	public AddTableButtonListener(AddTableButton button, EditorPanel panel){
		this.button = button;
		this.panel = panel;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		button.addTable();
		panel.editor.setText(panel.getTranslator().generateHTML(panel.editor.getDocumentModel()));
	}

}
