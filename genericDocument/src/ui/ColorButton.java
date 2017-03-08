package ui;

import java.awt.Color;
import java.util.HashSet;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JColorChooser;

import styles.Property;

public class ColorButton extends EditorButton {
	
	public Color color = new Color(0, 0, 0);

	public ColorButton(Property property) {
		super(property);
		// TODO Auto-generated constructor stub
	}

	public ColorButton(Icon arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ColorButton(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ColorButton(Action arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ColorButton(String arg0, Icon arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Property getProperty(){
		Color newColor = JColorChooser.showDialog(
                this,
                "Choose color",
                color);
		this.setColor(newColor);
		System.out.println("ma nouvelle couleur est" + color);
		return property;
	}
	
	public void setColor(Color c){
		System.out.println("setColor");
		color = c;
		String value = "rgb(" + color.getRed()+  ", " + color.getGreen() + ", " + color.getBlue() + ")";
		HashSet<String> h = new HashSet<String>();
		//h.add(value);
		property = new Property("color", value, h);
		System.out.println("propriété initialisée");
	}

}
