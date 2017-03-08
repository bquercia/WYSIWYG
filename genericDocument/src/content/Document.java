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
	
	public boolean createParagraph(){
		return elements.add(new Paragraph());
	}
	
	public void addElement(Element element, int position){
		int currentPosition = 0;
		//Let's go through the arborescence to find the runs that we want to change
		for(Element e: this.getElements()){
			//Paragraphs - Tables will be handled later
			System.out.println("Examen d'un �l�ment");
			if(e.getClass() == Paragraph.class){
				System.out.println("L'�l�ment est un paragraphe");
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
	
	public boolean createParagraph(String txt){
		return elements.add(new Paragraph(txt));
	}
	
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
		System.out.println("D�but de l'insertion dans le mod�le");
		//Let's go through the arborescence to find the run that we want to change
		for(Element e: this.getElements()){
			//Paragraphs - Tables will be handled later
			System.out.println("Examen d'un �l�ment");
			if(e.getClass() == Paragraph.class){
				System.out.println("L'�l�ment est un paragraphe");
				Paragraph p = (Paragraph)e;
				currentPosition = insertIntoParagraph(p, offset, currentPosition, text);
				if(currentPosition == -1)
					return;
			}
			else if(e.getClass() == Table.class){
				System.out.println("L'�l�ment est une table");
				for(Row r: ((Table)e).getRows()){
					for(Cell c: r.getOwnCells()){
						for(Paragraph p: c.getParagraphs()){
							System.out.println("Examen d'un paragraphe de la table");
							currentPosition = insertIntoParagraph(p, offset, currentPosition, text);
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
			System.out.println("Examen d'un �l�ment");
			if(e.getClass() == Paragraph.class){
				System.out.println("L'�l�ment est un paragraphe");
				Paragraph p = (Paragraph)e;
				currentPosition = setPropertyIntoParagraph(start, end, property, currentPosition, p);
				if(currentPosition == -1 || currentPosition == end)
					return;
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
							if(currentPosition == -1 || currentPosition == end)
								return;
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
	
	public void delete(int offset, int length){
		int currentPosition = 0;
		//Let's go through the arborescence to find the runs that we want to change
		for(Element e: this.getElements()){
			//Paragraphs - Tables will be handled later
			System.out.println("Examen d'un �l�ment");
			if(e.getClass() == Paragraph.class){
				System.out.println("L'�l�ment est un paragraphe");
				Paragraph p = (Paragraph)e;
				currentPosition = deleteIntoParagraph(currentPosition, offset, length, p);
				if(currentPosition == -1 || currentPosition == offset + length)
					return;
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
							if(currentPosition == -1 || currentPosition == offset + length)
								return;
							else if(currentPosition >= offset){
								length -= currentPosition - offset;
								offset += currentPosition;
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
				System.out.println("Position courante : " + currentPosition + ", position recherch�e : " + offset);
				//If it is, this is our guy
				if(currentPosition >= offset){
					System.out.println("La position a �t� trouv�e");
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
				System.out.println("apr�s l'image, la position est de " + currentPosition);
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
				System.out.println("Position courante : " + currentPosition + ", position recherch�e : " + start);
				//If it is, this is our guy
				if(currentPosition >= start){
					System.out.println("La position de d�part a �t� trouv�e");
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
						System.out.println("Il y aura d'autres runs � explorer");
						//All the rest of the text goes into our new run
						newRun.setText(textEnd);
						//And we insert it after tr
						i.add(newRun);
						//The exploration of the arborescence will go on.
						//The next run to be explored will be newRun.
						//We don't want to change it, so let's change our start position
						start += textEnd.length();
						System.out.println("nouvelle position de d�part : " + start);
					}
					//If exactly
					else if(textEnd.length() == end - start){
						System.out.println("La modification va pile jusqu'� la fin du run");
						//All the rest of the text goes into our new run
						newRun.setText(textEnd);
						//And we insert it after tr
						p.insertRunAfter(newRun, tr);
						//We're done! No other run is needed.
						return -1;
					}
					//If more
					else{
						System.out.println("La modification ne va pas tout � fait jusqu'� la fin du run");
						//Let's split the remaining text into two parts:
						//What must change, and what must remain the same.
						System.out.println("all�");
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
				System.out.println("Position courante : " + currentPosition + ", position recherch�e : " + offset);
				//If it is, this is our guy
				if(currentPosition >= offset){
					System.out.println("La position de d�part a �t� trouv�e");
					//Start position within the text
					int startPosition = l - currentPosition + offset;
					//Let's split the text around this position
					String textStart = tr.getText().substring(0, startPosition);
					String textEnd = tr.getText().substring(startPosition);
					//Is the rest less, more, or exactly what we want?
					//If less
					if(textEnd.length() < length){
						length -= textEnd.length();
						System.out.println("Il y aura d'autres runs � explorer");
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
						System.out.println("La modification va pile jusqu'� la fin du run");
						//All the rest of the text is deleted
						//We just keep textStart
						tr.setText(textStart);
						//We're done.
						return -1;
					}
					//If more
					else{
						System.out.println("La modification ne va pas tout � fait jusqu'� la fin du run");
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

	private int insertElementIntoParagraph(Element e, int position, int currentPosition, Paragraph p){
		System.out.println("insertion d'un �l�ment");
		ListIterator<Run> i = (ListIterator) p.getRuns().iterator();
		Paragraph newParagraph = new Paragraph();//TODO : g�rer le style
		int addPosition = elements.indexOf(p) + 1;
		while(i.hasNext()){
			System.out.println("encore un run � examiner");
			System.out.println(currentPosition);
			Run r = i.next();
			if(currentPosition == -1){
				newParagraph.addRun(r);
				i.remove();
				System.out.println("run retir�");
			}
			else{
				if(r.getClass() == TextRun.class){
					System.out.println("Le run est un TextRun");
					TextRun tr = (TextRun)r;
					//Let's check if the offset is within this run.
					int l = tr.getText().length();
					currentPosition += l;
					System.out.println("Position courante : " + currentPosition + ", position recherch�e : " + position);
					//If it is, this is our guy
					if(currentPosition >= position){
						System.out.println("La position de d�part a �t� trouv�e");
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
			System.out.println("ajout de l'�l�ment");
			elements.add(addPosition, newParagraph);
			elements.add(addPosition, e);
			return -1;
		}
		//End of paragraph = \n, thus one more character
			currentPosition ++;
		return currentPosition;
	}
}