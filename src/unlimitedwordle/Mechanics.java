
package unlimitedwordle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * The mechanics class determines the differences between a user attempt and the answer. 
 * The mechanics objects are created inside level selector objects (Easy, Medium, Hard).
 *  
 * @author Jennie, Hejin 
 */

public class Mechanics {

	// Tracks if the user input is the same as the answer. 
    private boolean isCorrect;

    
    // Constructor 
    public Mechanics() throws FileNotFoundException {
        this.isCorrect = false;
    }

    
    /**
     * Returns true if user input is correct, 
     * otherwise false.
     * @return isCorrect
     */
    public boolean getIsCorrect() {
        return isCorrect;
    }
    
    
    /**
     * Compares each character in the user input to each character in the answer, 
     * returns a list of values, where 1 means characters match, 
     * 0 means the character is present in answer but at wrong position,
     * -1 means the character is not present in answer. 
     * @param answer
     * @param userInput
     * @return checkList
     */
    public List<Integer> compareAnswer(String answer, String userInput) {
        
        List<Integer> checkList = new ArrayList<Integer>();

        final Integer correctValue = 1;
        final Integer differentPositionValue = 0;
        final Integer wrongValue = -1;

        for (int i = 0; i < userInput.length(); i++) {
            char currentCharacter = userInput.charAt(i);

            if (currentCharacter == answer.charAt(i)) {
                checkList.add(correctValue);
            } else {
                if (answer.indexOf(currentCharacter) != -1) {
                    checkList.add(differentPositionValue);
                } else {
                    checkList.add(wrongValue);
                }
            }
        }
        
        if (answer.equalsIgnoreCase(userInput)) {
            this.isCorrect = true;
            
        } else {
        	this.isCorrect = false;
        }

        return checkList;
    }    

}