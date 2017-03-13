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
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneLayout;

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
		ColorButton colorButton = new ColorButton(new Property("", "", new HashSet<String>()));
		colorButton.setText("color");
		EditorButton centerButton = new EditorButton(new Property("text-align", "center", new HashSet<String>()));
		centerButton.setText("center");
		toolBar.add(boldButton);
		toolBar.add(italicButton);
		toolBar.add(colorButton);
		//toolBar.add(centerButton);
		boldButton.addActionListener(new EditorButtonListener(this, boldButton));
		italicButton.addActionListener(new EditorButtonListener(this, italicButton));
		colorButton.addActionListener(new EditorButtonListener(this, colorButton));
		centerButton.addActionListener(new EditorButtonListener(this, centerButton));
		AddTableButton addTableButton = new AddTableButton(editor);
		addTableButton.addActionListener(new ActionButtonListener(addTableButton, this));
		addTableButton.setText("table");
		MergeCellsButton mergeCellsButton = new MergeCellsButton(editor);
		mergeCellsButton.addActionListener(new ActionButtonListener(mergeCellsButton, this));
		mergeCellsButton.setText("merge cells");
		toolBar.add(addTableButton);
		toolBar.add(mergeCellsButton);
		FontSizeButton fontSizeButton = new FontSizeButton(new Property("", "", new HashSet<String>()));
		fontSizeButton.setText("font size");
		fontSizeButton.addActionListener(new EditorButtonListener(this, fontSizeButton));
		toolBar.add(fontSizeButton);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(toolBar);
		JScrollPane scroll = new JScrollPane(editor);
		scroll.setLayout(new ScrollPaneLayout());
		this.add(scroll);
		editor.addKeyListener(new EditorKeyListener(this));
	}
	
	public Editor getEditor(){
		return this.editor;
	}
	
	public Translator getTranslator(){
		return this.translator;
	}
}
