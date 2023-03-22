package playingAssets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.Random;
import java.util.logging.Level;
import javafx.application.Platform;
import main.scene.FigureCoordinates;
import main.scene.MainViewController;
import main.scene.MapFieldSquare;
import playingCards.DeckOfCards;
import z.logger.MyLogger;

/***
 * abstarct figure class containing all figure atributes and methods for its movement
 * @author Stefan
 *
 */
public abstract class Figure extends Thread implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int figureType = 0, diamonds = 0, number, ID; // ID - od kojeg je playera
	private int i, j, tempI, tempJ, previousI, previousJ, startingI, startingJ;
	javafx.scene.paint.Color color;
	private static MapFieldSquare[][] squares;
	private static CircularLinkedList cll;
	private static DeckOfCards cards;
	private int row[], column[];
	private int r1, r2;
	private int diamondsDropped = 0;
	private String name = "", text = "", colorString = "", typeString = "";
	private PrintWriter outFile;
	private LocalTime myTimeStart, myTimeEnd;
	public int listViewNumber;

	/***
	 * 
	 * @param color - color of figure
	 * @param figureType - 0-basic figure, 1-flying figure, 2-superfast figure
	 * @param number - player number
	 * @param ID - figure number
	 * @param name - name of the player
	 */
	public Figure(javafx.scene.paint.Color color, int figureType, int number, int ID, String name) {
		squares = MainViewController.getMap();
		cll = MainViewController.getCll();
		cards = MainViewController.getDeck();
		this.figureType = figureType;
		this.color = color;
		this.number = number;
		this.ID = ID;
		this.name = name;
		if (MainViewController.getMatrixSize() == 7 || MainViewController.getMatrixSize() == 8) {
			this.i = this.startingI = 0;
			this.j = this.startingJ = 3;
		} else {
			this.i = this.startingI = 0;
			this.j = this.startingJ = 4;
		}
		row = new int[MainViewController.getMatrixSize()];
		column = new int[MainViewController.getMatrixSize()];
		if(figureType == 1) {
			typeString = "Obicna";
		} else if(figureType == 2) {
			typeString = "Lebdeca";
		} else if(figureType == 3) {
			typeString = "Superbrza";
		}
		if(javafx.scene.paint.Color.RED == color) {
			colorString = "Crvena";
		} else if(javafx.scene.paint.Color.YELLOW == color) {
			colorString = "Zuta";
		} else if(javafx.scene.paint.Color.LIMEGREEN == color) {
			colorString = "Zelena";
		} else {
			colorString = "Plava";
		}
	}

	@Override
	public void run() {
		try {
			if (this.figureType == 0) { //ako je ghost figura
				int diamondsToDrop;
				diamondsToDrop = new Random().nextInt(MainViewController.getMatrixSize()-1) + 2;
				do {
					for(int g=0; g<5; g++) {
						if(MainViewController.isPaused()) {
							synchronized(this) {
								wait();
							}
						}
						sleep(1000);
					}
					this.setDiamond();
				} while(diamondsDropped < diamondsToDrop && MainViewController.isGameEnded() == false);
				return;
			} else {
				do { //postavljanje figure na pocetnu poziciju sa koje krecu sve figure
					if (cll.getCurrentPlayer(ID) && !squares[i][j].isOccupied()) {
						Platform.runLater(() -> squares[i][j].setColorFigurine(color, figureType));
						text = text + Integer.toString(squares[i][j].getSquareNumber()) + "-";
						sleep(1000);
						text = "Figura " + (number+1) + " (" + typeString + ", " + colorString + ") - predjeni put (" + squares[i][j].getSquareNumber();
						myTimeStart = LocalTime.now();
						MainViewController.listFiguresCoordinates.get(listViewNumber).add(new FigureCoordinates(i, j));
						break;
					} else {
						sleep(1000);
					}
				} while (true);
				Random rand = new Random();
				int tempCard, dist, counter;

				while (true) { // tijelo niti
					if (cll.getCurrentPlayer(ID)) { 
						synchronized (cards) {
						tempCard = cards.drawCard(rand.nextInt(52));
						if (tempCard == 5) { //ako smo izvukli specijalnu kartu
							MainViewController.cardDescription.setText("Igrac " + name + " sa figurom br. " + number + " postavlja rupe.");
							this.setHoles();
							if(MainViewController.isPaused()) {
								cards.wait();
							}
							sleep(1000);
							if(MainViewController.isPaused()) {
								cards.wait();
							}
							cards.hideCard(tempCard);
							MainViewController.cardDescription.setText("");
							if (squares[i][j].isHole() && figureType != 2) { //brisanje rupa ako je figura na rupi
								if (squares[i][j].isDiamond()) {
									diamonds++;
									squares[i][j].resetDiamond();
								}
								Platform.runLater(() -> squares[i][j].resetColorFigurine(figureType));
								this.resetHoles();
								cll.moveTempHead();
								if (this.number == 3) {
									cll.deleteNode(ID);
								}
								myTimeEnd = LocalTime.now();
								text = text + ") - stigla do cilja (ne) - Pocetak " + myTimeStart + " - Kraj " + myTimeEnd;
								outFile = new PrintWriter(new BufferedWriter(new FileWriter("src"+File.separator+"z"+File.separator+
										"files"+File.separator+"data"+File.separator+"Igrac-"+name+"-Figura"+(number+1)+".txt")), true);
								outFile.println(text);
								outFile.close();
								return;
							} else { //obicno brisanje rupa koje su postavljene
								this.resetHoles();
								if (i != startingI && j != startingJ)
									cll.moveTempHead();
							}
						} else { // ako smo izvuki 1 - 4 kartu
							if (squares[i][j].isDiamond()) {
								diamonds++;
								squares[i][j].resetDiamond();
							}
							if (figureType == 3) {
								dist = (tempCard + diamonds) * 2;
							} else {
								dist = tempCard + diamonds;
							}

							counter = 0;
							for (int k = 0; k < dist;) { // petlja za pomjeranje figure
								if(MainViewController.isPaused()) {
									cards.wait();
								}
								MainViewController.cardDescription.setText("");
								if (squares[i][j].isDiamond()) {
									diamonds++;
									squares[i][j].resetDiamond();
								}
								previousI = i;
								previousJ = j;
								tempI = squares[i][j].getNextI();
								tempJ = squares[i][j].getNextJ();
								i = tempI;
								j = tempJ;
								text = text + "-" + squares[i][j].getSquareNumber();
								if (!squares[i][j].isOccupied()) {
									if (counter == 0) {
										Platform.runLater(() -> squares[previousI][previousJ].resetColorFigurine(figureType));
									}
									Platform.runLater(() -> squares[i][j].setColorFigurine(color, figureType));
									if (squares[i][j].isDiamond()) {
										diamonds++;
										squares[i][j].resetDiamond();
									}
									MainViewController.cardDescription.setText("Igrac " + name + " sa figurom br. " + (number+1) + " se pomjera sa pozicije "
											+ squares[previousI][previousJ].getSquareNumber() + " na poziciju " + squares[i][j].getSquareNumber());
									MainViewController.listFiguresCoordinates.get(listViewNumber).add(new FigureCoordinates(i, j));
									sleep(1000);
									if(MainViewController.isPaused()) {
										cards.wait();
									}
									MainViewController.cardDescription.setText("");
									if (squares[i][j].isEndSquare()) { //provjera cilja
										Platform.runLater(() -> squares[i][j].resetColorFigurine(figureType));
										cards.hideCard(tempCard);
										cll.moveTempHead();
										if (this.number == 3) {
											cll.deleteNode(ID);
										}
										myTimeEnd = LocalTime.now();
										text = text + ") - stigla do cilja (da) Pocetak " + myTimeStart + " - Kraj " + myTimeEnd;
										outFile = new PrintWriter(new BufferedWriter(new FileWriter("src"+File.separator+"z"+File.separator+
												"files"+File.separator+"data"+File.separator+"Igrac-"+name+"-Figura"+(number+1)+".txt")), true);
										outFile.println(text);
										outFile.close();
										return;
									}
									counter = 0;
									k++;
								} else {
									if (counter == 0) {
										squares[previousI][previousJ].resetColorFigurine(figureType);
										MainViewController.listFiguresCoordinates.get(listViewNumber).add(new FigureCoordinates(i, j));
									}
									counter++;
								}
							}
							cards.hideCard(tempCard);
							cll.moveTempHead();
						}
					}
					} else { //ako figura nije na redu da igra
						for (int g = 0; g < 2; g++) {
							if (squares[i][j].isHole() && figureType != 2) {
								if (squares[i][j].isDiamond()) {
									diamonds++;
									squares[i][j].resetDiamond();
								}
								Platform.runLater(() -> squares[i][j].resetColorFigurine(figureType));
								squares[i][j].resetHole();
								if (this.number == 3) {
									cll.deleteNode(ID);
								}
								myTimeEnd = LocalTime.now();
								text = text + ") - stigla do cilja (ne) Pocetak " + myTimeStart + " - Kraj " + myTimeEnd;
								outFile = new PrintWriter(new BufferedWriter(new FileWriter("src"+File.separator+"z"+File.separator+
										"files"+File.separator+"data"+File.separator+"Igrac-"+name+"-Figura"+(number+1)+".txt")), true);
								outFile.println(text);
								outFile.close();
								return;
							}
							sleep(500);
						}
					}
				}
			}
		} catch (InterruptedException e) {
			MyLogger.log(Level.WARNING, "InterruptedException", e);
		} catch (IOException e) {
			MyLogger.log(Level.WARNING, "IO Exception", e);
		}
	}

	public void setHoles() {
		synchronized (squares) {
			Random rand1 = new Random();
			Random rand2 = new Random();
			for (int c = 0; c < MainViewController.getMatrixSize();) {
				r1 = rand1.nextInt(MainViewController.getMatrixSize());
				r2 = rand2.nextInt(MainViewController.getMatrixSize());
				if (squares[r1][r2].isRouteSquare() && !squares[r1][r2].isHole()) {
					row[c] = r1;
					column[c] = r2;
					squares[r1][r2].setHole();
					c++;
				}
			}
		}
	}
	
	public void setDiamond() {
		Random rand1 = new Random();
		Random rand2 = new Random();
		int r1, r2;
		while (true) {
			r1 = rand1.nextInt(MainViewController.getMatrixSize());
			r2 = rand2.nextInt(MainViewController.getMatrixSize());
			if (squares[r1][r2].isRouteSquare() && !squares[r1][r2].isDiamond()) {
				squares[r1][r2].setDiamond();
				this.diamondsDropped++;
				break;
			}
		}
	}

	public void resetHoles() {
		synchronized (squares) {
			for (int c = 0; c < MainViewController.getMatrixSize(); c++) {
				if (squares[row[c]][column[c]].isHole()) {
					squares[row[c]][column[c]].resetHole();
				}
			}
		}
	}

}