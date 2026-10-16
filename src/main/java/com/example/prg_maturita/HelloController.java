package com.example.prg_maturita;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javafx.scene.control.Button;

import java.util.ArrayList;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private GridPane deska;


    Tile[][] tiles = new Tile[8][8];
    int[][] directions = new int[4][7];
    ArrayList<Tile> jumpableTiles = new ArrayList<>();

    private Tile checkerTile;
    private Tile destinationTile;


    //vygeneruje policka do listu tiles
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

    //zobrazo policka
    public void displayTiles(){
        for (Tile[] sloupec : tiles) {
            for (Tile t : sloupec) {

                Button btn = t.getButton();
                btn.setOnAction( e -> handleTileClick(t));
                deska.add(btn, t.getCol(), t.getRow());
            }
        }

    }

    //vypne svetle policka/tlacitka
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
        //System.out.println("Tile Clicked");
        //tile.outlineTile();

        //pokud klikne na označené pole
        if(tile.isOutlined()){
            destinationTile = tile;
            clearAllOutlines();

            moveTile(checkerTile, destinationTile);
        }else{
            //pokud klikne an fugurku nebo prazdny tile
            clearAllOutlines();
            if (tile.getTileState()!=0) {
                checkerTile = tile;
                calculateDirections(checkerTile );
                addJumpableTiles();
                for(Tile jumpable : jumpableTiles){
                    jumpable.outlineTile();
                }
            }
        }



    }


    public void clearAllOutlines(){
        for(Tile jumpable : jumpableTiles){
            jumpable.clearOutlineTile();
        }
    }

    public void moveTile(Tile checkerTile, Tile destinationTile) {

        destinationTile.setTileState(checkerTile.getTileState());
        destinationTile.setDarkPlayer(checkerTile.getDarkPlayer());
        destinationTile.updateTileImage();

        checkerTile.setTileState(0);
        checkerTile.setDarkPlayer(null);

        destinationTile.updateTileImage();
        checkerTile.updateTileImage();
    }


    boolean playerDark = true;
    //optimalizovat
    public void calculateDirections(Tile tile){
        for (int[] direction : directions) {
            java.util.Arrays.fill(direction, -1);
        }

        int  x = -1;
        if(!playerDark){
            x = 1;
        }
/*------------------------------------------------------------------------------------
        if(tile.getTileState()==1){
            //leva
            int leva1 = tile.getId()+x*9;
            int leva2 = leva1+x*9;
            if(leva1>=0 && leva1<=63){
                directions[0][0] = leva1;
                if (leva2>=0){
                    directions[0][1] = leva2;
                }
            }
            //prava
            int prava1 = tile.getId()+x*7;
            int prava2 = prava1+x*7;
            if(prava1>=0){
                directions[1][0] = prava1;
                if (prava2>=0 && prava2<=63){
                    directions[1][1] = prava2;
                }
            }

----------------------------------------------------------------------------------*/
        if(tile.getTileState()==1){
            int col = tile.getCol();
            //leva
            int leva1 = tile.getId()+x*9;
            int leva2 = leva1+x*9;
            if(col>=1 &&leva1>=0 && leva1<=63){
                directions[0][0] = leva1;
                if (col>=2 && leva2>=0 && leva2<=63){
                    directions[0][1] = leva2;
                }
            }
            //prava
            int prava1 = tile.getId()+x*7;
            int prava2 = prava1+x*7;
            if(col<=6 && prava1>=0 && prava1<=63){
                directions[1][0] = prava1;
                if (col<=5 && prava2>=0 && prava2<=63){
                    directions[1][1] = prava2;
                }
            }
        }else if(tile.getTileState()==2){
        //chybý
        }
    }
/*
    public void checkTilesForForcedJumpable(){
        for (int[] direction : directions) {
            for (int id : col) {
                Tile t = tiles[id][direction[0]];
            }
        }

        for (int[] directionGroup : directions) {
            for (int id : directionGroup) {

                Tile targetTile = getTileByDirections(id);

                if (targetTile.getTileState() == 0) {
                    jumpableTiles.add(targetTile);
                }

            }
        }
    }
*/


    public void addJumpableTiles() {
        jumpableTiles.clear();

        int[] ids = {directions[0][0], directions[1][0]};
        for (int id : ids) {
            if (id >= 0 && id <= 63) {

                int row = id / 8;
                int col = id % 8;

                Tile targetTile = tiles[row][col];

                if (targetTile.getTileState() == 0) {
                    jumpableTiles.add(targetTile);
                }
            }

        }


    }




    @FXML
    public void initialize() {
        generateTiles();
        displayTiles();
        disableLightTiles();
    }































}
