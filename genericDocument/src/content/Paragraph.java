/**
 * 
 */
package content;

import java.util.LinkedList;

import util.Translator;

/**
 * @author Bruno Quercia
 * A paragraph is an Element that contains runs.
 */
public class Paragraph extends Element {
	
	private LinkedList<Run> runs;
	
	/**
	 * Creates an empty Paragraph
	 */
	public Paragraph() {
		this.runs = new LinkedList<Run>();
		runs.add(new TextRun());
	}
	
	/**
	 * Creates a Paragraph containing a given text
	 * @param txt the text to be added to the Paragraph
	 */
	public Paragraph(String txt){
		this.runs = new LinkedList<Run>();
		this.runs.add(new TextRun(txt));
	}
	
	/**
	 * Creates a Paragraph containing given Runs
	 * @param runs the Runs to be added to the Paragraph.
	 */
	public Paragraph(LinkedList<Run> runs){
		this.runs = runs;
	}

	/**
	 * 
	 * @return the Runs that compose the Paragraph
	 */
	public LinkedList<Run> getRuns() {
		return runs;
	}

	/**
	 * Sets the list of runs composing the Paragraph.
	 * All pre-existing runs will be erased.
	 * @param runs the runs that will now compose the Paragraph.
	 */
	public void setRuns(LinkedList<Run> runs) {
		this.runs = runs;
	}
	
	/**
	 * Adds a new run to the Paragraph
	 * @param r the run to be added
	 * @return success
	 */
	public boolean addRun(Run r){
		return this.runs.add(r);
	}
	
	/**
	 * Removes a run from the paragraph
	 * @param r the run to be removed
	 * @return success
	 */
	public boolean removeRun(Run r){
		return this.runs.remove(r);
	}
	
	/**
	 * Inserts a run after another run
	 * @param newRun the run to be inserted
	 * @param previousRun the run after which to insert
	 */
	public void insertRunAfter(Run newRun, Run previousRun){
		this.runs.add(this.runs.indexOf(previousRun) + 1, newRun);
	}

}
