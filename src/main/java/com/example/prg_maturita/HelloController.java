package com.example.prg_maturita;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javafx.scene.control.Button;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private GridPane deska;


    Tile[][] tiles = new Tile[8][8];

    //vygeneruje pole do listu tiles
    public void generateTiles(){
    int id = 0;
    boolean isDark;

        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++) {
                isDark = false;

                if((col+row+1)%2 == 0) {
                    isDark = true;
                }
                tiles[row][col] = new Tile(row*8+col, row, col, isDark);

            }
        }

    }

    public void displayTiles(){
        for (Tile[] sloupec : tiles) {
            for (Tile t : sloupec) {

                Button btn = t.getButton();
                btn.setOnAction( e -> handleTileClick(t));
                deska.add(btn, t.getCol(), t.getRow());
            }
        }

    }

    public void disableLightTiles(){
        for (Tile[] sloupec : tiles) {
            for (Tile t : sloupec) {
                Button btn = t.getButton();
                if(!t.getDarkTile()){
                    btn.setDisable(true);
                    btn.setStyle("-fx-opacity: 1.0;");
                }

            }
        }
    }





    @FXML
    protected void handleTileClick(Tile tile) {
        System.out.println("Tile Clicked");
    }

    @FXML
    public void initialize() {
        generateTiles();
        displayTiles();
        disableLightTiles();
    }































}
