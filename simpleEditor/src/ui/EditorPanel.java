/**
 * 
 */
package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import styles.Property;
import util.Translator;

/**
 * @author Bruno Quercia
 *
 */
public class EditorPanel extends JPanel {
	Editor editor;
	JPanel toolBar;
	Translator translator;
	
	public EditorPanel(Translator translator){
		this.editor = new Editor();
		this.toolBar = new JPanel();
		this.translator = translator;
		//Let's create the buttons
		EditorButton boldButton = new EditorButton(new Property("font-weight", "bold", new HashSet<String>()));
		boldButton.setText("B");
		EditorButton italicButton = new EditorButton(new Property("font-style", "italic", new HashSet<String>()));
		italicButton.setText("I");
		toolBar.add(boldButton);
		toolBar.add(italicButton);
		boldButton.addActionListener(new EditorButtonListener(this, boldButton));
		italicButton.addActionListener(new EditorButtonListener(this, italicButton));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(toolBar);
		this.add(editor);
	}
	
	public Editor getEditor(){
		return this.editor;
	}
	
	public Translator getTranslator(){
		return this.translator;
	}
}
