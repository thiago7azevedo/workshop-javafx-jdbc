package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) {
		return (Stage)((Node)event.getSource()).getScene().getWindow();
		//para o event da actionEvent pegar a source + scene + window, faz um casting de Node e um casting de tudo para Stage
	}
	
}
