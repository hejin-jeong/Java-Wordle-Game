package unlimitedwordle;

import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * An interface for pop-up windows.
 */
public interface Popup {
	
	final Stage popup = new Stage();
	
	/**
	 * Display a pop-up window.  
	 * @param message the text to be displayed on pop-up window
	 */
	public void popupDialog(Text message);
	
}
