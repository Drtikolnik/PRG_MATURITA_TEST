package com.example.prg_maturita;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private GridPane gridPane;


    Tile[][] tiles = new Tile[8][8];
    int id = 0;
    Boolean isDark = false;
    public void generateTiles(){
        for(int col = 0; col < 8; col++) {
            for(int row = 0; row < 8; row++){

                tiles[col][row] = new Tile(col+row, true);
//dodělaaaaaaaaaaaaaaat


            }


        }

    }




    @FXML
    protected void onHelloButtonClick() {

    }































}
