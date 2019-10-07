package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene mainScene;	//expondo a referencia da scena pelo atributo, para guardar a referencia
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			
			scrollPane.setFitToHeight(true); //a barra do scrollpane acompanha a largura da teabela no height
			scrollPane.setFitToWidth(true); //a barra do scrollpane acompanha a largura da teabela no widht
						
			mainScene = new Scene(scrollPane); // criamos a referenca da scena 
			primaryStage.setScene(mainScene);// colocamos a scena no palco
			primaryStage.setTitle("Sample JavaFX application");
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Scene getMainScene() {
		return mainScene; // cria um método para retornar o mainScene, pq o atributo é private
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
