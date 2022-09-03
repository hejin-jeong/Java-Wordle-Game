package unlimitedwordle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Constructs grid for Medium level and plays the game.
 */

public class Easy implements LevelSelector {

    private int roundCount; // The row which the user is working on
    private int score; // The current score
    private String answer; // The answer

    int nrows = 7; // Number of rows, i.e. the number of allowed attempts
    int ncols = 5; // Number of rows, i.e. the length of words

    private Label solutionLabel; // The GUI label for displaying answer
    private TextField scoreField; // The GUI text field for displaying score
    private Animation animation; // The animation object used for winning

    private String[] letters = new String[(nrows * ncols)]; // initialize letter grids

    private int index;
    private int guessIndex;
    private int row;

    private List<String> userInput; // Stores user input

    private Mechanics mechanics; // The mechanics object for comparing user input to answer

    private GridPane letterGrid = new GridPane();

    /**
     * Constructor for Easy level
     * 
     * @param answer
     * @param scoreField
     * @param solutionLabel
     * @param cum_score
     * @param animation
     */
    public Easy(String answer, TextField scoreField, Label solutionLabel, int cumulativeScore, Animation animation) {
        try {
            mechanics = new Mechanics();
            this.answer = answer;
            this.scoreField = scoreField;
            this.solutionLabel = solutionLabel;
            this.score = cumulativeScore + nrows;
            this.animation = animation;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIsCorrect() {
        return this.mechanics.getIsCorrect();
    }

    public boolean checkGridFull() {
        return index == nrows * ncols;
    }

    public int getRoundCount() {
        return this.roundCount;
    }

    public int getScore() {
        return this.score;
    }

    public void setSolutionLabelEmpty() {
        solutionLabel.setText("");
    }

    /**
     * Creates letter grid based on nrows and ncols.
     */
    @Override
    public GridPane createGrid() {

        for (int i = 0; i < ncols; i++) {
            for (int j = 0; j < nrows; j++) {
                TextField letterBox = new TextField();
                letterBox.setPrefSize(80, 80);
                letterBox.setText("");
                letterBox.setEditable(false);
                letterGrid.add(letterBox, i, j);
            }
        }

        return letterGrid;

    }

    /**
     * Set user input Receives information from keyboard class.
     */
    @Override
    public void setUserInput(List<String> attempt) {
        userInput = attempt;
    }

    /**
     * Compares the user input with the answer and display appropriate color on the
     * grid.
     */
    @Override
    public void play(int roundCount) {

        // Stores the array that indicates the correctness of each character in the user
        // input based on the answer
        List<Integer> resultList = mechanics.compareAnswer(answer.toUpperCase(), String.join("", userInput));

        // Changes the colors of letter grids on current row based on correctness
        for (int j = 0; j < 5; j++) {

            if (resultList.get(j) == -1) {
                TextField letterBox = new TextField();
                letterBox.setPrefSize(80, 80);
                letterBox.setText(userInput.get(j));
                letterBox.setEditable(false);
                letterBox.setStyle("-fx-font-size : 30; -fx-background-color: #FF0000;"); // To red
                letterGrid.add(letterBox, j, roundCount);
            } else if (resultList.get(j) == 0) {
                TextField letterBox = new TextField();
                letterBox.setPrefSize(80, 80);
                letterBox.setText(userInput.get(j));
                letterBox.setEditable(false);
                letterBox.setStyle("-fx-font-size : 30; -fx-background-color: #FFFF00;"); // To yellow
                letterGrid.add(letterBox, j, roundCount);
            } else {
                TextField letterBox = new TextField();
                letterBox.setPrefSize(80, 80);
                letterBox.setText(userInput.get(j));
                letterBox.setEditable(false);
                letterBox.setStyle("-fx-font-size : 30; -fx-background-color: #14d714;"); // To green
                letterGrid.add(letterBox, j, roundCount);
            }
        }

        // Tracks the working row
        this.roundCount = roundCount;

        // If user got the true answer (win)
        if (mechanics.getIsCorrect()) {

            // Displays score, answer, and animation
            animation.show();
            scoreField.setText(Integer.toString(score));
            solutionLabel.setText("Answer is: " + answer);
        }

        // If didn't get true answer and exhausted all attempts
        else if (roundCount == nrows - 1) {

            // Decrement score, display answer and score
            score--;
            scoreField.setText(Integer.toString(score));
            solutionLabel.setText("Answer is: " + answer);
        }

        // If the input is wrong, but has attempts left
        else {
            score--;
        }
    }

    /**

     * Takes a String of a single letter (checked to be a single letter in
     * Keyboard.java) and "types" it onto the letterGrid. Makes a letterBox
     * containing the letter and replaces the currently existing letterBox already
     * in the letterGrid, finding the correct location using the index variables
     * initialized. Limits each row to five letters, does not add any more boxes
     * once limit is reached. Also, limits amount of boxes able to be added using
     * ncols and nrows, preventing overflow.
     * 
     * @param letter that passed from Keyboard.java to be typed into the letterGrid
     */
    public void typeLetter(String letter) {

        TextField letterBox = new TextField();
        letterBox.setText(letter);
        letterBox.setStyle("-fx-font-size : 30;");

        if(!(index >= letters.length) && !(guessIndex >= 5)) {
            letters[index] = letter;
            letterGrid.add(letterBox, index % 5, row);
            index++;
            guessIndex++;
        }

    }

    /**
     * Takes no parameters "Deletes" a letter finding the correct coordinates in the
     * letterGrid using the index variables initialized. Creates a letterBox with no
     * text inside and replaces the currently existing letterbox at the location of
     * the deleted letter. Will not "delete" a box if the user is currently in the
     * first box of a row (This doubles as the "locking" mechanism for previous
     * rows).
     */
    public void deleteLetter() {

        TextField letterBox = new TextField();
        letterBox.setText("");

        if (!(guessIndex == 0 || index == 0)) {
            letterGrid.add(letterBox, guessIndex - 1, row);
            letters[index] = "";
            index--;
            guessIndex--;
        }

    }

    /**
     * Takes no parameters, moves on to the next row in the letterGrid if and only
     * if the current guess has 5 letters. Once the guess is entered, it cannot be
     * edited thanks to the limitations set in deleteLetter().
     */
    public void enterGuess() {

        if (guessIndex >= 4) { // guessIndex starts at 0, so a 5 letter guess would have a guessIndex of 4
            guessIndex = 0;
            if (row < nrows) {
                row++;
            }
        }
    }

}