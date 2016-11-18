package simpleEditor;

public class Style {
	
	private String startTag, endTag;
	private int start, end;
	
	public Style(int start, int end, String startTag, String endTag) {
		this.start = start;
		this.end = end;
		this.startTag = startTag;
		this.endTag = endTag;
	}
	
	public String formatText(String txt){
		return startTag + txt + endTag;
	}
	
	public void setBoundaries(int start, int end){
		if(start >= 0)
			this.start = start;
		else this.start = 0;
		this.end = end;
	}
	
	public int getStart(){
		return start;
	}
	public int getEnd(){
		return end;
	}
	public String toString(){
		return startTag + endTag + " : " + start + " -> " + end;
	}

}
