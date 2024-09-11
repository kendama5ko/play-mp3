package com.example.kendama5ko.play_mp3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MP3Player extends Application {

    private MediaPlayer mediaPlayer;
    private Slider timeSlider;
    private Slider volumeSlider;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("MP3 Player");


        // スライダーを作成
        timeSlider = new Slider(0, 100, 0);

        // メディアの進行に合わせてスライダーを動かす
        if (mediaPlayer != null) {
        mediaPlayer.currentTimeProperty().
                addListener((observable, oldTime, newTime) -> playingTimeSlider());
        }

        // volumeSliderを作成
        initializeVolumeSlider();

        // ファイルを選択するボタンを作成
        Button selectFileButton = createSelectFileButton(primaryStage,
                volumeSlider);

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

        // FXMLファイルの読込
        String fxmlPath = "/com/example/kendama5ko/play_mp3/MP3PlayerLayout.fxml";
        URL location = getClass().getResource(fxmlPath);
        FXMLLoader fxmlLoader = new FXMLLoader(location);

        // シーングラフの作成
        //fxmlLoader.setRoot( new VBox() );
        AnchorPane fxmlPane = fxmlLoader.load();

        // MP3PlayerのインスタンスをLayoutControllerに渡す
        LayoutController layoutController = fxmlLoader.getController();
        layoutController.setMP3Player(this);

        VBox rootVBox = new VBox(10);
        rootVBox.getChildren().addAll(fxmlPane, sliderHBox,
                playControlButtonHBox,
                border);

        Scene scene = new Scene(rootVBox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * ファイルを選択するダイアログを表示するためのボタンを作成
     * @param primaryStage 最上位のJavaFXコンテナ
     * @param volumeSlider 音量を制御するためのスライダー
     * @return ファイルを選択するボタン
     */
    public Button createSelectFileButton(Stage primaryStage,
                                          Slider volumeSlider) {
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

                // 再生が始まったらスライダーの最大値を設定
                mediaPlayer.setOnReady(() -> {
                    Duration totalTime = mediaPlayer.getMedia().getDuration();
                    timeSlider.setMax(totalTime.toSeconds());
                });

                // 再生スライダーを再生時間に合わせる
                mediaPlayer.currentTimeProperty().
                        addListener((observable, oldTime, newTime) -> playingTimeSlider());

                // ボリュームスライダーの音量を反映して再生を開始
                updateVolume(volumeSlider);
                mediaPlayer.play();
            }
        });
        return selectFileButton;
    }

    /**
     * ボリュームスライダーを作成
     */
    public void initializeVolumeSlider() {
        volumeSlider = new Slider(0, 1, 0.3); // 0から1の範囲で、デフォルト値0.5
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                updateVolume(volumeSlider);
            }
        });
    }


    /**
     * 音量を更新する
     * @param volumeSlider 音量を制御するためのスライダー
     */
    public void updateVolume(Slider volumeSlider) {
        if (mediaPlayer != null) {
            double volume = volumeSlider.getValue();
            volume = Math.pow(volume, 2);
            mediaPlayer.setVolume(volume);
        }
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