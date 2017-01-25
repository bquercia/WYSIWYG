/**
 * 
 */
package test;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import content.Document;
import content.Paragraph;
import content.Run;
import content.TextRun;
import styles.InlineStyle;
import styles.Property;
import styles.Style;
import ui.Editor;
import ui.EditorPanel;
import util.Rule;
import util.Translator;
import content.DocumentListener;
import content.Element;
import content.ImgRun;

/**
 * @author Bruno Quercia
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame();
		JFrame monitor = new JFrame();
		Translator translator = new Translator();
		EditorPanel panel = new EditorPanel(translator);
		JPanel monitorPanel = new JPanel();
		JButton button = new JButton();
		Editor editor = panel.getEditor();
		JEditorPane monitorEditor = new JEditorPane("text/html", "");
		monitorEditor.setEditable(false);
		Document document = editor.getDocumentModel();
		
		//Graphics
		/*panel.add(editor);
		panel.add(button);
		button.setText("B");*/
		window.setContentPane(panel);
		window.setSize(500, 650);
		monitorPanel.add(monitorEditor);
		monitor.setContentPane(monitorPanel);
		monitor.setSize(500, 650);
		
		//Creating the document
		TextRun run = new TextRun(new InlineStyle(), "Premier run");
		HashSet<String> s = new HashSet<String>();
		s.add("bold");
		s.add("normal");
		Property p = new Property("font-weight", "bold", s);
		run.getStyle().addProperty(p);
		Paragraph para = new Paragraph();
		para.addRun(run);
		document.addElement(para);
		
		//Creating the translator
		Rule r = new Rule(p, "<span style='font-weight:bold'>", "</span>");
		translator.addRule(r);
		p = new Property("color", "red", new HashSet<String>());
		r = new Rule(p);
		translator.addRule(r);
		run = new TextRun(new InlineStyle(), "deuxième run");
		run.getStyle().addProperty(p);
		para.addRun(run);
			ImgRun ir = new ImgRun("file:///C:\\Users\\bruno_000\\Documents\\projet docx\\genericDocument\\image.jpg");
			ir.setStyle(new InlineStyle());
			para.addRun(ir);
		
		//Testing the setProperty function
		document.setProperty(new Property("color", "blue",  new HashSet<String>()), 2, 8);
		document.setProperty(new Property("color", "green", new HashSet<String>()), 9, 17);
		
		//Translating the document
		String content = translator.generateHTML(document);
		
		//Monitoring
		document.addListener(new DocumentMonitor(document, monitorEditor));
		
		//Display
		System.out.println(content);
		editor.setText(content);
		
		monitor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		monitor.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		//TODO créer un event sur le document qui permette de rafraîchir le monitor à chaque fois qu'une modification a été effectuée
	}

}

class DocumentMonitor implements DocumentListener{
	private Document d;
	private JEditorPane ed;
	public DocumentMonitor(Document document, JEditorPane editor){
		d = document;
		ed = editor;
		contentUpdate();
	}
	@Override
	public void contentUpdate() {
		String txt = "Document";
		for(Element e: d.getElements())	{
			txt += "<br/>&nbsp;&nbsp;&nbsp;" + e.getClass().getSimpleName();
			if(e.getClass() == Paragraph.class){
				for(Run r: ((Paragraph)e).getRuns()){
					txt += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + r.getClass().getSimpleName();
					if(r.getClass() == TextRun.class){
						txt += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+ ((TextRun)r).getText().replace(" ", "&nbsp;")
													.replace("\n", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					}
				}
			}
		}
		ed.setText(txt);
	}
	
}