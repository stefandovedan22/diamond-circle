package main.scene;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import playingAssets.CircularLinkedList;
import playingAssets.Figure;
import playingAssets.GhostFigure;
import playingAssets.Player;
import playingCards.DeckOfCards;
import z.logger.MyLogger;

/***
 * main stage controller
 * @author Stefan
 *
 */
public class MainViewController {
	
	@FXML
	private GridPane grid10, grid9, grid8, grid7;
	@FXML
	private Label player1L, player2L, player3L, player4L;
	@FXML
	private Button startStopBttn, showResultsBttn;
	@FXML
	private ListView<String> listFigures;
	
	private static Image image1, image2, image3, image4, image5, image6;
	private static ImageView card1, card2, card3, card4, card5, imageBorder;
	public static TextArea cardDescription;
	public static Label timeLabel, gamesPlayedLabel;
	
	private static boolean cardDrawn = false;
	private static LinkedList<Player> players = new LinkedList<Player>();
	public static DeckOfCards cards;
	public static CircularLinkedList cll = new CircularLinkedList();
	private static int matrixSize, startingJ;
	private static int numOfPlayers;
	
	private String player1, player2, player3, player4;
	private static MapFieldSquare[][] squares;
	private static boolean gameEnded = false, paused = false;
	private GamesPlayedWatcher gamesPlayedWatcher;
	private Figure ghostFigure;
	private ResultsViewController controller;
	private Timer currentTime;
	
	private LinkedList<String> figures;
	private int startStopState = 0;
	
	public static LinkedList<LinkedList<FigureCoordinates>> listFiguresCoordinates;
	
