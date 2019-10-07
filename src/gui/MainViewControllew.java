package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewControllew implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	@FXML
	public void onMenuItemDepartmentAction() {
		System.out.println("onMenuItemDepartmentAction");
	}
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}
		
	@Override
	public void initialize(URL uri, ResourceBundle rb) { // o initializable implementado na classe, chama este método
	}

	private synchronized void loadView(String absolutName) { // cria uma função loadView para carregar a scena principal pasando string FXML
	//	synchronized garante que todo o processamento não seja interrompido antes do fim ???
		try {
											// get class padrão + origem absoluteName
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName)); // pra carregar tela chama FXMLLoader
			VBox newVBox = loader.load();//objeto tipo Vbox e recebe o loader.load pra carregar a view, objeto criado para leitura
			
			Scene mainScene = Main.getMainScene(); // instancia a sceena na variavel mainScene e chama o método criado no program principal
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent(); //cria um objeto vBox, pegando a scena do MainView
											// get root = primeiro ato do main view que é um scrollpane e depois busca o segundo que é content
			//depois de buscar o primeiro e segundo do main view, faz um casting de scrollpane e um casting de tudo de VBox
			Node mainMenu = mainVBox.getChildren().get(0);//cria um objeto Node que são as ramificações do vBox
			//buscando a children ou crianças o conteudo dentro do VBox e pegando o peimeiro filho do menu campo get 0
			mainVBox.getChildren().clear(); // depois de pegar o conteudo a cima, ele faz um clear para limpar o childrem do main view
			mainVBox.getChildren().add(mainMenu);//add novo children pego na about e colocado na varivel mainMenu
			mainVBox.getChildren().addAll(newVBox.getChildren()); //
		
			}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
			}
		}
	
}
