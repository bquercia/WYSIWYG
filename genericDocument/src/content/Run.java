package content;

import styles.InlineStyle;
import styles.Property;
import styles.Style;

/**
 * Run represents an elementary bit of content. It can either be an image, or text.
 * @author Bruno Quercia
 *
 */
public abstract class Run {
	private Style style;
	/**
	 * Creates a run with a given style.
	 * @param style the style to be applied to the run
	 */
	public Run(Style style) {
		this.style = style;
	}
	
	/**
	 * Creates a run with no specific style. Its style will be default.
	 */
	public Run(){
		this.style = new InlineStyle();
	}
	
	/**
	 * 
	 * @return the style of the run
	 */
	public Style getStyle(){
		return this.style;
	}
	
	/**
	 * Sets the style of the run to a desired style
	 * If the run had a style, it will be entirely erased.
	 * @param s the desired style
	 */
	public void setStyle(Style s){
		this.style = s;
	}
	
	/**
	 * Adds a new property to the style.
	 * @param p property to be added
	 * @return success
	 */
	public boolean addProperty(Property p){
		return this.style.addProperty(p);
	}

}
