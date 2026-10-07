package com.example.prg_maturita;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.Button;

public class Tile {

    private int id;
    private int row;
    private int col;
    private Button button;
    private Boolean isDark;
    private final Image tileImage;

    private Boolean hasKamen;
    private Boolean hasDama;

    public Tile(int id, int row, int col, Boolean isDark){
        this.id = id;
        this.row = row;
        this.col = col;
        this.button = new Button(null);
        this.button.setMinSize(100, 100);
        this.button.setMaxSize(100, 100);
        this.isDark = isDark;

        //nastavení grafiky políčka
        if(isDark){
            if(row==0||row==1||row==2){
                this.tileImage = loadImage("/com/example/prg_maturita/imgs/dark_kamen_light.png");
            }else if(row==5||row==6||row==7) {
                this.tileImage = loadImage("/com/example/prg_maturita/imgs/dark_kamen_dark.png");
            }else{
                this.tileImage = loadImage("/com/example/prg_maturita/imgs/dark.png");
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

    public Boolean getIsDark() {
        return isDark;
    }

    public void setIsDark(Boolean dark) {
        isDark = dark;
    }

    public Image getTileImage() {
        return tileImage;
    }

    public Boolean getHasKamen() {
        return hasKamen;
    }

    public void setHasKamen(Boolean hasKamen) {
        this.hasKamen = hasKamen;
    }

    public Boolean getHasDama() {
        return hasDama;
    }

    public void setHasDama(Boolean hasDama) {
        this.hasDama = hasDama;
    }
}
