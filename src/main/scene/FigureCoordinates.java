package main.scene;

/***
 * utility class for figures
 * @author Stefan
 *
 */
public class FigureCoordinates {

	private int i, j;
	private int index;
	
	public FigureCoordinates(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public int getI() {
		return this.i;
	}
	
	public int getJ() {
		return this.j;
	}
	
	public int getIndex() {
		return this.index;
	}
	
}
