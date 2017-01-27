/**
 * 
 */
package content;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import styles.InlineStyle;
import styles.Property;
import styles.Style;
import util.Translator;

/**
 * A Document is the abstract representation of a... Document. Pretty simple, hey?
 * It consists of Elements.
 * @author Bruno Quercia
 * 
 */
public class Document {
	private LinkedList<Element> elements;
	private LinkedList<DocumentListener> listeners;
	
	/**
	 * Creates an empty Document
	 */
	public Document() {
		this.elements = new LinkedList<Element>();
		this.listeners = new LinkedList<DocumentListener>();
	}
	
	/**
	 * Creates a document with pre-existing elements
	 * @param elements the elements to be added to the document
	 */
	public Document(LinkedList<Element> elements){
		this.elements = elements;
		this.listeners = new LinkedList<DocumentListener>();
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
	
	/**
	 * Inserts some text at some offset position.
	 * @param text the text to be inserted
	 * @param offset the offset position
	 */
	public void insert(String text, int offset){
		int currentPosition = 0;
		System.out.println("Début de l'insertion dans le modèle");
		//Let's go through the arborescence to find the run that we want to change
		for(Element e: this.getElements()){
			//Paragraphs - Tables will be handled later
			System.out.println("Examen d'un élément");
			if(e.getClass() == Paragraph.class){
				System.out.println("L'élément est un paragraphe");
				Paragraph p = (Paragraph)e;
				currentPosition = insertIntoParagraph(p, offset, currentPosition, text);
				if(currentPosition == -1)
					break;
			}
			else if(e.getClass() == Table.class){
				for(Row r: ((Table)e).getRows()){
					for(Cell c: r.getOwnCells()){
						for(Paragraph p: c.getParagraphs()){
							currentPosition = insertIntoParagraph(p, offset, currentPosition, text);
							if(currentPosition == -1)
								break;
						}
					}
				}
			}
		}
		for(DocumentListener l: listeners){
			l.contentUpdate();
		}
	}
	
	/**
	 * Submethod of insert, avoids repetition of code.
	 * It is used to locate the paragraph and insert the text in it.
	 * @param p paragraph to be examined
	 * @param offset same as insert
	 * @param currentPosition current position of the search process
	 * @param text same as insert
	 * @return the new value of currentPosition, or -1 if insertion has been achieved.
	 */
	private int insertIntoParagraph(Paragraph p, int offset, int currentPosition, String text){
		ListIterator<Run> i = (ListIterator) p.getRuns().iterator();
		while(i.hasNext()){
			Run r = i.next();
			System.out.println("Examen d'un run");
			if(r.getClass() == TextRun.class){
				System.out.println("Le run est un TextRun");
				TextRun tr = (TextRun)r;
				//Let's check if the offset is within this run.
				int l = tr.getText().length();
				currentPosition += l;
				System.out.println("Position courante : " + currentPosition + ", position recherchée : " + offset);
				//If it is, this is our guy
				if(currentPosition >= offset){
					System.out.println("La position a été trouvée");
					//Insertion position within the text
					int insertionPosition = l - currentPosition + offset;
					//Let's split the text around this position
					String start = tr.getText().substring(0, insertionPosition);
					String end = tr.getText().substring(insertionPosition);
					//And then let's recompose it with the inserted text.
					tr.setText(start + text + end);
					System.out.println(tr.getText());
					return -1;
				}
				//If not, let's look at the next run.
			}
			else
			{
				//This is an ImgRun. Its length is 1.
				currentPosition++;
				if(currentPosition >= offset){
					TextRun run = new TextRun();
					run.setText(text);
					i.add(run);
				}
			}
		}
		//End of paragraph = \n, thus one more character
		currentPosition ++;
		return currentPosition;
	}
	
	/**
	 * Adds a listener to the document
	 * @param listener a DocumentListener
	 * @return success
	 */
	public boolean addListener(DocumentListener listener){
		return listeners.add(listener);
	}
	
	/**
	 * Adds a property to a given portion of content.
	 * @param property the property to be added
	 * @param start the beginning of the portion
	 * @param end the end of the portion
	 */
	public void setProperty(Property property, int start, int end){
		int currentPosition = 0;
		//Let's go through the arborescence to find the runs that we want to change
		for(Element e: this.getElements()){
			//Paragraphs - Tables will be handled later
			System.out.println("Examen d'un élément");
			if(e.getClass() == Paragraph.class){
				System.out.println("L'élément est un paragraphe");
				Paragraph p = (Paragraph)e;
				ListIterator<Run> i = (ListIterator) p.getRuns().iterator();
				while(i.hasNext()){
					Run r = i.next();
					//TextRuns - ImgRuns will be handled later
					System.out.println("Examen d'un run");
					if(r.getClass() == TextRun.class){
						System.out.println("Le run est un TextRun");
						TextRun tr = (TextRun)r;
						//Let's check if the offset is within this run.
						int l = tr.getText().length();
						currentPosition += l;
						System.out.println("Position courante : " + currentPosition + ", position recherchée : " + start);
						//If it is, this is our guy
						if(currentPosition >= start){
							System.out.println("La position de départ a été trouvée");
							//Start position within the text
							int startPosition = l - currentPosition + start;
							//Let's split the text around this position
							String textStart = tr.getText().substring(0, startPosition);
							String textEnd = tr.getText().substring(startPosition);
							//The current run will only keep the beginning of the text.
							tr.setText(textStart);
							//Ok, now let's create a run that has our new property, plus the properties of tr.
							InlineStyle s = new InlineStyle();
							s.setParent(tr.getStyle());
							TextRun newRun = new TextRun(s, "");
							newRun.addProperty(property);
							//Is the rest less, more, or exactly what we want?
							//If less
							if(textEnd.length() < end - start + 1){
								System.out.println("Il y aura d'autres runs à explorer");
								//All the rest of the text goes into our new run
								newRun.setText(textEnd);
								//And we insert it after tr
								i.add(newRun);
								//The exploration of the arborescence will go on.
								//The next run to be explored will be newRun.
								//We don't want to change it, so let's change our start position
								start += textEnd.length();
							}
							//If exactly
							else if(textEnd.length() == end - start + 1){
								System.out.println("La modification va pile jusqu'à la fin du run");
								//All the rest of the text goes into our new run
								newRun.setText(textEnd);
								//And we insert it after tr
								p.insertRunAfter(newRun, tr);
								//We're done! No other run is needed.
								break;
							}
							//If more
							else{
								System.out.println("La modification ne va pas tout à fait jusqu'à la fin du run");
								//Let's split the remaining text into two parts:
								//What must change, and what must remain the same.
								String changingText = textEnd.substring(0, end - start);
								textEnd = textEnd.substring(end - start);
								//What must change goes into our new run
								newRun.setText(changingText);
								p.insertRunAfter(newRun, tr);
								//The rest goes into a run similar to tr
								TextRun lastRun = new TextRun(tr.getStyle(), textEnd);
								p.insertRunAfter(lastRun, newRun);
								//We're done!
								break;
							}
						}
						//If not, let's look at the next run.
					}
				}
				//End of paragraph = \n, thus one more character
				currentPosition ++;
			}
		}
		for(DocumentListener l: listeners){
			l.contentUpdate();
		}
	}
}