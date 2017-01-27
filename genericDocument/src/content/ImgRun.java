/**
 * 
 */
package content;

import java.awt.Image;

import styles.Style;

/**
 * @author Bruno Quercia
 *
 */
public class ImgRun extends Run {
	private String img;
	/**
	 * @param style
	 */
	public ImgRun(Style style, String image) {
		super(style);
		// TODO Auto-generated constructor stub
		this.img = image;
	}

	/**
	 * 
	 */
	public ImgRun(String image) {
		// TODO Auto-generated constructor stub
		this.img = image;
	}
	
	public ImgRun(){
		
	}
	
	public String getImg(){
		return this.img;
	}

}
