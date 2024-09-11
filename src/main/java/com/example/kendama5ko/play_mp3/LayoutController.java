package com.example.kendama5ko.play_mp3;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {

    private MP3Player mp3Player;
    private MediaPlayer mediaPlayer;

    @FXML
    private Slider volumeSlider;

    public void setMP3Player(MP3Player mp3Player) {
        this.mp3Player = mp3Player;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Sliderの初期設定
        volumeSlider.setValue(50);  // 初期音量を50に設定（必要に応じて変更）

        // スライダーが動かされた時の処理
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // MP3Playerクラスの音量調整メソッドを呼び出す
            mp3Player.updateVolume(volumeSlider);
        });

    }

}
