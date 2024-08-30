package com.example.kendama5ko.play_mp3;


import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MP3Player extends Application {
	
	private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MP3 Player");



        // ファイルを選択するボタンを作成
        Button selectFileButton = new Button("MP3ファイルを選択");

        // ファイルを選択するボタンが押されたときの動作を設定
        selectFileButton.setOnAction(e -> {
            FileChooser audioFileChooser = new FileChooser();
            audioFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.m4a"));
            File selectedAudioFile = audioFileChooser.showOpenDialog(primaryStage);
            
            if (selectedAudioFile != null) {
                // メディアプレイヤーがすでに存在する場合は停止してリセット
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }
                // 新しいファイルを再生
                Media media = new Media(selectedAudioFile.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            }
        });
            
        Button playButton = new Button("Play");
        playButton.setOnAction(e -> mediaPlayer.play());

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> mediaPlayer.pause());

        Button stopButton = new Button("Stop");
        stopButton.setOnAction(e -> mediaPlayer.stop());

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(selectFileButton, playButton, pauseButton, stopButton);

        Scene scene = new Scene(vBox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
