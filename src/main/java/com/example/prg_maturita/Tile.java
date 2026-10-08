package com.example.prg_maturita;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.Button;

public class Tile {

    private int id;
    private int row;
    private int col;
    private Button button;
    private boolean isDarkTile;
    private Boolean isDarkPlayer;
    private Image tileImage;

    private int tileState = 0;// 0==nemá figurku | 1==má kámen | 2==má dámu


    public Tile(int id, int row, int col, boolean isDarkTile) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.button = new Button(null);
        this.button.setMinSize(100, 100);
        this.button.setMaxSize(100, 100);
        this.isDarkTile = isDarkTile;

        //nastavení grafiky políčka
        if(isDarkTile){
            if(row==0||row==1||row==2){
                this.tileImage = loadImage("/com/example/prg_maturita/imgs/dark_kamen_light.png");
                this.tileState = 1;
                this.isDarkPlayer = true;

            }else if(row==5||row==6||row==7) {
                this.tileImage = loadImage("/com/example/prg_maturita/imgs/dark_kamen_dark.png");
                this.tileState = 1;
                this.isDarkPlayer = false;
            }else{
                this.tileImage = loadImage("/com/example/prg_maturita/imgs/dark.png");
                this.tileState = 0;
            }

        }else{
            this.tileImage = loadImage("/com/example/prg_maturita/imgs/light.png");
        }
        button.setGraphic(imageView(tileImage));

    }

    //načtení obrázku ze souboru
    private Image loadImage(String path) {
        var stream = getClass().getResourceAsStream(path);
        if (stream == null){
            throw new IllegalArgumentException("Can't load image from "+path);
        }
        return new Image(stream);
    }

    //načtení obrázku na button
    private ImageView imageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true); //
        return imageView;
    }

    public void outlineTile(Tile tile){
        if(tile.isDarkPlayer==null){
            if(tile.isDarkTile){
                tileImage = loadImage("/com/example/prg_maturita/imgs/dark_outlined.png");
            }
        }else if(tile.isDarkPlayer==true){
            if(tile.tileState==1){
                tileImage = loadImage("/com/example/prg_maturita/imgs/dark_kamen_dark_outlined.png");
            }else if(tile.tileState==2){
                tileImage = loadImage("/com/example/prg_maturita/imgs/dark_dama_dark_outlined.png");
            }
        }else{
            if(tile.tileState==1){
                tileImage = loadImage("/com/example/prg_maturita/imgs/dark_kamen_light_outlined.png");
            }else if(tile.tileState==2){
                tileImage = loadImage("/com/example/prg_maturita/imgs/dark_dama_light_outlined.png");
            }
        }
        button.setGraphic(imageView(tileImage));

    }

    public void unOutlineTile(Tile tile){

    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public boolean getDarkTile() {
        return isDarkTile;
    }

    public void setDarkTile(boolean darkTile) {
        this.isDarkTile = darkTile;
    }

    public Boolean getDarkPlayer() {
        return isDarkPlayer;
    }

    public void setDarkPlayer(Boolean darkPlayer) {
        this.isDarkPlayer = darkPlayer;
    }

    public int getTileState() {
        return tileState;
    }

    public Image getTileImage() {
        return tileImage;
    }

    public void setTileState(int state) {
        if(state==0||state==1||state==2){
            this.tileState = state;
        }else{
            System.err.println("Invalid tile state");
        }
    }


}