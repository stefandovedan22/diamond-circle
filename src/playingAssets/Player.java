package playingAssets;

import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import z.logger.MyLogger;

/***
 * player class in which we make 4 figures for every player and start them one by one 
 * @author Stefan
 *
 */
public class Player extends Thread {
	
	private LinkedList<Figure> figures;
	private String name;
	private javafx.scene.paint.Color color;
	private int ID;
	private boolean finished = false;
	
	public Player(String name, int ID) {
		this.name = name;
		this.ID = ID;
		if(ID == 1) { 
			color = javafx.scene.paint.Color.RED;
		} else if(ID == 2) { 
			color = javafx.scene.paint.Color.YELLOW;
		} else if(ID == 3) {
			color = javafx.scene.paint.Color.LIMEGREEN;
		} else {
			color = javafx.scene.paint.Color.DODGERBLUE;
		}
		this.figures = new LinkedList<Figure>();
		Random rand = new Random();
		for(int i=0; i<4; i++) {
			int temp = rand.nextInt(3);
			if(temp == 0) {
				figures.add(new BasicFigure(this.color, i, ID, name));
			} else if(temp == 1) {
				figures.add(new FlyingFigure(this.color, i, ID, name));
			} else {
				figures.add(new SuperFastFigure(this.color, i, ID, name));
			}
		}
	}
	
	@Override
	public void run() {
		for (int i = 0; i < 4; i++) {
			if(this.ID == 1) {
				figures.get(i).listViewNumber = i;
			} else if(this.ID == 2) {
				figures.get(i).listViewNumber = i+4;
			} else if(this.ID == 3) {
				figures.get(i).listViewNumber = i+8;
			} else {
				figures.get(i).listViewNumber = i+12;
			}
		}
		for (int i = 0; i < 4; i++) {
			figures.get(i).start();
			try {
				figures.get(i).join();
			} catch (InterruptedException e) {
				MyLogger.log(Level.WARNING, "InterruptedException", e);
			}
		}
		//System.out.println("IGRAC " + ID + " ZAVRSIO IGRU");
		finished = true;
	}

	public boolean isFinished() {
		return this.finished;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public String getPlayerName() {
		return this.name;
	}
	
	public javafx.scene.paint.Color getColor() {
		return this.color;
	}

}
