package unlimitedwordle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Displays a pop up window with a specific message and buttons with Yes and No.
 */
public class PopupYesNo implements Popup{
	
	final Stage popup = new Stage();
	
	/**
	 * Display a pop-up window.  
	 * @param message the text to be displayed on pop-up window
	 */
	@Override
	public void popupDialog(Text message) {
		// Display a pop up window 
	    popup.initModality(Modality.APPLICATION_MODAL);
	    
	    Button yes = new Button("Yes");
	    yes.setStyle("-fx-background-color: #40CE0E;");
	    yes.setOnAction(this::buttonClickYes);
	    
	    Button no = new Button("No");
	    no.setStyle("-fx-background-color: #CE0E0E;");
	    no.setOnAction(this::buttonClickNo);
	    
	    HBox choiceHbox = new HBox(yes, no);
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
	 * Exit the program when YES is clicked.
	 * @param e Action Event
	 */
	public void buttonClickYes(ActionEvent e) {
		Platform.exit();
	}

	/**
	 * Close pop-up window when NO is clicked.
	 * @param e Action Event
	 */
	public void buttonClickNo(ActionEvent e) {
		popup.close();
	}
	
}
