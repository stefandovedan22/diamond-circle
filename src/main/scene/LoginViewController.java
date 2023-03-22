package main.scene;

import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import z.logger.MyLogger;

/***
 * login stage
 * @author Stefan
 *
 */
public class LoginViewController {

	@FXML
	public TextField matrixLabel;
	@FXML
	public TextField playersLabel;
	@FXML
	public TextField player1Label;
	@FXML
	public TextField player2Label;
	@FXML
	public TextField player3Label;
	@FXML
	public TextField player4Label;
	@FXML
	public Button submitBtn;

	private static int matrixSize = 0;
	private static int numOfPlayers = 0;
	private String player1 = "";
	private String player2 = "";
	private String player3 = "";
	private String player4 = "";
	private MainViewController controller;

	@FXML
	private void submit(ActionEvent event) {
			boolean size = false;
			boolean num = false;
			try {
				if(!matrixLabel.getText().isBlank()) {
					matrixSize = Integer.parseInt(matrixLabel.getText());
				}
				if(!playersLabel.getText().isBlank()) {
					numOfPlayers = Integer.parseInt(playersLabel.getText());
				}
				player1 = player1Label.getText();
				player2 = player2Label.getText();
				player3 = player3Label.getText();
				player4 = player4Label.getText();
				if(matrixSize>=7 && matrixSize <=10) {
					size = true;
				}
				if(numOfPlayers>=2 && numOfPlayers<=4) {
					num = true;
				}
				if(size && num && checkPlayerNames()) {
					Main.primaryStage = new Stage();
					Main.primaryStage.setTitle("DiamondCircle");
					Main.primaryStage.setResizable(false);
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Main.class.getResource("MainView.fxml"));
					Main.mainLayout = loader.load();
					Scene scene = new Scene(Main.mainLayout);
					Main.primaryStage.setScene(scene);
					Main.primaryStage.show();
					
					controller = loader.getController();
					controller.getInfo(matrixSize, numOfPlayers, player1, player2, player3, player4);
					
					Main.playersStage.close();
					controller.showGUI();
					
				}
			} catch (Exception e) {
				MyLogger.log(Level.WARNING, "TextFieldException", e);
			}
	}
	
	private boolean checkPlayerNames() {
		int temp = 0;
		if (player1Label.getText().isBlank()) {
			temp++;
		} else {
			if(player1.equals(player2) || player1.equals(player3) || player1.equals(player4))
				return false;
		}
		if (player2Label.getText().isBlank()) {
			temp++;
		} else {
			if(player2.equals(player1) || player2.equals(player3) || player2.equals(player4))
				return false;
		}
		if (player3Label.getText().isBlank()) {
			temp++;
		} else {
			if(player3.equals(player1) || player3.equals(player2) || player3.equals(player4))
				return false;
		}
		if (player4Label.getText().isBlank()) {
			temp++;
		} else {
			if(player4.equals(player1) || player4.equals(player2) || player4.equals(player3))
				return false;
		}
		if(temp + numOfPlayers != 4) 
			return false;
		
		return true;
	}
	
	public static int getMatrixSize() {
		return LoginViewController.matrixSize;
	}
	
	public MainViewController getController() {
		return this.controller; 
	}

}
