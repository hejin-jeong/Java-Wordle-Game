package unlimitedwordle;
import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Maintains and initializes all the fixed parameters that control the application.
 */
public class Defines {
    
    // coefficients related to the application & scene
    final int APP_HEIGHT = 700;
    final int APP_WIDTH = 700;
    final int SCENE_HEIGHT = 700;
    final int SCENE_WIDTH = 700;
   
    // coefficients related to the image blobs
    final int BLOB_WIDTH = 40;
    final int BLOB_HEIGHT = 40;
    
    // coefficients related to time
    final double TRANSITION_DURATION = 4;
    final double TRANSITION_TIME = 0.5;
    final double TRANSITION_DELAY = 0.5;
    final int TRANSITION_CYCLE = 20;
   
    final String STAGE_TITLE = "Unlimited Wordle";
   
    // variables related to image files
    private final String IMAGE_DIR = "../resources/images/";
    private final String FRUIT_PATH = "../resources/images/fruits/";
    private final String[] IMAGE_FILES = {"sound on", "sound off","trampoline", "doraemon"};
    public final String[] FRUIT_IMAGES = { "apple", "banana", "berry", "cherry", "grapes", "kiwifruit", "orange", "prunes", "strawberry" };
    
    // hashmap to access the images & their container image views by their file names directly
    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, ImageView> IMAGE_VIEW = new HashMap<String, ImageView>();
    
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    final HashMap<String, Image> IMAGES = new HashMap<String, Image>();
     
    
    Defines(){
        
        //creating images components for each of the image files
        for(int i=0; i<IMAGE_FILES.length; i++) {
            if(i<=1) {
                Image img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
                IMAGE.put(IMAGE_FILES[i],img);
            } else if (i==2) {
                Image img = new Image(pathImage(IMAGE_FILES[i]), SCENE_WIDTH, 0.5*SCENE_HEIGHT, false, false);
                IMAGE.put(IMAGE_FILES[i],img);
            }  else {
                Image img = new Image(pathImage(IMAGE_FILES[i]), 8*BLOB_WIDTH, 8*BLOB_HEIGHT, false, false);
                IMAGE.put(IMAGE_FILES[i],img);
            }
        }
        
        for (int i = 0; i < FRUIT_IMAGES.length; i++) {
            Image image = new Image(getImagePath(FRUIT_IMAGES[i]), 50, 50, false, false);
            IMAGES.put(FRUIT_IMAGES[i], image);
        }
        
        //creating image views containing each of the images
        for(int i=0; i<IMAGE_FILES.length; i++) {
            ImageView imgView = new ImageView(IMAGE.get(IMAGE_FILES[i]));
            IMVIEW.put(IMAGE_FILES[i],imgView);
        }
        
        for (int i = 0; i < FRUIT_IMAGES.length; i++) {
            ImageView imageView = new ImageView(IMAGES.get(FRUIT_IMAGES[i]));
            IMAGE_VIEW.put(FRUIT_IMAGES[i], imageView);
        }
        
    }
    
    /**
     * Helper method to build complete URL of the image files from their names and locations
     * @param filepath relative filepath of image file
     * @return full path of the file.
     */
    public String pathImage(String filepath) {
        String fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
        return fullpath;
    }
    
    /**
     * Build the complete URL of the image files from their names and locations
     * 
     * @param filePath
     * @return the full path of the file
     */
    public String getImagePath(String filePath) {
        return getClass().getResource(this.FRUIT_PATH + filePath + ".png").toExternalForm();
    }
    
}