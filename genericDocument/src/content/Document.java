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
	 * Adds a paragraph to the document
	 * @return success
	 */
	public boolean createParagraph(){
		return elements.add(new Paragraph());
	}
	
	/**
	 * Adds an element at a given position (offset) in the document.
	 * This will have the effect of splitting the element containing the offset position into two parts,
	 * one part before the added element, and the other one after it.
	 * @param element the element to be added
	 * @param position the adding position
	 */
	public void addElement(Element element, int position){
		int currentPosition = 0;
		//Let's go through the arborescence to find the runs that we want to change
		for(Element e: this.getElements()){
			//Paragraphs - Tables will be handled later
			System.out.println("Examen d'un élément");
			if(e.getClass() == Paragraph.class){
				System.out.println("L'élément est un paragraphe");
				Paragraph p = (Paragraph)e;
				currentPosition = insertElementIntoParagraph(element, position, currentPosition, p);
				if(currentPosition == -1)
					return;
			}
			else if(e.getClass() == Table.class){
				for(Row r: ((Table)e).getRows()){
					for(Cell c: r.getOwnCells()){
						for(Paragraph p: c.getParagraphs()){
							System.out.println("Examen d'un paragraphe de la table");
							currentPosition = insertElementIntoParagraph(element, position, currentPosition, p);
							if(currentPosition == -1)
								return;
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
	 * Creates a paragraph with a given text and adds it to the document
	 * @param txt text
	 * @return success
	 */
	public boolean createParagraph(String txt){
		return elements.add(new Paragraph(txt));
	}
	/**
	 * Creates a 3*2 table and adds it to the document
	 * @return success
	 */
	public boolean createTable(){
		return elements.add(new Table(3, 2));
	}
	
	public boolean createTable(int cols, int rows){
		return elements.add(new Table(cols, rows));
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
				if(currentPosition == -1){
					for(DocumentListener l: listeners){
						l.contentUpdate();
					}
					return;
				}
			}
			else if(e.getClass() == Table.class){
				System.out.println("L'élément est une table");
				for(Row r: ((Table)e).getRows()){
					for(Cell c: r.getOwnCells()){
						for(Paragraph p: c.getParagraphs()){
							System.out.println("Examen d'un paragraphe de la table");
							currentPosition = insertIntoParagraph(p, offset, currentPosition, text);
							if(currentPosition == -1){
								for(DocumentListener l: listeners){
									l.contentUpdate();
								}
								return;
							}
						}
					}
				}
			}
		}
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
				currentPosition = setPropertyIntoParagraph(start, end, property, currentPosition, p);
				if(currentPosition == -1 || currentPosition >= end){
					for(DocumentListener l: listeners){
						l.contentUpdate();
					}
					return;
				}
				else if(currentPosition >= start){
					start = currentPosition;
				}
			}
			else if(e.getClass() == Table.class){
				for(Row r: ((Table)e).getRows()){
					for(Cell c: r.getOwnCells()){
						for(Paragraph p: c.getParagraphs()){
							System.out.println("Examen d'un paragraphe de la table");
							currentPosition = setPropertyIntoParagraph(start, end, property, currentPosition, p);
							if(currentPosition == -1 || currentPosition >= end){
								for(DocumentListener l: listeners){
									l.contentUpdate();
								}
								return;
							}
							else if(currentPosition >= start){
								start = currentPosition;
							}
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
	 * Deletes content of a given length starting from a given offset
	 * @param offset the offset
	 * @param length the length to delete
	 */
	public void delete(int offset, int length){
		int currentPosition = 0;
		//Let's go through the arborescence to find the runs that we want to change
		for(Element e: this.getElements()){
			//Paragraphs - Tables will be handled later
			System.out.println("Examen d'un élément");
			if(e.getClass() == Paragraph.class){
				System.out.println("L'élément est un paragraphe");
				Paragraph p = (Paragraph)e;
				currentPosition = deleteIntoParagraph(currentPosition, offset, length, p);
				if(currentPosition == -1 || currentPosition == offset + length){
					for(DocumentListener l: listeners){
						l.contentUpdate();
					}
					return;
				}
				else if(currentPosition >= offset){
					length -= currentPosition - offset;
					offset += currentPosition;
				}
			}
			//Table
			else if(e.getClass() == Table.class){
				Table t = (Table)e;
				for(Row r: t.getRows()){
					for(Cell c: r.getCells()){
						for(Paragraph p: c.getParagraphs()){
							currentPosition = deleteIntoParagraph(currentPosition, offset, length, p);
							if(currentPosition == -1 || currentPosition == offset + length){
								for(DocumentListener l: listeners){
									l.contentUpdate();
								}
								return;
							}
							else if(currentPosition >= offset){
								length -= currentPosition - offset;
								offset += currentPosition;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Finds the main elements between two positions
	 * @param start
	 * @param end
	 * @return
	 */
	public LinkedList<Element> getElements(int start, int end){
		int currentPosition = 0;
		LinkedList<Element> result = new LinkedList<Element>();
		//Let's go through the arborescence to find the runs that we want to change
		for(Element e: this.getElements()){
			//Paragraphs - Tables will be handled later
			System.out.println("Examen d'un élément");
			currentPosition += length(e);
			if(currentPosition >= start){
				result.add(e);
			}
			if(currentPosition >= end){
				return result;
			}
		}
		return result;
	}
	
	/**
	 * Gives the offset (position) at which an element starts.
	 * @param e the element
	 * @return the offset
	 */
	public int getStartOffset(Element e){
		if(elements.contains(e)){
			int result = 0;
			//Let's go though the elements until we find the one we are looking for.
			for(Element elem: elements){
				if(elem != e){
					result += length(elem);
				}
				else return result;
			}
			//We shouldn't need this, but just in case
			return -1;
		}
		else return -1;
	}
	
	/**
	 * Fetches a cell from a given offset
	 * @param t the table in which the cell must be retrieved
	 * @param offset the offset within the editor at which the cell should be located
	 * @return the cell, null if no cell exists within this table at this position.
	 */
	public Cell getCellFromOffset(Table t, int offset){
		int currentPosition = 0;
		for(Row r: t.getRows()){
			System.out.println("Je change de ligne");
			for(Cell c: r.getOwnCells()){
				System.out.println("J'étudie une nouvelle cellule : " + c.getRow().getNumber() + ", " + c.getRow().getPosition(c));
				for(Paragraph p: c.getParagraphs()){
					currentPosition += length(p);
				}
				if(currentPosition >= offset){
					System.out.println("cellule trouvée");
					return c;
				}
			}
		}
		return null;
	}
	
	//Private auxiliary methods

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
				System.out.println("après l'image, la position est de " + currentPosition);
				if(currentPosition >= offset){
					TextRun run = new TextRun();
					run.setText(text);
					i.add(run);
					return -1;
				}
			}
		}
		//End of paragraph = \n, thus one more character
		currentPosition ++;
		return currentPosition;
	}
	
	/**
	 * Sets a given property in a given paragraph. Auxiliary function.
	 * @param start
	 * @param end
	 * @param property
	 * @param currentPosition
	 * @param p
	 * @return
	 */
	private int setPropertyIntoParagraph(int start, int end, Property property, int currentPosition, Paragraph p){
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
						System.out.println("nouvelle position de départ : " + start);
					}
					//If exactly
					else if(textEnd.length() == end - start){
						System.out.println("La modification va pile jusqu'à la fin du run");
						//All the rest of the text goes into our new run
						newRun.setText(textEnd);
						//And we insert it after tr
						p.insertRunAfter(newRun, tr);
						//We're done! No other run is needed.
						return -1;
					}
					//If more
					else{
						System.out.println("La modification ne va pas tout à fait jusqu'à la fin du run");
						//Let's split the remaining text into two parts:
						//What must change, and what must remain the same.
						System.out.println("allô");
						String changingText = textEnd.substring(0, end - start);
						System.out.println("Changing text : " + changingText);
						textEnd = textEnd.substring(end - start);
						//What must change goes into our new run
						newRun.setText(changingText);
						p.insertRunAfter(newRun, tr);
						//The rest goes into a run similar to tr
						TextRun lastRun = new TextRun(tr.getStyle(), textEnd);
						p.insertRunAfter(lastRun, newRun);
						//We're done!
						return -1;
					}
				}
				//If not, let's look at the next run.
			}
			else if(r.getClass() == ImgRun.class){
				System.out.println("Le run est un ImgRun");
				ImgRun ir = (ImgRun)r;
				currentPosition ++;
				if(currentPosition == start){
					ir.addProperty(property);
					if(end == start)
						return -1;
					start ++;
				}
			}
		}
		//End of paragraph = \n, thus one more character
		currentPosition ++;
		return currentPosition;
	}
	
	/**
	 * Deletes content in a given paragraph. Auxiliary function.
	 * @param currentPosition
	 * @param offset
	 * @param length
	 * @param p
	 * @return
	 */
	private int deleteIntoParagraph(int currentPosition, int offset, int length, Paragraph p){
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
				System.out.println("Position courante : " + currentPosition + ", position recherchée : " + offset);
				//If it is, this is our guy
				if(currentPosition >= offset){
					System.out.println("La position de départ a été trouvée");
					//Start position within the text
					int startPosition = l - currentPosition + offset;
					//Let's split the text around this position
					String textStart = tr.getText().substring(0, startPosition);
					String textEnd = tr.getText().substring(startPosition);
					//Is the rest less, more, or exactly what we want?
					//If less
					if(textEnd.length() < length){
						length -= textEnd.length();
						System.out.println("Il y aura d'autres runs à explorer");
						//All the rest of the text is deleted
						//We just keep textStart
						tr.setText(textStart);
						//The exploration of the arborescence will go on.
						//The next run to be explored will be newRun.
						//We don't want to change it, so let's change our start position
						offset += textEnd.length();
					}
					//If exactly
					else if(textEnd.length() == length){
						System.out.println("La modification va pile jusqu'à la fin du run");
						//All the rest of the text is deleted
						//We just keep textStart
						tr.setText(textStart);
						//We're done.
						return -1;
					}
					//If more
					else{
						System.out.println("La modification ne va pas tout à fait jusqu'à la fin du run");
						//Let's split the remaining text into two parts:
						//What must be deleted, and what must be re-attached.
						textEnd = textEnd.substring(length);
						tr.setText(textStart + textEnd);
						//We're done!
						return -1;
					}
				}
				//If not, let's look at the next run.
			}
		}
		//End of paragraph = \n, thus one more character
		currentPosition ++;
		return currentPosition;
	}

	/**
	 * Inserts an element within a paragraph, if appropriate. Auxiliary function.
	 * @param e
	 * @param position
	 * @param currentPosition
	 * @param p
	 * @return
	 */
	private int insertElementIntoParagraph(Element e, int position, int currentPosition, Paragraph p){
		System.out.println("insertion d'un élément");
		ListIterator<Run> i = (ListIterator) p.getRuns().iterator();
		Paragraph newParagraph = new Paragraph();//TODO : gérer le style
		int addPosition = elements.indexOf(p) + 1;
		while(i.hasNext()){
			System.out.println("encore un run à examiner");
			System.out.println(currentPosition);
			Run r = i.next();
			if(currentPosition == -1){
				newParagraph.addRun(r);
				i.remove();
				System.out.println("run retiré");
			}
			else{
				if(r.getClass() == TextRun.class){
					System.out.println("Le run est un TextRun");
					TextRun tr = (TextRun)r;
					//Let's check if the offset is within this run.
					int l = tr.getText().length();
					currentPosition += l;
					System.out.println("Position courante : " + currentPosition + ", position recherchée : " + position);
					//If it is, this is our guy
					if(currentPosition >= position){
						System.out.println("La position de départ a été trouvée");
						//Start position within the text
						int startPosition = l - currentPosition + position;
						//Let's split the text around this position
						String textStart = tr.getText().substring(0, startPosition);
						String textEnd = tr.getText().substring(startPosition);
						//The current run will only keep the beginning of the text.
						tr.setText(textStart);
						//Ok, now let's create a run that has our new property, plus the properties of tr.
						InlineStyle s = new InlineStyle();
						s.setParent(tr.getStyle());
						TextRun newRun = new TextRun(s, textEnd);
						newRun.setText(textEnd);
						newParagraph.addRun(newRun);
						System.out.println("Dans le nouveau paragraphe, j'ajoute " + textEnd);
						//We're done! No other run is needed.
						currentPosition = -1;
					}
					//If not, let's look at the next run.
				}
				else if(r.getClass() == ImgRun.class){
					ImgRun ir = (ImgRun)r;
					currentPosition ++;
					if(currentPosition == position){
						currentPosition = -1;
					}
				}
				System.out.println("pos" + currentPosition);
			}
		}
		if(currentPosition == -1){
			System.out.println("ajout de l'élément");
			elements.add(addPosition, newParagraph);
			elements.add(addPosition, e);
			return -1;
		}
		//End of paragraph = \n, thus one more character
			currentPosition ++;
		return currentPosition;
	}
	
	/**
	 * Returns the length of a given element
	 * @param e the element
	 * @return its length
	 */
	private int length(Element e){
		int result = 1;
		if(e.getClass() == Paragraph.class){
			Paragraph p = (Paragraph)e;
			for(Run r: p.getRuns()){
				if(r.getClass() == TextRun.class){
					TextRun tr = (TextRun)r;
					result += tr.getText().length();
				}
				else{
					result ++;
				}
			}
		}
		else if(e.getClass() == Table.class){
			for(Row r: ((Table)e).getRows()){
				for(Cell c: r.getOwnCells()){
					for(Paragraph p: c.getParagraphs()){
						result += length(p);
					}
				}
			}
		}
		return result;
	}
}