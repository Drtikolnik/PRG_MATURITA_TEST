package com.example.prg_maturita;

import javafx.scene.image.Image;

public class Tile {

    private int id;
    private Boolean isDark;
    private final Image tileImage;

    private Boolean hasKamen;
    private Boolean hasDama;

    public Tile(int id, Boolean isDark){
        this.id = id;
        this.isDark = isDark;

        if(isDark){
            this.tileImage = loadImage("/com/example/PRG_MATURITA_TEST/imgs/dark.png");
        }else {
            this.tileImage = loadImage("/com/example/PRG_MATURITA_TEST/imgs/light.png");
        }

    }

    private Image loadImage(String path) {
        var stream = getClass().getResourceAsStream(path);
        if (stream == null){
            throw new IllegalArgumentException("Can't load image from "+path);
        }
        return new Image(stream);
    }

}
