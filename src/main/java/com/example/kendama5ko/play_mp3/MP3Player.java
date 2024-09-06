package com.example.kendama5ko.play_mp3;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MP3Player extends Application {

    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MP3 Player");

        // スライダーの作成と初期設定
        Slider volumeSlider = new Slider(0, 1, 0.3);  // 最小0、最大1、初期値0.3
//        volumeSlider.setShowTickLabels(true);
//        volumeSlider.setShowTickMarks(true);


        // スライダーの値を変更することで音量を調整
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue());
            }
        });

        // ファイルを選択するボタンを作成
        Button selectFileButton = createSelectFileButton(primaryStage, volumeSlider);

        Button playButton = new Button("Play");
        playButton.setOnAction(e -> mediaPlayer.play());

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> mediaPlayer.pause());

        Button stopButton = new Button("Stop");
        stopButton.setOnAction(e -> mediaPlayer.stop());

        // レイアウト
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(100, 10, 10, 10));
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(playButton, pauseButton, stopButton);

        BorderPane border = new BorderPane();
        border.setLeft(selectFileButton);
        border.setRight(volumeSlider);
        BorderPane.setAlignment(volumeSlider, Pos.CENTER);

        VBox rootVBox = new VBox(10);
        rootVBox.getChildren().addAll(hBox, border);

        Scene scene = new Scene(rootVBox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createSelectFileButton(Stage primaryStage, Slider volumeSlider) {
        Button selectFileButton = new Button("MP3ファイルを選択");
        String[] extensions = {"*.mp3", "*.wav", "*.m4a"};
        // ファイルを選択するボタンが押されたときの動作を設定
        selectFileButton.setOnAction(e -> {
            FileChooser audioFileChooser = new FileChooser();
            audioFileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("Audio Files", extensions));
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

                // ボリュームスライダーの音量を反映して再生を開始
                mediaPlayer.setVolume(volumeSlider.getValue());
                mediaPlayer.play();
            }
        });
        return selectFileButton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}