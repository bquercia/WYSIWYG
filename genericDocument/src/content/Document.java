/**
 * 
 */
package content;
import java.util.LinkedList;

import util.Translator;

/**
 * @author Bruno Quercia
 * A Document is the abstract representation of a... Document. Pretty simple, hey?
 * It consists of Elements.
 */
public class Document {
	private LinkedList<Element> elements;
	
	/**
	 * Creates an empty Document
	 */
	public Document() {
		this.elements = new LinkedList<Element>();
	}
	
	/**
	 * Creates a document with pre-existing elements
	 * @param elements the elements to be added to the document
	 */
	public Document(LinkedList<Element> elements){
		this.elements = elements;
	}

	/**
	 * 
	 * @return the elements of the document
	 */
	public LinkedList<Element> getElements() {
		return elements;
	}

	/**
	 * Changes the elements of the Document. All pre-existing elements will be erased.
	 * @param elements the new elements
	 */
	public void setElements(LinkedList<Element> elements) {
		this.elements = elements;
	}
	
	/**
	 * Adds an element to the Document
	 * @param e the element to be added
	 * @return success
	 */
	public boolean addElement(Element e){
		return elements.add(e);
	}

}
