package unlimitedwordle;
import javafx.scene.media.AudioClip;

/**
 * Sets up a sound object by extracting the media from its file path 
 * Provides helper function for minimal control of sound media files.
 *
 */
public class Sound {
    private AudioClip soundEffect;

    /**
     * Constructs sound object from file path of audio file
     * @param filePath relative filepath of audio file
     */
    public Sound(String filePath) {
        soundEffect = new AudioClip(getClass().getResource(filePath).toExternalForm());
    	 
    }

    /**
     * Plays the sound clip
     */
    public void playClip() {
        soundEffect.play();
    }
    
    /**
     * Stops the sound clip
     */
    public void stopClip() {
        if(soundEffect.isPlaying()) soundEffect.stop();
    }
    
}