	/***
	 * main function for showing all elements on main stage before the game can be started
	 */
	public void showGUI() {
		try {
			startStopBttn.setDisable(true);
			showResultsBttn.setDisable(true);
			Font font = Font.font("Microsoft JhengHei", 24);
			timeLabel = new Label("00:00");
			timeLabel.setFont(font);
			timeLabel.setTextFill(Color.rgb(166, 32, 186));
			timeLabel.setLayoutX(1090.0);
			timeLabel.setLayoutY(492.0);
			Main.mainLayout.getChildren().add(timeLabel);
			
			gamesPlayedLabel = new Label("");
			gamesPlayedLabel.setFont(font);
			gamesPlayedLabel.setTextFill(Color.rgb(166, 32, 186));
			gamesPlayedLabel.setLayoutX(410.0);
			gamesPlayedLabel.setLayoutY(846.0);
			Main.mainLayout.getChildren().add(gamesPlayedLabel);
			
			cardDescription = new TextArea();
			Font font1 = Font.font("Microsoft JhengHei", 20);
			cardDescription.setFont(font1);
			cardDescription.setWrapText(true);
			cardDescription.setStyle(player1);
			cardDescription.setPrefWidth(200);
			cardDescription.setPrefHeight(298);
			cardDescription.setLayoutX(989);
			cardDescription.setLayoutY(587);
			cardDescription.setStyle("-fx-border-color: black ;");
			cardDescription.setEditable(false);
			cardDescription.setText("");
			Main.mainLayout.getChildren().add(cardDescription);
			
			double width = 0, height = 0;
			squares = new MapFieldSquare[matrixSize][matrixSize];
			int squareNumber=1;
			if (matrixSize == 10) {
				width = height = 700.0 / 10.0;
				MainViewController.startingJ = 4;
				for (int i = 0; i < matrixSize; i++) {
					for (int j = 0; j < matrixSize; j++) {
						squares[i][j] = new MapFieldSquare(i, j, width, height, squareNumber++);
						grid10.add(squares[i][j], j, i);
					}
				}
			} else if (matrixSize == 9) {
				width = height = 702.0 / 9.0;
				startingJ = 4;
				for (int i = 0; i < matrixSize; i++) {
					for (int j = 0; j < matrixSize; j++) {
						squares[i][j] = new MapFieldSquare(i, j, width, height, squareNumber++);
						grid9.add(squares[i][j], j, i);
					}
				}
			} else if (matrixSize == 8) {
				width = height = 696.0 / 8.0;
				startingJ = 3;
				for (int i = 0; i < matrixSize; i++) {
					for (int j = 0; j < matrixSize; j++) {
						squares[i][j] = new MapFieldSquare(i, j, width, height, squareNumber++);
						grid8.add(squares[i][j], j, i);
					}
				}
			} else { //7
				width = height = 100.0;
				startingJ = 3;
				for (int i = 0; i < matrixSize; i++) {
					for (int j = 0; j < matrixSize; j++) {
						squares[i][j] = new MapFieldSquare(i, j, width, height, squareNumber++);
						grid7.add(squares[i][j], j, i);
					}
				}
			}
			
			setPlayRoute(matrixSize);
			currentTime = new Timer();
			randomizePlayers();
			createImages();
			MainViewController.cards = new DeckOfCards();
			
			for(File file : new File("src"+File.separator+"z"+File.separator+"files"+File.separator+"data").listFiles()) {
				if (!file.isDirectory()) 
			        file.delete();
			}
			
			gamesPlayedWatcher = new GamesPlayedWatcher();
			gamesPlayedWatcher.start();
			
			synchronized (squares) {
				Thread temp = new Thread(new Thread() {
					int i = 0, j = startingJ;
					public void run() {
						int tempI, tempJ;
						while (true) {
//							Platform.runLater(() -> squares[i][j].setColorSquare(javafx.scene.paint.Color.TRANSPARENT));
							squares[i][j].setColorSquare(javafx.scene.paint.Color.TRANSPARENT);
							squares[i][j].setRouteSquare();
							if (squares[i][j].isEndSquare()) {
								squares[i][j].setBorderSquare(javafx.scene.paint.Color.rgb(168, 31, 188));
								break;
							}
							try {
								sleep(120);
							} catch (InterruptedException e) {
								MyLogger.log(Level.WARNING, "Thread initialization of playField", e);
							}
							tempI = squares[i][j].getNextI();
							tempJ = squares[i][j].getNextJ();
							i = tempI;
							j = tempJ;
						}
						startStopBttn.setDisable(false);
						showResultsBttn.setDisable(false);
					}
				});
				temp.start();
			}
			
			Thread figuresThread = new Thread(new Thread() { //tab sa figurama
				public void run() {
					listFigures.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
						@Override
						public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
							try {
								String[] temp = listFigures.getSelectionModel().getSelectedItem().split(" ");
								int index = Integer.parseInt(temp[1])-1;
								Main.figureStage = new Stage();
								Main.figureStage.setTitle("Figure");
								Main.figureStage.setResizable(false);
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(Main.class.getResource("FigureView.fxml"));
								Main.figureLayout = loader.load();
								Scene scene = new Scene(Main.figureLayout);
								Main.figureStage.setScene(scene);
								Main.figureStage.show();
								FigureController cont = loader.getController();
								cont.createMap(matrixSize, index);
							} catch (IOException e) {
								MyLogger.log(Level.WARNING, "IOException", e);
							}
						}
					});
				}
			});
			figuresThread.start();
		} catch (Exception e) {
			MyLogger.log(Level.WARNING, "Exception in showing matrix", e);
		}
	}
	
	@SuppressWarnings("removal") //start/stop button
	@FXML
	private void startStop() {
		if (this.startStopState == 0) { 
			currentTime.start();
			ghostFigure = new GhostFigure(javafx.scene.paint.Color.TRANSPARENT, 0, 0, "Ghost");
			ghostFigure.start();

			this.startStopState = 1;
			Thread t = new Thread(new Thread() {
				public void run() {
					try {
						for (int i = 0; i < numOfPlayers; i++) {
							players.get(i).start();
							sleep(1000);
						}
						for (int i = 0; i < numOfPlayers; i++) {
							players.get(i).join();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("IGRA JE ZAVRSENA");
					gameEnded = true;
				}
			});
			t.start();
			startStopBttn.setText("Pause");
			startStopBttn.setTextFill(javafx.scene.paint.Color.RED);
		} else if (this.startStopState == 1) { 
			MainViewController.paused = true;
			currentTime.suspend();
			this.startStopState = 2;
			startStopBttn.setText("Resume");
			startStopBttn.setTextFill(javafx.scene.paint.Color.rgb(80, 200, 120));
		} else { 
			synchronized(cards) {
				cards.notify();
			}
			synchronized(ghostFigure) {
				ghostFigure.notify();
			}
			MainViewController.paused = false;
			currentTime.resume();
			this.startStopState = 1;
			startStopBttn.setText("Pause");
			startStopBttn.setTextFill(javafx.scene.paint.Color.RED);
		}
	}
	
	@FXML
	private void showResults() {
		try {
			Main.resultsStage = new Stage();
			Main.resultsStage.setTitle("Results");
			Main.resultsStage.setResizable(false);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("ShowResultsView.fxml"));
			Main.resultsLayout = loader.load();
			Scene scene = new Scene(Main.resultsLayout);
			Main.resultsStage.setScene(scene);
			Main.resultsStage.show();
			controller = loader.getController();
			controller.populateComboBox();
		} catch (IOException e) {
			MyLogger.log(Level.WARNING, "IOException forming result stage", e);
		}
	}
	
	private void randomizePlayers() {
		if(!"".equals(player1)) {
			players.add(new Player(player1, 1));
		}
		if(!"".equals(player2)) {
			players.add(new Player(player2, 2));
		}
		if(!"".equals(player3)) {
			players.add(new Player(player3, 3));
		}
		if(!"".equals(player4)) {
			players.add(new Player(player4, 4));
		}
		Collections.shuffle(players); //igraci igraju po random rasporedu
		for(int i=0; i<numOfPlayers; i++) {
			cll.addNode(players.get(i).getID());
		}
	}
	
	/**
	 * function to create images for cards
	 */
	public void createImages() {
		image1 = new Image("z/files/images/1.png");
		card1 = new ImageView();
		card1.setImage(image1);
		card1.setFitWidth(150);
		card1.setFitHeight(140);
		card1.setVisible(false);
		card1.setX(1015);
		card1.setY(320);
		
		image2 = new Image("z/files/images/2.png");
		card2 = new ImageView();
		card2.setImage(image2);
		card2.setFitWidth(150);
		card2.setFitHeight(140);
		card2.setVisible(false);
		card2.setX(1015);
		card2.setY(320);
		
		image3 = new Image("z/files/images/3.png");
		card3 = new ImageView();
		card3.setImage(image3);
		card3.setFitWidth(150);
		card3.setFitHeight(140);
		card3.setVisible(false);
		card3.setX(1015);
		card3.setY(320);
		
		image4 = new Image("z/files/images/4.png");
		card4 = new ImageView();
		card4.setImage(image4);
		card4.setFitWidth(150);
		card4.setFitHeight(140);
		card4.setVisible(false);
		card4.setX(1015);
		card4.setY(320);
		
		image5 = new Image("z/files/images/S.png");
		card5 = new ImageView();
		card5.setImage(image5);
		card5.setFitWidth(150);
		card5.setFitHeight(140);
		card5.setVisible(false);
		card5.setX(1015);
		card5.setY(320);
		
		image6 = new Image("z/files/images/squarev2.png");
		imageBorder = new ImageView();
		imageBorder.setImage(image6);
		imageBorder.setFitWidth(150);
		imageBorder.setFitHeight(142);
		imageBorder.setVisible(false);
		imageBorder.setX(1015);
		imageBorder.setY(320);
		
		Main.mainLayout.getChildren().add(card1);
		Main.mainLayout.getChildren().add(card2);
		Main.mainLayout.getChildren().add(card3);
		Main.mainLayout.getChildren().add(card4);
		Main.mainLayout.getChildren().add(card5);
		Main.mainLayout.getChildren().add(imageBorder);
	}
	
	/***
	 * transferring information from login stage to main stage
	 * @param matrix size of matrix
	 * @param num number of players
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 */
	public void getInfo(int matrix, int num, String p1, String p2, String p3, String p4) {
		MainViewController.matrixSize = matrix;
		MainViewController.numOfPlayers = num;
		this.player1 = p1;
		this.player1L.setText(p1);
		this.player2 = p2;
		this.player2L.setText(p2);
		this.player3 = p3;
		this.player3L.setText(p3);
		this.player4 = p4;
		this.player4L.setText(p4);
		figures = new LinkedList<>();
		listFiguresCoordinates = new LinkedList<LinkedList<FigureCoordinates>>();
		for(int i=0; i<num*4; i++) {
			figures.add("Figura "+(i+1));
			listFiguresCoordinates.add(new LinkedList<FigureCoordinates>());
		}
		listFigures.getItems().addAll(figures);
	}
	
	@FXML
	public static synchronized void showCard1() {
		card1.setVisible(true);
		imageBorder.setVisible(true);
	}
	@FXML
	public static synchronized void showCard2() {
		card2.setVisible(true);
		imageBorder.setVisible(true);
	}
	@FXML
	public static synchronized void showCard3() {
		card3.setVisible(true);
		imageBorder.setVisible(true);
	}
	@FXML
	public static synchronized void showCard4() {
		card4.setVisible(true);
		imageBorder.setVisible(true);
	}
	@FXML
	public static synchronized void showCard5() {
		card5.setVisible(true);
		imageBorder.setVisible(true);
	}
	@FXML
	public static synchronized void hideCard1() {
		card1.setVisible(false);
		imageBorder.setVisible(false);
	}
	@FXML
	public static synchronized void hideCard2() {
		card2.setVisible(false);
		imageBorder.setVisible(false);
	}
	@FXML
	public static synchronized void hideCard3() {
		card3.setVisible(false);
		imageBorder.setVisible(false);
	}
	@FXML
	public static synchronized void hideCard4() {
		card4.setVisible(false);
		imageBorder.setVisible(false);
	}
	@FXML
	public static synchronized void hideCard5() {
		card5.setVisible(false);
		imageBorder.setVisible(false);
	}
	
	public static int getMatrixSize() {
		return MainViewController.matrixSize;
	}

	/***
	 * setting game route
	 * @param dimension matrix dimension
	 */
	private void setPlayRoute(int dimension) {
		if(dimension == 7) {
			squares[0][3].setNextIJ(1, 4);
			squares[1][4].setNextIJ(2, 5);
			squares[2][5].setNextIJ(3, 6);
			squares[3][6].setNextIJ(4, 5);
			squares[4][5].setNextIJ(5, 4);
			squares[5][4].setNextIJ(6, 3);
			squares[6][3].setNextIJ(5, 2);
			squares[5][2].setNextIJ(4, 1);
			squares[4][1].setNextIJ(3, 0);
			squares[3][0].setNextIJ(2, 1);
			squares[2][1].setNextIJ(1, 2);
			squares[1][2].setNextIJ(1, 3);
			squares[1][3].setNextIJ(2, 4);
			squares[2][4].setNextIJ(3, 5);
			squares[3][5].setNextIJ(4, 4);
			squares[4][4].setNextIJ(5, 3);
			squares[5][3].setNextIJ(4, 2);
			squares[4][2].setNextIJ(3, 1);
			squares[3][1].setNextIJ(2, 2);
			squares[2][2].setNextIJ(2, 3);
			squares[2][3].setNextIJ(3, 4);
			squares[3][4].setNextIJ(4, 3);
			squares[4][3].setNextIJ(3, 2);
			squares[3][2].setNextIJ(3, 3);
			squares[3][3].setEndSquare();
		} else if(dimension == 8) {
			squares[0][3].setNextIJ(1, 4);
			squares[1][4].setNextIJ(2, 5);
			squares[2][5].setNextIJ(3, 6);
			squares[3][6].setNextIJ(4, 7);
			squares[4][7].setNextIJ(5, 6);
			squares[5][6].setNextIJ(6, 5);
			squares[6][5].setNextIJ(7, 4);
			squares[7][4].setNextIJ(6, 3);
			squares[6][3].setNextIJ(5, 2);
			squares[5][2].setNextIJ(4, 1);
			squares[4][1].setNextIJ(3, 0);
			squares[3][0].setNextIJ(2, 1);
			squares[2][1].setNextIJ(1, 2);
			squares[1][2].setNextIJ(1, 3);
			squares[1][3].setNextIJ(2, 4);
			squares[2][4].setNextIJ(3, 5);
			squares[3][5].setNextIJ(4, 6);
			squares[4][6].setNextIJ(5, 5);
			squares[5][5].setNextIJ(6, 4);
			squares[6][4].setNextIJ(5, 3);
			squares[5][3].setNextIJ(4, 2);
			squares[4][2].setNextIJ(3, 1);
			squares[3][1].setNextIJ(2, 2);
			squares[2][2].setNextIJ(2, 3);
			squares[2][3].setNextIJ(3, 4);
			squares[3][4].setNextIJ(4, 5);
			squares[4][5].setNextIJ(5, 4);
			squares[5][4].setNextIJ(4, 3);
			squares[4][3].setNextIJ(3, 2);
			squares[3][2].setNextIJ(3, 3);
			squares[3][3].setNextIJ(4,  4);
			squares[4][4].setEndSquare();
		} else if(dimension == 9) {
			squares[0][4].setNextIJ(1, 5);
			squares[1][5].setNextIJ(2, 6);
			squares[2][6].setNextIJ(3, 7);
			squares[3][7].setNextIJ(4, 8);
			squares[4][8].setNextIJ(5, 7);
			squares[5][7].setNextIJ(6, 6);
			squares[6][6].setNextIJ(7, 5);
			squares[7][5].setNextIJ(8, 4);
			squares[8][4].setNextIJ(7, 3);
			squares[7][3].setNextIJ(6, 2);
			squares[6][2].setNextIJ(5, 1);
			squares[5][1].setNextIJ(4, 0);
			squares[4][0].setNextIJ(3, 1);
			squares[3][1].setNextIJ(2, 2);
			squares[2][2].setNextIJ(1, 3);
			squares[1][3].setNextIJ(1, 4);
			squares[1][4].setNextIJ(2, 5);
			squares[2][5].setNextIJ(3, 6);
			squares[3][6].setNextIJ(4, 7);
			squares[4][7].setNextIJ(5, 6);
			squares[5][6].setNextIJ(6, 5);
			squares[6][5].setNextIJ(7, 4);
			squares[7][4].setNextIJ(6, 3);
			squares[6][3].setNextIJ(5, 2);
			squares[5][2].setNextIJ(4, 1);
			squares[4][1].setNextIJ(3, 2);
			squares[3][2].setNextIJ(2, 3);
			squares[2][3].setNextIJ(2, 4);
			squares[2][4].setNextIJ(3, 5);
			squares[3][5].setNextIJ(4, 6);
			squares[4][6].setNextIJ(5, 5);
			squares[5][5].setNextIJ(6, 4);
			squares[6][4].setNextIJ(5, 3);
			squares[5][3].setNextIJ(4, 2);
			squares[4][2].setNextIJ(3, 3);
			squares[3][3].setNextIJ(3, 4);
			squares[3][4].setNextIJ(4, 5);
			squares[4][5].setNextIJ(5, 4);
			squares[5][4].setNextIJ(4, 3);
			squares[4][3].setNextIJ(4, 4);
			squares[4][4].setEndSquare();
		} else {
			squares[0][4].setNextIJ(1, 5);
			squares[1][5].setNextIJ(2, 6);
			squares[2][6].setNextIJ(3, 7);
			squares[3][7].setNextIJ(4, 8);
			squares[4][8].setNextIJ(5, 9);
			squares[5][9].setNextIJ(6, 8);
			squares[6][8].setNextIJ(7, 7);
			squares[7][7].setNextIJ(8, 6);
			squares[8][6].setNextIJ(9, 5);
			squares[9][5].setNextIJ(8, 4);
			squares[8][4].setNextIJ(7, 3);
			squares[7][3].setNextIJ(6, 2);
			squares[6][2].setNextIJ(5, 1);
			squares[5][1].setNextIJ(4, 0);
			squares[4][0].setNextIJ(3, 1);
			squares[3][1].setNextIJ(2, 2);
			squares[2][2].setNextIJ(1, 3);
			squares[1][3].setNextIJ(1, 4);
			squares[1][4].setNextIJ(2, 5);
			squares[2][5].setNextIJ(3, 6);
			squares[3][6].setNextIJ(4, 7);
			squares[4][7].setNextIJ(5, 8);
			squares[5][8].setNextIJ(6, 7);
			squares[6][7].setNextIJ(7, 6);
			squares[7][6].setNextIJ(8, 5);
			squares[8][5].setNextIJ(7, 4);
			squares[7][4].setNextIJ(6, 3);
			squares[6][3].setNextIJ(5, 2);
			squares[5][2].setNextIJ(4, 1);
			squares[4][1].setNextIJ(3, 2);
			squares[3][2].setNextIJ(2, 3);
			squares[2][3].setNextIJ(2, 4);
			squares[2][4].setNextIJ(3, 5);
			squares[3][5].setNextIJ(4, 6);
			squares[4][6].setNextIJ(5, 7);
			squares[5][7].setNextIJ(6, 6);
			squares[6][6].setNextIJ(7, 5);
			squares[7][5].setNextIJ(6, 4);
			squares[6][4].setNextIJ(5, 3);
			squares[5][3].setNextIJ(4, 2);
			squares[4][2].setNextIJ(3, 3);
			squares[3][3].setNextIJ(3, 4);
			squares[3][4].setNextIJ(4, 5);
			squares[4][5].setNextIJ(5, 6);
			squares[5][6].setNextIJ(6, 5);
			squares[6][5].setNextIJ(5, 4);
			squares[5][4].setNextIJ(4, 3);
			squares[4][3].setNextIJ(4, 4);
			squares[4][4].setNextIJ(5, 5);
			squares[5][5].setEndSquare();
		}
	}
	
	public static MapFieldSquare[][] getMap() {
		return MainViewController.squares;
	}
	
	public static CircularLinkedList getCll() {
		return MainViewController.cll;
	}
	
	public static DeckOfCards getDeck() {
		return MainViewController.cards;
	}
	
	public static synchronized boolean isCardDrawn(int rand) {
		return MainViewController.cardDrawn;
	}
	
	public static synchronized void setCardDrawn(boolean cardDrawn) {
		MainViewController.cardDrawn = cardDrawn;
	}
	
	public static void setGameEnded() {
		MainViewController.gameEnded = true;
	}
	
	public static boolean isGameEnded() {
		return MainViewController.gameEnded;
	}
	
	public static boolean isPaused() {
		return MainViewController.paused;
	}
}
