package unlimitedwordle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Creates virtual keyboard on the window.
 */

public class Keyboard {

    // Current keystroke typed which is the user input
    private String typed;

    // List of letters typed
    private List<String> attempt = new ArrayList<String>();

    // Current row
    private int roundCount = 0;

    // Access to Easy, Medium, and Hard
    private LevelSelector letterGrid;

    public boolean disabled = false;

    private String answer;

    // Alphabet array
    private final String[] alphabet = { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H",
            "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M" };

    // Add letters to this as they are used if they are NOT in String answer
    public List<String> disabledKeys = new ArrayList<String>();

    private HashMap<Integer, Button> keyLetters;

    public String getTyped() {
        return this.typed;
    }

    public List<String> getUserInput() {
        return this.attempt;
    }

    /**
     * Sets the difficulty level as the level passed in from UnlimitedWordle class
     * 
     * @param level selected by the user
     */
    public void setLetterGrid(LevelSelector level) {
        System.out.println("Keyboard class registered grid change");

        letterGrid = level;
        roundCount = 0;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setTyped(String typed) {
        this.typed = typed;
    }

    /**
     * Checks (boolean) if the button clicked is a letter or not (otherwise ENTER or
     * BACKSPACE)
     * 
     * @param typed the button clicked represented as a String
     * @return true if the button is a letter, otherwise false
     */
    public boolean isLetter(String typed) {
        return typed.length() == 1;
    }

    /**
     * Creates keyboard with different keys on the window
     * 
     * @return keyboard
     */
    public VBox createKeyboard() {

        this.keyLetters = new HashMap<>();

        HBox topKeysRow = new HBox(10);
        topKeysRow.setAlignment(Pos.CENTER);
        HBox midKeysRow = new HBox(11);
        midKeysRow.setAlignment(Pos.CENTER);
        HBox bottomKeysRow = new HBox(7);
        bottomKeysRow.setAlignment(Pos.CENTER);

        // Make alphabet keys
        for (int i = 0; i < alphabet.length; i++) {
            Button key = new Button(alphabet[i]);
            key.setMinWidth(40);
            key.setOnMouseClicked(arg0 -> {
                try {
                    setTyped(arg0);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            keyLetters.put(i, key);
//          System.out.println("Button: " + key.getText()); // test print
        }

        // Make enter key
        Button enter = new Button("ENTER");
        enter.setOnMouseClicked(arg0 -> {
            try {
                setTyped(arg0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        // Make backspace key
        Button backspace = new Button("BACK_SPACE");
        backspace.setOnMouseClicked(arg0 -> {
            try {
                setTyped(arg0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        for (int i = 0; i < 10; i++) {
            topKeysRow.getChildren().add(keyLetters.get(i));
        }
        midKeysRow.getChildren().add(backspace);
        for (int i = 10; i < 19; i++) {
            midKeysRow.getChildren().add(keyLetters.get(i));
        }
        midKeysRow.getChildren().add(enter);
        for (int i = 19; i < 26; i++) {
            bottomKeysRow.getChildren().add(keyLetters.get(i));
        }

        VBox keyboardHolder = new VBox(3);
        keyboardHolder.getChildren().addAll(topKeysRow, midKeysRow, bottomKeysRow);

        return keyboardHolder;
    }

    public void updateLetters() {

    }

    /**
     * Sets the String typed via virtual keys and connects with the grid through
     * typeLetter() method
     * 
     * @param e the button that was clicked
     * @throws FileNotFoundException
     */
    private void setTyped(MouseEvent e) throws FileNotFoundException {
        String typedLetter = ((Button) e.getSource()).getText();
        typed = typedLetter;
        typeLetter(typed);
    }

    /**
     * Disables the key of the letter that the user typed but was not in the answer
     */
    private void disableKeys() {
        for (Entry<Integer, Button> entry : this.keyLetters.entrySet()) {
            Button button = entry.getValue();
            String buttonText = button.getText();

            if (this.disabledKeys.contains(buttonText)) {
                button.setStyle("-fx-background-color: #c9d4d6;");
                button.setDisable(true);
            }
        }
    }
    
    public void enableKeys() {
    	for (Entry<Integer, Button> entry : this.keyLetters.entrySet()) {
            Button button = entry.getValue();
            String buttonText = button.getText();

            if (this.disabledKeys.contains(buttonText)) {
                button.setStyle("-fx-background-color: #7f9094; ");
                button.setDisable(false);
            }
        }
    	disabledKeys.removeAll(disabledKeys);
    }

    public void toggleKeyboard() {
        disabled = !disabled;
    }

    /**
     * Types the letter into the grid by sending to LevelSelector object Run the
     * logic based on the key typed: display letters, delete letters, play the
     * backend logic when ENTER is typed, and show pop-up based on the length of the
     * user input
     */
    public void typeLetter(String typed) {
        if (disabled == false) {
            // If the input is the letter
            if (isLetter(typed)) {
                if (attempt.size() >= 5) {
                    PopupOK popup = new PopupOK();
                    popup.popupDialog(new Text("You cannot enter more than 5 letters."));
                } else {
                    attempt.add(typed);
                    System.out.println("updating attempt: " + attempt.toString());

                    letterGrid.typeLetter(typed);
                }

            } else if (typed.equals("BACK_SPACE")) {
                if (!attempt.isEmpty()) {
                    attempt.remove(attempt.size() - 1);
                    letterGrid.deleteLetter();
                }

            } else if (typed.equals("ENTER")) {
                if (attempt.size() == 5) {
                    // Set user input
                    letterGrid.setUserInput(attempt);
                    letterGrid.play(roundCount);

                    // Disable the keys of the letters that are wrong
                    for (String attemptedKey : this.attempt) {
                        if (!this.answer.contains(attemptedKey.toLowerCase())) {
                            this.disabledKeys.add(attemptedKey);
                            disableKeys();
                        }
                    }

                    // Empty the list that stores the user input
                    attempt = new ArrayList<String>();
                    // Increment the current row
                    roundCount++;

                    if (letterGrid.checkIsCorrect() || letterGrid.checkGridFull() && disabled == false) {
                        toggleKeyboard();
                    }
                    

                    letterGrid.enterGuess();
                }
                // If the user typed less than 5 letters
                else {
                    PopupOK popup = new PopupOK();
                    popup.popupDialog(new Text("You must enter 5 letters."));
                }
            }
        }
    }

    }

