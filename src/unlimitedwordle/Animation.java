package unlimitedwordle;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javafx.animation.PathTransition;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Constructs the Animation that is displayed when the user is correct.
 */
public class Animation {
    
    private Defines DEF = new Defines();
    private Group topmostRoot;
    
    public Animation(Group topmostRoot) {
        this.topmostRoot = topmostRoot;
    }
    
    /**
     * Show the animation
     */
    public void show() {
        Pane dancePane = new Pane();
        dancePane.setViewOrder(0);

        // Create ImageView for each image and show its animation on the screen
        for (int i = 0; i < DEF.FRUIT_IMAGES.length; i++) {
            String fruitName = DEF.FRUIT_IMAGES[i];
            ImageView fruitImage = DEF.IMAGE_VIEW.get(fruitName);

            dancePane.getChildren().addAll(fruitImage);
            showFruit(fruitImage, i + 1);
        }

        // Get the scene that belongs to `topmostRoot`
        Stage stage = (Stage) topmostRoot.getScene().getWindow();

        topmostRoot.getChildren().add(dancePane);
        stage.show();
    }
    
    /**
     * Show a linear animation of a fruit ImageView
     * 
     * @param fruit ImageView
     * @param (the index of the fruit image) + 1
     */
    private void showFruit(ImageView fruit, int index) {
        Path path = new Path();

        path.getElements().add(new MoveTo(300, 800));
        path.getElements().add(new LineTo(100 * index, -100));

        PathTransition pathTransition = new PathTransition();

        pathTransition.setAutoReverse(true);
        pathTransition.setCycleCount(DEF.TRANSITION_CYCLE);
        pathTransition.setDuration(Duration.seconds(DEF.TRANSITION_DURATION));
        pathTransition.setNode(fruit);
        pathTransition.setPath(path);

        pathTransition.play();

        // Stop the path transition after 4 seconds
        CompletableFuture.delayedExecutor(4, TimeUnit.SECONDS).execute(() -> {
            pathTransition.stop();
        });
    }
    
}
