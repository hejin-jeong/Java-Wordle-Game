package unlimitedwordle;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Displays a pop up window with a specific message and an OK button.
 */
public class PopupOK implements Popup{
	
	final Stage popup = new Stage();
	
	/**
	 * Display a pop-up window.  
	 * @param message the text to be displayed on pop-up window
	 */
	public void popupDialog(Text message) {
		// Display a pop up window 
	    popup.initModality(Modality.APPLICATION_MODAL);
	       
	    Button OK = new Button("OK");
	    OK.setStyle("-fx-background-color: #40CE0E;");
	    OK.setOnMouseClicked(this::buttonClickOK);

	    HBox choiceHbox = new HBox(OK);
	    choiceHbox.setAlignment(Pos.CENTER);
	    choiceHbox.setSpacing(20);
	    
	    VBox popupVbox = new VBox(20);
	    popupVbox.getChildren().addAll(message, choiceHbox);
	    popupVbox.setAlignment(Pos.CENTER);
	    
	    Scene popupScene = new Scene(popupVbox, 300, 200);
	    popup.setScene(popupScene);
	    popup.show();
	}
	
	/**
	 * Close pop-up window when OK is clicked.
	 * @param e Action Event
	 */
	public void buttonClickOK(MouseEvent e) {
		popup.close();	
		
	}

}
