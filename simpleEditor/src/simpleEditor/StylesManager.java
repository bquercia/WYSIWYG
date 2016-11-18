package simpleEditor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class StylesManager {

	private Vector styles;
	
	public StylesManager() {
		styles = new Vector();
	}
	
	public void insertTag(int start, int end, String startTag, String endTag){
		Vector overlappingStart = this.searchTag(0, end - 1, start);
		Collections.reverse(overlappingStart);
		Vector overlappingEnd = this.searchTag(start + 1, -1, end);
		Iterator i = overlappingStart.iterator();
		int currentPosition = start;
		int pos = 0;
		
		//Let's add as many styles as there are overlapping styles at the beginning.
		//Those tags are assumed to be ordered by end.
		while(i.hasNext()){
			Style s = (Style) i.next();
			//We may be considering a parent of someone we have already met.
			//Let's avoid that
			if(s.getEnd() > currentPosition){
				System.out.println("Found start-overlapping element: " + s);
				//Let's insert our tag in the right place
				pos = styles.indexOf(s);
				System.out.println("This element's index is " + pos);
				//We know where the parent element starts.
				//Let's find out if it has children before current position.
				Vector children = this.searchTag(s.getStart(), currentPosition - 1, -1);
				Iterator j = children.iterator();
				//We now increment the insertion position for each sibling we have found
				while(j.hasNext()){
					System.out.println("I found one of this element's children");
					j.next();
					pos ++;
				}
				pos ++;
				styles.add(pos, new Style(currentPosition, s.getEnd(), startTag, endTag));
				pos ++;
				System.out.println("Ok, I have added the first block. Look, here's how I am now:");
				System.out.println(this);
				currentPosition = s.getEnd() + 1;
			}
		}
		/*if(currentPosition != start)
			currentPosition++;*/
		System.out.println("My position is now " + currentPosition + ", while start is " + start);
		//By now, currentPosition should be a number between start and end, indicating where our "main tag" starts.
		//It should end at the beginning of the first end-overlapping element.
		//We can start listing these elements.
		i = overlappingEnd.iterator();
		while(i.hasNext()){
			Style s = (Style) i.next();
			System.out.println("Found end-overlapping element: " + s);
			pos = styles.indexOf(s);
			styles.add(pos, new Style(currentPosition, s.getStart() - 1, startTag, endTag));
			currentPosition = s.getStart();
			pos +=2;
		}
		//By now, currentPosition should be a number between start and end, indicating where our last tag starts.
		//pos should the index of this tag, provided overlapping tags have been met.
		//If not, then pos is 0, and we must determine the index by finding all the tags before start.
		if(pos == 0){
			System.out.println("Found no one over me. Looking for my parents.");
			Vector parentTags = this.searchTag(0,  -1, start);
			if(parentTags.size() != 0){
				Collections.reverse(parentTags);
				pos = styles.indexOf(parentTags.get(0)) + 1;
			}
			else{
				System.out.println("I have no parent. Looking for my older siblings.");
				Vector previousTags = this.searchTag(0,  start - 1, -1);
				if(previousTags.size() != 0){
					Collections.reverse(previousTags);
					pos = styles.indexOf(previousTags.get(0)) + 1;
				}
			}
		}
		System.out.println("My insertion position is " + pos);
		//pos is now the index we were looking for. Let's put the last brick!
		styles.add(pos, new Style(currentPosition, end, startTag, endTag));
		System.out.println("Finished inserting. This is me now:");
		System.out.println(this);
	}
	
	public void insertion(int start, int end){
		Iterator i = styles.iterator();
		while(i.hasNext()){
			Style s = (Style) i.next();
			//If the element start before the beginning and ends after it
			//We must simply shift the end of this element.
			if(s.getStart() <= start && s.getEnd() >= start)
				s.setBoundaries(s.getStart(), s.getEnd() + end - start + 1);
			//If the element starts after the beginning
			//We must shift the end and the beginning of this element.
			else if(s.getStart() > start)
				s.setBoundaries(s.getStart() + end - start + 1, s.getEnd() + end - start + 1);
		}
	}
	
	public void deletion(int start, int end){
		Iterator i = styles.iterator();
		while(i.hasNext()){
			Style s = (Style) i.next();
			//If the element start before the beginning and ends before the end
			//We must set the end just before the start of the deletion.
			if(s.getStart() < start && s.getEnd() <= end)
				s.setBoundaries(s.getStart(), start - 1);
			//If the element starts after the beginning and ends before the end
			//We must get rid of it.
			else if(s.getStart() >= start && s.getEnd() <= end)
				styles.removeElement(s);
			//If the elements starts after the beginning but before the end
			//(and implicitly, ends after the end)
			//We must set the beginning before the deleted elements and shift the end consequently.
			else if(s.getStart() >= start && s.getStart() <= s.getEnd())
				s.setBoundaries(start, s.getEnd() - end + start);
			//If the elements starts after the end
			//We must simply shift the beginning and end.
			else if(s.getStart() > end)
				s.setBoundaries(s.getStart() - start + end - 1, s.getEnd() - start + end - 1);
		}
	}
	
	public String parseText(String text){
		Style s = new Style(0, text.length() - 1, "", "");
		return auxParseText(s, text);
	}
	
	//Search all the tags starting at or after start, ending at or before end, containing member.
	//If end is -1, it just means there is no fixed end.
	//If member is -1, there is no specific content condition.
	//Tags are assumed to be ordered by start.
	private Vector searchTag(int start, int end, int member){
		Vector tags = new Vector();
		Iterator i = styles.iterator();
		while(i.hasNext()){
			Style s = (Style) i.next();
			if(s.getStart() >= start && (end == -1 || s.getEnd() <= end) && (member == -1 || (s.getStart() <= member && s.getEnd() >= member)))
				tags.addElement(s);
		}
		//The return value preserves the order.
		return tags;
	}
	
	public String toString(){
		Iterator i = styles.iterator();
		String txt = "";
		while(i.hasNext()){
			Style s = (Style) i.next();
			txt += s.toString() + "\n";
		}
		return txt;
	}
	
	private String auxParseText(Style s, String txt){
		//Let's find the children
		System.out.println("I am parsing " + s);
		Vector v = this.searchTag(s.getStart(), s.getEnd(), -1);
		Vector children = new Vector();
		while(!v.isEmpty() && styles.indexOf(v.get(0)) <= styles.indexOf(s)){
			v.remove(0);				
		}
		while(!v.isEmpty()){
			Style child = (Style) v.get(0);
			System.out.println("Found ond child:"  + child);
			children.add(child);
			if(child.getEnd() < s.getEnd() || child.getEnd() != child.getStart())
				v = this.searchTag(child.getEnd() + 1, s.getEnd(), -1);
			else v.clear();
		}
		Iterator i = children.iterator();
		String result = new String();
		int currentPos = s.getStart();
		System.out.println("current position:" + currentPos);
		while(i.hasNext()){
			Style child = (Style) i.next();
			result += txt.substring(currentPos, child.getStart());
			System.out.println("result is currently " + result);
			result += auxParseText(child, txt);
			currentPos = child.getEnd() + 1;
		}
		result += txt.substring(currentPos, s.getEnd() + 1);
		result = s.formatText(result);
		return result;
	}

}
