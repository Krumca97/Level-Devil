package projekt.frontEnd;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class SoundManager {

    private static MediaPlayer backgroundPlayer;

    public static void playBackgroundMusic() {

        if (backgroundPlayer != null) return;

        Media media = new Media(
                Objects.requireNonNull(SoundManager.class.getResource("/projekt/music.mp3")).toExternalForm());

        backgroundPlayer = new MediaPlayer(media);
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundPlayer.setVolume(0.4);
        backgroundPlayer.play();
    }

    public static void stopBackgroundMusic() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
            backgroundPlayer = null;
        }
    }
}
