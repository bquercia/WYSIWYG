package simpleEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.ProtectDocument;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.STDocProtect;

import javax.swing.JButton;
import javax.swing.JEditorPane;

public class UserInterface extends JFrame {
	JPanel panel;
	JEditorPane txt;
	private StylesManager manager;
	public UserInterface(){
		//Let's draw a small window
		this.setTitle("Simple docx editor");
	    this.setSize(400, 500);
	    this.setLocationRelativeTo(null);
	    
	    //Now the content
	    panel = new JPanel();
	    
	    //Test text
	    String test = "Bonjour tout le monde, comment ça va ???";
	    manager = new StylesManager();
	    /*manager.insertTag(1,  4,  "<b>",  "</b>");
	    manager.insertTag(2,  7,  "<i>",  "</i>");
	    manager.insertTag(4,  5, "<span color='red'>", "</span>");
	    manager.insertTag(8,  10, "<span color='green'>", "</span>");
	    //manager.insertTag(2, 3, "<span style='font-weight:normal'>", "</span>");
	    String parsedText = manager.parseText(test);*/
	    String parsedText = "";
	    
	    //Formatted textarea fitting the window
	    txt = new JEditorPane("text/html", parsedText);
	    Dimension s = new Dimension();
	    s.setSize(this.getWidth()*0.9, this.getHeight()*0.8);
	    txt.setPreferredSize(s);
	    
	    //Button to make the text blue
	    JButton blueButton = new JButton("blue");
	    ButtonListener blueListener = new ButtonListener("<span style='color:blue'>", "</span>", txt, manager);
	    blueButton.addActionListener(blueListener);
	    
	    //Button to make the text bold
	    JButton boldButton = new JButton("bold");
	    ButtonListener boldListener = new ButtonListener("<b>", "</b>", txt, manager);
	    boldButton.addActionListener(boldListener);
	    
	    //Button to make the text not bold
	    JButton notBoldButton = new JButton("not bold");
	    ButtonListener notBoldListener = new ButtonListener("<span style='font-weight:normal'>", "</span>", txt, manager);
	    notBoldButton.addActionListener(notBoldListener);
	    
	    //Button to make the text italic
	    JButton italicButton = new JButton("italic");
	    ButtonListener italicListener = new ButtonListener("<i>", "</i>", txt, manager);
	    italicButton.addActionListener(italicListener);
	    
	    //Button to make the text not bold
	    JButton notItalicButton = new JButton("not italic");
	    ButtonListener notItalicListener = new ButtonListener("<span style='font-style:normal'>", "</span>", txt, manager);
	    notItalicButton.addActionListener(notItalicListener);
	    
	    //Save button
	    JButton save = new JButton("save");
	    SaveButtonListener saveListener = new SaveButtonListener(txt, manager);
	    save.addActionListener(saveListener);
	    
	    
	    //Now to display all this
	    this.setContentPane(panel);
	    panel.add(blueButton, BorderLayout.NORTH);
	    panel.add(boldButton, BorderLayout.NORTH);
	    panel.add(notBoldButton, BorderLayout.NORTH);
	    panel.add(italicButton, BorderLayout.NORTH);
	    panel.add(notItalicButton, BorderLayout.NORTH);
	    panel.add(save, BorderLayout.NORTH);
	    panel.add(txt, BorderLayout.SOUTH);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
	    this.setVisible(true);
	}
}


class SaveButtonListener implements ActionListener{
	JEditorPane jEP;
	StylesManager manager;
	public SaveButtonListener(JEditorPane jEP, StylesManager manager){
		this.jEP = jEP;
		this.manager = manager;
	}
	public void actionPerformed(ActionEvent e){
		WordprocessingMLPackage wordMLPackage;
		try {
			wordMLPackage = WordprocessingMLPackage.createPackage();
			MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
			mdp.addAltChunk(AltChunkType.Html, new String(
					"<html><body>" + manager.parseText(jEP.getDocument().getText(0, jEP.getDocument().getLength())
					+ "</html></body>")).getBytes());
			
			ProtectDocument protection = new ProtectDocument(wordMLPackage);
			protection.restrictEditing(STDocProtect.READ_ONLY, "foobaa");
			
			
			String filename = System.getProperty("user.dir") + "/protect.docx";
			Docx4J.save(wordMLPackage, new java.io.File(filename), Docx4J.FLAG_SAVE_ZIP_FILE); 
			
		} catch (InvalidFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Docx4JException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}


class ButtonListener implements ActionListener{

	JEditorPane jEP;
	StylesManager manager;
	String startTag, endTag;
	public ButtonListener(String startTag, String endTag, JEditorPane jEP, StylesManager manager){
		this.jEP = jEP;
		this.manager = manager;
		this.startTag = startTag;
		this.endTag = endTag;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String before, selection, after;
		int start = jEP.getSelectionStart();
		//selection = "<span color='blue'>" + jEP.getSelectedText() + "</span>";
		int end = jEP.getSelectionEnd();
		String txt;
		try {
			txt = jEP.getDocument().getText(0, jEP.getDocument().getLength());
			System.out.println(txt);
			manager.insertTag(start, end - 1, startTag, endTag);
			jEP.setText(manager.parseText(txt));
			//A carriage return is added, let's delete it.
				jEP.getDocument().remove(0, 1);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
