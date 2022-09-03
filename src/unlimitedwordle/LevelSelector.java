package unlimitedwordle;

import java.util.List;

import javafx.scene.layout.GridPane;

/**
 * An interface for each level.
 */
public interface LevelSelector {
	
    public boolean checkIsCorrect();
    
    public boolean checkGridFull();
    
    public int getRoundCount();
    
    public int getScore();

	public void setSolutionLabelEmpty();
    
    
	/**
     * Set user input 
     * Receives information from keyboard class. 
     */
    public void setUserInput(List<String> attempt);


    /**
	 * Creates letter grid based on nrows and ncols. 
	 */
	public GridPane createGrid();

	
	/**
     * Compares the user input with the answer and display appropriate  color on the grid. 
     */
    public void play(int roundCount);
    
    
    public void typeLetter(String letter);

    public void deleteLetter();

    public void enterGuess();


}