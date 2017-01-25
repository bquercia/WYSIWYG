/**
 * 
 */
package content;

import styles.Style;

/**
 * TextRun is an implementation of Run, which contains text.
 * @author Bruno Quercia
 *
 */
public class TextRun extends Run {
	
	private String text;

	/**
	 * Creates a text run with a given style and text
	 * @param style the style
	 * @param text the text
	 */
	public TextRun(Style style, String text) {
		super(style);
		this.text = text;
	}

	/**
	 * Creates a text run with a given text. Style will be default
	 * @param text the text
	 */
	public TextRun(String text) {
		this.text = text;
	}
	
	/**
	 * Creates an empty text run with no specific style
	 */
	public TextRun(){
		this.text = "";
	}
	
	/**
	 * 
	 * @return the text content of the run
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text content of the run
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

}
