/**
 * 
 */
package util;

import java.util.LinkedList;

import content.Cell;
import content.Document;
import content.Element;
import content.ImgRun;
import content.Paragraph;
import content.Row;
import content.Run;
import content.TextRun;
import styles.Property;
import content.Table;

/**
 * @author Bruno Quercia
 *
 */
public class Translator {
	
	private LinkedList<Rule> rules;

	/**
	 * 
	 */
	public Translator() {
		this.rules = new LinkedList<Rule>();
	}
	
	public void addRule(Rule r){
		this.rules.add(r);
	}
	
	public String generateHTML(Document document){
		String result = "";
		for(Element e: document.getElements()){
			if(e.getClass() == Paragraph.class){
				result += "<p>";
				for(Run r: ((Paragraph)e).getRuns()){
					result += this.generateHTML(r);
				}
				result += "</p>";
			}
			else if(e.getClass() == Table.class){
				Table table = (Table)e;
				result += "<table style='border-spacing: -0px;'>";
				for(Row r: table.getRows()){
					result += "<tr>";
					for(Cell c: r.getOwnCells()){
						result +="<td style='border:1px solid black;' colspan='" + c.getColSpan() + "' rowspan='" + c.getRowSpan() + "'>";
						for(Paragraph p: c.getParagraphs()){
							result += "<p>";
							for(Run run: p.getRuns()){
								result += this.generateHTML(run);
							}
							result += "</p>";
						}
						result +="</td>";
					}
					result += "</tr>";
				}
				result += "</table>";
			}
		}
		return result;
	}
	
	private String generateHTML(Run r){
		String before = "";
		String after = "";
		String content = "";
		for(Property p: r.getStyle().getProperties()){
			System.out.println("Propriété : " + p.getLabel() + " : " + p.getValue());
			if(!p.hasPossibleValues()){
				Rule rule = new Rule(p);
				before += rule.getBefore();
				after = rule.getAfter() + after;
			}
			else if(rules.contains(new Rule(p, "", ""))){
				Rule rule = rules.get(rules.indexOf(new Rule(p, "", "")));
				before += rule.getBefore();
				after = rule.getAfter() + after;
			}
			else System.out.println("property not found");
		}
		if(r.getClass() == TextRun.class){
			content = (((TextRun)r).getText()).replaceAll("\r", "<br/>");
		}
		else if(r.getClass() == ImgRun.class){
			content = "<img src='" + ((ImgRun)r).getImg() + "'/>";
		}
		return before + content + after;
		
	}

}
