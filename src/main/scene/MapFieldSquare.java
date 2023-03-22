package main.scene;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/***
 * class representing fields in playing matrix 
 * with methods for colloring fields etc
 * @author Stefan
 *
 */
public class MapFieldSquare extends StackPane implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private boolean occupied = false, holeFlag = false, diamondFlag = false;
	private boolean routeSquare = false, endSquare = false;
	private int I, nextI;
	private int J, nextJ;
	private int squareNumber;
	
	protected Rectangle square;
	protected Rectangle normal;
	protected Arc flying;
	protected Rectangle superfast;
	protected Circle hole;
	
	public MapFieldSquare(int I, int J, double width, double height, int squareNumber) {
		this.I = I;
		this.J = J;
		this.squareNumber = squareNumber;
		square = new Rectangle(width, height);
		square.setFill(javafx.scene.paint.Color.rgb(225, 225, 225));
		square.setStroke(javafx.scene.paint.Color.BLACK);
		square.setArcHeight(0);
		square.setArcWidth(0);
		
		normal = new Rectangle(width/2.5, height/2.5);
		normal.setFill(javafx.scene.paint.Color.TRANSPARENT);
		normal.setArcHeight(0.0);
		normal.setArcWidth(0.0);
		
		flying = new Arc();
		flying.setFill(javafx.scene.paint.Color.TRANSPARENT);
		flying.setCenterX(width/2.0);
		flying.setCenterY(width/2.0);
		flying.setRadiusX(width/4.0);
		flying.setRadiusY(width/4.0);
		flying.setStartAngle(width/2.0);
		flying.setLength(270.0f);
		flying.setType(ArcType.ROUND);
		
		superfast = new Rectangle(width/3.5, height/2.0);
		superfast.setFill(javafx.scene.paint.Color.TRANSPARENT);
		
		hole = new Circle(width/2 - width/15);
		hole.setFill(javafx.scene.paint.Color.TRANSPARENT);
		
		this.getChildren().add(square);
		this.getChildren().add(hole);
		this.getChildren().add(normal);
		this.getChildren().add(flying);
		this.getChildren().add(superfast);
		this.setAlignment(Pos.CENTER);
	}
	
	public synchronized void setColorSquare(javafx.scene.paint.Color color) {
		square.setFill(color);
	}
	public synchronized void setColorNormal(javafx.scene.paint.Color color) {
		this.occupied = true;
		normal.setFill(color);
	}
	public synchronized void setColorFlying(javafx.scene.paint.Color color) {
		this.occupied = true;
		flying.setFill(color);
	}
	public synchronized void setColorSuperfast(javafx.scene.paint.Color color) {
		this.occupied = true;
		superfast.setFill(color);
	}
	
	public synchronized void setBorderSquare(javafx.scene.paint.Color color) {
		square.setStroke(color);
	}
	
	public synchronized void setColorFigurine(javafx.scene.paint.Color color, int figureType) {
		if(figureType == 1) {
			normal.setFill(color);
		} else if(figureType == 2) {
			flying.setFill(color);
		} else {
			superfast.setFill(color);
		}
		this.occupied = true;
	}
	
	public synchronized void resetColorFigurine(int figureType) {
		if(figureType == 1) {
			normal.setFill(javafx.scene.paint.Color.TRANSPARENT);
		} else if(figureType == 2) {
			flying.setFill(javafx.scene.paint.Color.TRANSPARENT);
		} else {
			superfast.setFill(javafx.scene.paint.Color.TRANSPARENT);
		}
		this.occupied = false;
	}
	
	public void listViewColor() {
		square.setFill(javafx.scene.paint.Color.rgb(80, 200, 120));
	}
	
	public synchronized void resetColorSquare() {
		square.setFill(javafx.scene.paint.Color.TRANSPARENT);
	}
	public synchronized void resetColorNormal() {
		normal.setFill(javafx.scene.paint.Color.TRANSPARENT);
		this.occupied = false;
	}
	public synchronized void resetColorFlying() {
		flying.setFill(javafx.scene.paint.Color.TRANSPARENT);
		this.occupied = false;
	}
	public synchronized void resetColorSuperFast() {
		superfast.setFill(javafx.scene.paint.Color.TRANSPARENT);
		this.occupied = false;
	}
	
	public synchronized void setHole() {
		hole.setFill(javafx.scene.paint.Color.DIMGRAY);
		this.holeFlag = true;
	}
	
	public synchronized boolean isHole() {
		return this.holeFlag;
	}
	
	public synchronized void resetHole() {
		hole.setFill(javafx.scene.paint.Color.TRANSPARENT);
		this.holeFlag = false;
	}
	
	public synchronized boolean isOccupied() {
		return this.occupied;
	}
	
	public synchronized void setOccupied() {
		this.occupied = true;
	}
	
	public synchronized void resetOccupied() {
		this.occupied = false;
	}
	
	public boolean getOccupied() {
		return this.occupied;
	}
	
	public synchronized void setDiamond() {
		square.setFill(javafx.scene.paint.Color.rgb(80, 200, 120));
		this.diamondFlag = true;
	}
	
	public synchronized boolean isDiamond() {
		return this.diamondFlag;
	}
	
	public void resetDiamond() {
		square.setFill(javafx.scene.paint.Color.TRANSPARENT); 
		this.diamondFlag = false;
	}
	
	public int getI() {
		return this.I;
	}
	
	public int getJ() {
		return this.J;
	}
	
	public void setNextIJ(int nextI, int nextJ) {
		this.nextI = nextI;
		this.nextJ = nextJ;
		this.routeSquare = true;
	}
	
	public int getNextI() {
		return this.nextI;
	}
	
	public int getNextJ() {
		return this.nextJ;
	}
	
	public synchronized boolean isRouteSquare() {
		return this.routeSquare;
	}
	
	public void setRouteSquare() {
		this.routeSquare = true;
	}
	
	public void setEndSquare() {
		this.endSquare = true;
		this.routeSquare = true;
	}
	
	public boolean isEndSquare() {
		return this.endSquare;
	}
	
	public int getSquareNumber() {
		return this.squareNumber;
	}

}
