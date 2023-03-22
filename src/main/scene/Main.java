package main.scene;

import java.io.IOException;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//import main.scene.MainViewController;
import z.logger.MyLogger;

public class Main extends Application {

	public static Stage primaryStage, playersStage, resultsStage, figureStage;
	public static AnchorPane mainLayout, playersLayout, resultsLayout, figureLayout;

	@Override
	public void start(Stage primaryStage) throws IOException {
		
		MyLogger.setup();
		
		try {
			Main.playersStage = new Stage();
			Main.playersStage.setTitle("Players Login");
			Main.playersStage.initStyle(StageStyle.UNDECORATED);
			Main.playersStage.initModality(Modality.APPLICATION_MODAL);
			
			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(Main.class.getResource("LogInScreen.fxml"));
			playersLayout = loader2.load();
			Scene scene2 = new Scene(playersLayout);
			playersStage.setScene(scene2);
			playersStage.show(); 
			
		} catch(IOException e) {
			MyLogger.log(Level.WARNING, "IOException", e);
		}
	}

	public static void main(String[] args) {
		launch(args); 
	}
}
