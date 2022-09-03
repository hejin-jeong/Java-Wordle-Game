package unlimitedwordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Creates a window for the game.
 */
public class UnlimitedWordle extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // Get images
    private Defines DEF = new Defines();

    ImageView play = DEF.IMVIEW.get("sound on");
    ImageView pause = DEF.IMVIEW.get("sound off");

    // Get Music
    private String filePath = "/resources/Medieval2.mp3";
    Sound bgm = new Sound(filePath);

    private ComboBox<String> levelsMenu = new ComboBox<>();

    private ArrayList<String> food = new ArrayList<>();

    private GridPane letterGrid;
    private HBox gridHolder = new HBox(1);

    private GridPane buttonGrid = new GridPane(); // GUI grid
    private TextField score = new TextField(); // Score keeping TextField
    private Keyboard keyboard = new Keyboard(); // Virtual keyboard
    private Label solution = new Label(); // TextField holding answer, initially invisible

    private Scene firstScene;
    private Group topmostRoot = new Group();

    private static final String EASY = "Easy";
    private static final String MEDIUM = "Medium";
    private static final String HARD = "Hard";

    private LevelSelector level;
    private Object selected;

    private int cumulativeScore = 0;
    Animation animation = new Animation(this.topmostRoot);

    Button mute = new Button();
    Button newGame = new Button("New Game");
    Button exit = new Button("Exit");

    // Whether music is playing or muted
    private Boolean toggleValue = true;

    /**
     * start method
     * 
     * @param Stage primaryStage
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception {
        // Initialize database (string array) by reading from text file
        setUpDatabase();

        // Create the drop-down menu
        createMenu();

        // Create grid and link keyboard to it
        createGrid(levelsMenu.getSelectionModel().getSelectedItem().toString());

        // Link the GUI buttons with mouse events
        linkButtonsWithEvents();

        // Play music and add graphics to mute button
        bgm.playClip();

        Label menuLbl = new Label("Difficulty: ");
        menuLbl.setStyle(" -fx-font-size: 20; ");
        Label scoreLbl = new Label("Score: ");
        scoreLbl.setStyle(" -fx-font-size: 20; ");

        score.setText("0");
        score.setEditable(false);
        score.setStyle(" -fx-text-box-border : black; ");
        score.setStyle("-fx-focus-color : black;");
        
        mute.setGraphic(play);

        newGame.setStyle("-fx-background-color: #6b88c2;");
        exit.setStyle("-fx-background-color: #e88787;");

        buttonGrid.setVgap(20);
        buttonGrid.add(scoreLbl, 0, 0);
        buttonGrid.add(score, 1, 0);
        buttonGrid.add(mute, 1, 1);
        buttonGrid.add(newGame, 1, 2);
        buttonGrid.add(exit, 1, 3);
        buttonGrid.add(menuLbl, 0, 4);
        buttonGrid.add(levelsMenu, 1, 4);

        // Create keyboard VBox
        VBox keys = keyboard.createKeyboard();

        keys.addEventHandler(ActionEvent.ANY, (e) -> {
            if (!levelsMenu.isDisabled()) {
                toggleLevelsMenu();
            }
            
         // disables newGame button while game is ongoing to prevent reset abuse
            newGame.setDisable(!keyboard.disabled); 
        });

        // HBox holding the keyboard
        HBox keysHolder = new HBox(1);
        keysHolder.getChildren().addAll(keys);
        keysHolder.setAlignment(Pos.CENTER);

        // HBox holding the letter grid
        gridHolder.getChildren().addAll(letterGrid);
        gridHolder.setAlignment(Pos.CENTER);

        // VBox stacking the grid on top of the keyboard (and centering them)
        VBox gridAndKeys = new VBox(2);
        VBox.setMargin(gridHolder, new Insets(0, 0, 30, 0));
        gridAndKeys.getChildren().addAll(gridHolder, keysHolder);
        gridAndKeys.setAlignment(Pos.CENTER);

        // VBox holding the GUI buttons
        VBox buttonsHolder = new VBox(2);
        buttonsHolder.getChildren().addAll(solution, buttonGrid);
        buttonsHolder.setAlignment(Pos.CENTER);

        // HBox to center and position both grid and GUI buttons
        HBox root = new HBox(2);
        HBox.setMargin(buttonsHolder, new Insets(10, 10, 10, 10));
        root.getChildren().addAll(gridAndKeys, buttonsHolder);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #d0e7f2;");

        // Set up firstScene
        this.topmostRoot.getChildren().add(root);

        this.firstScene = new Scene(topmostRoot, 840, 700);
        this.firstScene.getStylesheets().add("resources/style.css");

        // Listens to physical keystrokes
        firstScene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {

//            System.out.println("Physical keystroke registered: " + key.getCode().toString()); // test print

            keyboard.setTyped(key.getCode().toString());
            
         // if the keystroke is not currently disabled, type it
            if (!keyboard.disabledKeys.contains(keyboard.getTyped())) { 
                keyboard.typeLetter(keyboard.getTyped());
            }

            // Locks the difficulty select if first guess is entered on physical keys
            if (!levelsMenu.isDisabled()) {
                // System.out.println("ROOT: difficulty lock if block called"); // test print
                toggleLevelsMenu();
            }
         // disables newGame button while game is ongoing to prevent reset abuse
            newGame.setDisable(!keyboard.disabled);
        });

        primaryStage.setScene(firstScene);
        primaryStage.setTitle("UnlimitedWordle");
        primaryStage.show();

    }

    /**
     * Create difficulty drop-down menu
     */
    private void createMenu() {
        levelsMenu.getItems().addAll(EASY, MEDIUM, HARD);
        levelsMenu.getSelectionModel().select(0);
        levelsMenu.setOnAction(this::itemStateChanged);
    }

    /**
     * Recognize the changes in the difficulty levels
     * 
     * @param evt
     */
    public void itemStateChanged(ActionEvent evt) {
        selected = ((ComboBox<?>) evt.getSource()).getSelectionModel().getSelectedItem();

        if (selected != null) {
            letterGrid = createGrid(selected.toString());
            letterGrid.setFocusTraversable(true);
            gridHolder.getChildren().clear();
            gridHolder.getChildren().add(letterGrid);
        }
    }

    /**
     * Toggle levelsMenu ComboBox enable and disable
     */
    public void toggleLevelsMenu() {
        boolean disable = !levelsMenu.isDisabled();
        levelsMenu.setDisable(disable);
    }

    /**
     * Create a letter grid based on the selected difficulty level
     * 
     * @param the name of the selected difficulty level
     * @return the letter grid
     */
    private GridPane createGrid(String selected) {
        if (selected.equals(EASY)) {
            level = new Easy(getRandomWord(), score, solution, cumulativeScore, animation);
        } else if (selected.equals(MEDIUM)) {
            level = new Medium(getRandomWord(), score, solution, cumulativeScore, animation);
        } else {
            level = new Hard(getRandomWord(), score, solution, cumulativeScore, animation);
        }

        level.setSolutionLabelEmpty();
        letterGrid = level.createGrid();
        keyboard.setLetterGrid(level);
        letterGrid.setFocusTraversable(true);

        return letterGrid;
    }

    /**
     * Attach handle methods to buttons
     */
    private void linkButtonsWithEvents() {
        newGame.setOnMouseClicked(this::buttonClickReset);
        mute.setOnMouseClicked(this::buttonClickMute);
        exit.setOnMouseClicked(this::buttonClickExit);
    }

    /**
     * Handle the event when the reset button is clicked
     * 
     * @param e
     */
    public void buttonClickReset(MouseEvent e) {
//      System.out.println("Reset button clicked");
//      System.out.println(keyboard.printLetters());
//      if(levelsMenu.isDisabled()) {
//          toggleLevelsMenu();
//      }
//      levelsMenu.getSelectionModel().select(0);

        // keep the score from previous round
        cumulativeScore = level.getScore();

        gridHolder.getChildren().clear();

        // create new grid, with cumulative score

//      System.out.println("current level selected: " + levelsMenu.getSelectionModel().getSelectedItem());

        gridHolder.getChildren().add(createGrid(levelsMenu.getSelectionModel().getSelectedItem()));

//        System.out.println("answer: " + keyboard.answer);

        // keep disabling level selector

        // Re-enable keyboard
        if (keyboard.disabled) {
            keyboard.toggleKeyboard();
        }
        keyboard.enableKeys();
        letterGrid.setFocusTraversable(true);
        letterGrid.requestFocus();
        
        
    }

    /**
     * Handle the event when the mute button is clicked
     * 
     * @param e
     */
    public void buttonClickMute(MouseEvent e) {
        // Q: how to decide if the mute button is clicked or not
        if (toggleValue) {
            toggleValue = false;
            bgm.stopClip();
            mute.setGraphic(pause);
        } else {
            toggleValue = true;
            bgm.playClip();
            mute.setGraphic(play);
        }

    }

    /**
     * Handle the event when the exit button is clicked
     * 
     * @param e
     */
    public void buttonClickExit(MouseEvent e) {
        PopupYesNo popup = new PopupYesNo();
        popup.popupDialog(new Text("Are you sure you want to exit?"));
    }

    /**
     * Get the path of the text file and read it into a string list
     * 
     * @throws FileNotFoundException
     */
    public void setUpDatabase() throws FileNotFoundException {
        File file = new File("src/resources/food.txt");
        String absolutePath = file.getAbsolutePath();

        Scanner scanner = new Scanner(new File(absolutePath));

        while (scanner.hasNextLine()) {
            food.add(scanner.nextLine());
        }

        scanner.close();
    }

    /**
     * Return a random word from the text file
     * 
     * @return a random word
     */
    public String getRandomWord() {
        Random random = new Random();

        String answer = food.get(random.nextInt(food.size()));
        System.out.println("answer: " + answer);
        keyboard.setAnswer(answer);
        return answer;
    }

}
