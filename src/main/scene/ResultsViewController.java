package main.scene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import z.logger.MyLogger;

public class ResultsViewController {
	
	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private Label printLabel;
	
	private File file;
	private String[] files;
	private BufferedReader reader;
	
	public void populateComboBox() {
		file = new File("src"+File.separator+"z"+File.separator+"files"+File.separator+"data");
		files = file.list();
		comboBox.getItems().addAll(files);
	}
	
	@FXML
	public void boxAction() {
		try {
			for (String temp : files) {
				if (temp == comboBox.getSelectionModel().getSelectedItem().toString()) {
					reader = new BufferedReader(new FileReader("src" + File.separator + "z" + File.separator + "files"
							+ File.separator + "data" + File.separator + temp));
					printLabel.setText(reader.readLine());
				}
			}
		} catch (FileNotFoundException e) {
			MyLogger.log(Level.WARNING, "FileNotFoundException at combo box", e);
		} catch (IOException ex) {
			MyLogger.log(Level.WARNING, "IOException at combo box", ex);
		}
	}
}
