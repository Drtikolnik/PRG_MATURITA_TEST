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
    int[][] directions = new int[4][7]; //0=leva nahhore / 1=vpravo nahore / 2=vlevo dole / 3=vpravo dole
    ArrayList<Tile> jumpableTiles = new ArrayList<>();
    ArrayList<Tile> forcedTiles = new ArrayList<>();

    private Tile checkerTile;
    private Tile destinationTile;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    //zobrazi policka
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //pokud user klikme na políčko...
    @FXML
    protected void handleTileClick(Tile tile) {
        //System.out.println("Tile Clicked");
        //tile.outlineTile();


        if (tile.isOutlined()) { //pokud klikne na označené pole
            destinationTile = tile;
            clearAllOutlines();

            moveTile(checkerTile, destinationTile);
            playerDark = !playerDark;
        } else { //pokud klikne an fugurku nebo prazdny tile
            clearAllOutlines();
            if (tile.getTileState() != 0) { //pokiud tile neni prazdny
                checkerTile = tile;
                calculateDirections(checkerTile);
                addJumpableTiles();
                for (Tile jumpable : jumpableTiles) {
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

        //leva nahore, prava nahore, leva dole, prava dole
        int[] krok    = {-9, -7, 7, 9};      // o kolik se změní id
        int[] sloupec = {-1, 1, -1, 1};      // o kolik se změní sloupec

        for (int strana = 0; strana < 4; strana++) { //pro každou diagonalni stranu
            int id = tile.getId();// nacte se id aktualniho tilu
            int col = tile.getCol();//nacte se col tilu pro kontrolu

            for (int mistoVListu = 0; mistoVListu < 7; mistoVListu++) { // pro kazde misto 1-7 v jedne strane
                id = id + krok[strana];
                col = col + sloupec[strana];

                if (id < 0 || id > 63 || col < 0 || col > 7) { // pokud je id mimo desku
                    //nic se nestane
                }else{ // pokud je validni
                    directions[strana][mistoVListu] = id; // ulozi se do directions
                }

            }
        }

    }

    public Tile getTileById(int id) { // returne tile s timto id
        return tiles[id / 8][id % 8]; //vypocita row a col na desce z id
    }

    public boolean hasForcedJump(Tile tile) {
        calculateDirections(tile);  // spocita directions pro tuhle figurku

        if(tile.getTileState() == 1){ // pokud kamen
            // tmavy hrac jde nahoru (0 a 1)  svetly dolu (2 a 3)
            int prvniStrana = 0;
            if (!tile.getDarkPlayer()) {
                prvniStrana = 2;
            }

            for (int strana = prvniStrana; strana < prvniStrana + 2; strana++) {
                int id1 = directions[strana][0]; // sousedni policko
                int id2 = directions[strana][1]; // policko za sousednim polickem

                if (id1!=-1 && id2!=-1) { // pokud jsou id na desce
                    Tile sousedni= getTileById(id1);
                    Tile zaSousednim = getTileById(id2);

                    // pukud je sousedni policko figruka a je soupere && pokud je policko za nim volne
                    if ((sousedni.getTileState()!=0 && sousedni.getDarkPlayer()!=tile.getDarkPlayer()) && (zaSousednim.getTileState()==0)) {
                        return true; // byl nalezen forced skok
                    }
                }
            }

        }else if(tile.getTileState() == 2){ // pokud dama
            //////dokoncit
        }

        return false;
    }


    public void findForcedTiles() { // projde celou desku a ulozi tile ktery musi udelat forced jump
        forcedTiles.clear();

        for (Tile[] row : tiles) { // projde row
            for (Tile t : row) { // projde col

                if (t.getTileState()!=0 && t.getDarkPlayer()==playerDark) { // pokud je to figurka a je hrace na tahu
                    if (hasForcedJump(t)) {
                        forcedTiles.add(t);
                    }
                }
            }
        }
    }






    public void addJumpableTiles() {
        jumpableTiles.clear();


        int[] ids = new int[4];
        java.util.Arrays.fill(ids, -1);

        ids[0] = directions[0][0];
        ids[1] = directions[1][0];
        ids[2] = directions[2][0];
        ids[3] = directions[3][0];


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

        //zkouska
        findForcedTiles();
        System.out.println("Figurky, které musí skákat: " + forcedTiles.size());
    }































}
