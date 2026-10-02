package com.example.prg_maturita;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import javafx.application.Application;
import javax.swing.Timer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.Button;

import java.awt.*;

public class Tile {

    private int id;
    private Button button;
    private Boolean isDark;
    private final Image tileImage;

    private Boolean hasKamen;
    private Boolean hasDama;

    public Tile(int id, Boolean isDark){
        this.id = id;
        this.button = new Button(null);
        this.button.setMinSize(135, 135);
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


    private ImageView imageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(135);
        imageView.setFitWidth(135);
        imageView.setPreserveRatio(true); //
        return imageView;
    }





















}
