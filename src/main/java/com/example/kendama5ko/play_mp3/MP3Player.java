package com.example.kendama5ko.play_mp3;


import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MP3Player extends Application {
	private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MP3 Player");

        // ファイルパスを指定（実際のMP3ファイルのパスに変更してください）
        String path = "Jazz Piano BGM.mp3";
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        Button playButton = new Button("Play");
        playButton.setOnAction(e -> mediaPlayer.play());

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> mediaPlayer.pause());

        Button stopButton = new Button("Stop");
        stopButton.setOnAction(e -> mediaPlayer.stop());

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(playButton, pauseButton, stopButton);

        Scene scene = new Scene(vBox, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
