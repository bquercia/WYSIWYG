package ui;

import java.awt.Color;
import java.util.HashSet;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

import styles.Property;

public class FontSizeButton extends EditorButton{

	public FontSizeButton(Property property) {
		super(property);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Property getProperty(){
		String fontSize = (String) JOptionPane.showInputDialog(
				this,
                "Choose font size\n",
                "Font size",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
		this.setFontSize(fontSize);
		return property;
	}
	
	public void setFontSize(String fontSize){
		HashSet<String> h = new HashSet<String>();
		//h.add(value);
		property = new Property("font-size", fontSize, h);
		System.out.println("propriété initialisée");
	}

}
