/**
 * 
 */
package test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
import util.Rule;
import util.Translator;

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
		JPanel panel = new JPanel();
		Editor editor = new Editor();
		Document document = new Document();
		Translator translator = new Translator();
		
		//Graphics
		panel.add(editor);
		window.setContentPane(panel);
		window.setSize(500, 650);
		
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
		
		//Translating the document
		String content = translator.generateHTML(document);
		
		//Display
		System.out.println(content);
		editor.setText(content);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

}
