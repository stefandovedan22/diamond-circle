package main.scene;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class FigureController {
	
	@FXML
	private GridPane grid7, grid8, grid9, grid10;
	private MapFieldSquare[][] squares;
	private double width, height;
	private int squareNumber = 1;
	
	/***
	 * function for creating mini map when click on figure in list view
	 * @param size matrix dimension
	 * @param index figure index
	 */
	public void createMap(int size, int index) {
		squares = new MapFieldSquare[size][size];
		if (size == 10) {
			width = height = 700.0 / 10.0;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					squares[i][j] = new MapFieldSquare(i, j, width, height, squareNumber++);
					grid10.add(squares[i][j], j, i);
				}
			}
		} else if (size == 9) {
			width = height = 702.0 / 9.0;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					squares[i][j] = new MapFieldSquare(i, j, width, height, squareNumber++);
					grid9.add(squares[i][j], j, i);
				}
			}
		} else if (size == 8) {
			width = height = 696.0 / 8.0;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					squares[i][j] = new MapFieldSquare(i, j, width, height, squareNumber++);
					grid8.add(squares[i][j], j, i);
				}
			}
		} else { //7
			width = height = 100.0;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					squares[i][j] = new MapFieldSquare(i, j, width, height, squareNumber++);
					grid7.add(squares[i][j], j, i);
				}
			}
		}
		for(int i=0; i<MainViewController.listFiguresCoordinates.get(index).size(); i++) {
			squares[MainViewController.listFiguresCoordinates.get(index).get(i).getI()][MainViewController.listFiguresCoordinates.get(index).get(i).getJ()]
					.listViewColor();
		}
	}
}
