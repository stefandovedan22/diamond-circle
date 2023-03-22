package playingCards;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import main.scene.Main;
import main.scene.MainViewController;
import z.logger.MyLogger;

public class DeckOfCards {

	LinkedList<Card> cards;
	MainViewController controller;
	private Lock lock;
	
	public DeckOfCards() {
		lock = new ReentrantLock();
		cards = new LinkedList<Card>();
		cards.add(new Card(5));
		for(int i=0; i<10; i++) {
			cards.add(new Card(1));
			cards.add(new Card(2));
			cards.add(new Card(3));
			cards.add(new Card(4));
			cards.add(new Card(5));
		}
		cards.add(new Card(5));
		Collections.shuffle(cards);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("MainView.fxml"));
		
		try {
			loader.load();
		} catch (IOException e) {
			MyLogger.log(Level.WARNING, "IOException in deck of cards", e);
		}
		controller = loader.getController();
	}
	
	public synchronized int drawCard(int index) {
		Card tempC = cards.remove(index);
		cards.add(tempC);
		if(tempC.getID() == 1) {
			MainViewController.showCard1();
		} else if(tempC.getID() == 2) {
			MainViewController.showCard2();
		} else if(tempC.getID() == 3) {
			MainViewController.showCard3();
		} else if(tempC.getID() == 4) {
			MainViewController.showCard4();
		} else {
			MainViewController.showCard5();
		}
		MainViewController.setCardDrawn(true);
		return tempC.getID();
	}
	
	public synchronized void hideCard(int cardValue) {
		if(cardValue == 1) {
			MainViewController.hideCard1();
		} else if(cardValue == 2) {
			MainViewController.hideCard2();
		} else if(cardValue == 3) {
			MainViewController.hideCard3();
		} else if(cardValue == 4) {
			MainViewController.hideCard4();
		} else {
			MainViewController.hideCard5();
		}
		MainViewController.setCardDrawn(false);
	}
	
	public Lock getLock() {
		return this.lock;
	}
}
