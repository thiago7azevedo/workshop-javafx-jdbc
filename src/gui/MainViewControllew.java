package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewControllew implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
			
	}
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {}); // precisa passar a express�o lambda x -> {} vazia devido ao consumer<T> repassado na fun��o loadview.
	}
		
	@Override
	public void initialize(URL url, ResourceBundle rb) { // o initializable implementado na classe, chama este m�todo
	}
// fun��o loadView � gen�rica do tipo T. � utilizado aqui para n�o ter que criar duas fun��es loadview que fazem a mesma coisa. Consumer <T> parametriza com a fun��o de chamada loadview
	private synchronized <T> void loadView(String absolutName, Consumer<T> initializingAction) { // cria uma fun��o loadView para carregar a scena principal pasando string FXML
	//	synchronized garante que todo o processamento n�o seja interrompido antes do fim ???
		try {
											// get class padr�o + origem absoluteName
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName)); // pra carregar tela chama FXMLLoader
			VBox newVBox = loader.load();//objeto tipo Vbox e recebe o loader.load pra carregar a view, objeto criado para leitura
			
			Scene mainScene = Main.getMainScene(); // instancia a sceena na variavel mainScene e chama o m�todo criado no program principal
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent(); //cria um objeto vBox, pegando a scena do MainView
											// get root = primeiro ato do main view que � um scrollpane e depois busca o segundo que � content
			//depois de buscar o primeiro e segundo do main view, faz um casting de scrollpane e um casting de tudo de VBox
			Node mainMenu = mainVBox.getChildren().get(0);//cria um objeto Node que s�o as ramifica��es do vBox
			//buscando a children ou crian�as o conteudo dentro do VBox e pegando o peimeiro filho do menu campo get 0
			mainVBox.getChildren().clear(); // depois de pegar o conteudo a cima, ele faz um clear para limpar o childrem do main view
			mainVBox.getChildren().add(mainMenu);//add novo children pego na about e colocado na varivel mainMenu
			mainVBox.getChildren().addAll(newVBox.getChildren()); //
		
			T controller = loader.getController(); // retorna um DepartmentListController que � um tipo <T> gen�rico. 
			//o FXMLLoader que � responsavel por rodar a tela, tem a chamada do getController que � a chamada da fun��o passada como paramatreo a express�o lambda no loadview
			
			initializingAction.accept(controller); // executa a fun��o que passamos como argumento em onMenuItemDepartmentAction
			// utilizada aqui devido a fun��o loadview estar agora com o consumer<T> initializingAction.
			
			}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
			}
		}
}
