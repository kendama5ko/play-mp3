package com.example.kendama5ko.play_mp3;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class MP3Player extends Application {

    private MediaPlayer mediaPlayer;
    private Slider timeSlider;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MP3 Player");


        // スライダーを作成
        timeSlider = new Slider(0, 100, 0);

        // メディアの進行に合わせてスライダーを動かす
        if (mediaPlayer != null) {
        mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
            playingTimeSlider();
        });
        }

        // スライダーの作成と初期設定
        Slider volumeSlider = new Slider(0, 1, 0.3);  // 最小0、最大1、初期値0.3

        // スライダーの値を変更することで音量を調整
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                // 2乗して対数的に変換する（こちらの方が80%-100%でも音量の変化を感じられる）
                double volume = Math.pow(newValue.doubleValue(), 2);
                mediaPlayer.setVolume(volume);
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
        HBox sliderHBox = new HBox(10);
        sliderHBox.getChildren().addAll(timeSlider, volumeSlider);
        timeSlider.setPadding(new Insets(30, 0, 0, 5));
        volumeSlider.setPadding(new Insets(30, 10, 0, 20));

        // 再生と音量のスライダーの簡易レスポンシブデザイン
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        HBox.setHgrow(volumeSlider, Priority.SOMETIMES);
        volumeSlider.setMaxWidth(150);

        // ボタンを横一列に表示
        HBox playControlButtonHBox = new HBox(10);
        playControlButtonHBox.setPadding(new Insets(10, 10, 10, 10));
        playControlButtonHBox.setAlignment(Pos.CENTER);
        playControlButtonHBox.getChildren().addAll(playButton, pauseButton, stopButton);


        BorderPane border = new BorderPane();
        border.setLeft(selectFileButton);
        BorderPane.setAlignment(volumeSlider, Pos.CENTER);


        VBox rootVBox = new VBox(10);
        rootVBox.getChildren().addAll(sliderHBox, playControlButtonHBox, border);

        Scene scene = new Scene(rootVBox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * ファイルを選択するダイアログを表示するためのボタンを作成
     * @param primaryStage
     * @param volumeSlider
     * @return
     */
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

    /**
     * スライダーをメディアの進行に合わせて進める
     */
    private void playingTimeSlider() {
        Duration currentTime = mediaPlayer.getCurrentTime();
        timeSlider.setValue(currentTime.toSeconds());
    }
    public static void main(String[] args) {
        launch(args);
    }
}