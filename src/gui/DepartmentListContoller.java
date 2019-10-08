package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListContoller implements Initializable, DataChangeListener {

	private DepartmentService service; // não instancia direto pelo new, mas sim cria uma injeção de dependencia através de
										// um método criado abvaixo com setDepartmentService.
	
	@FXML
	private TableView<Department> tableViewDepartment; // cria a tabela Department com o nome da TableView do SceneBuilder
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId; // cria a tabela Department com o seu respectivo nome ID
	
	@FXML
	private TableColumn<Department, Integer> tableColumnName; // cria a tabela Department com o seu respectivo nome NAME
	
	@FXML
	private Button btNew; // atributo para o botão criado na SceneBuilder
	
	private ObservableList<Department> obsList;
	
	
	@FXML 
	public void onBtNewAction(ActionEvent event) { // precisa passar o event para ter referencia, para poder acessar o stage
		Stage parentStage = Utils.currentStage(event); //passa o event nesse método que foi criado no de
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage); //abre o formulario DepartmentForm.fxml e diz qual janela abrir
	}
	
	public void setDepartmentService(DepartmentService service) { //deixa pronto um método para acesso externo, injeção de dependencia
		this.service = service;									//principio solid, inversão de controle
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes(); // chama o método criado abaixo
	
	}

	private void initializeNodes() { // padrão do JAVA FX para inicar o comportamento das colunas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); // passa o nome da coluna que é "id"
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name")); // passa o nome da coluna que é "name"
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); // cria um obj Stage, que busca a scena no método getMainScene()
		// depois busca a janela no getWindow() que é uma superclasse de Stage
		// depois faz um DownCasing colocando Stage na frente
		
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());// macete para o tableView acompanhar a janela da scena
		//passando o stage criado a cima chamado a getWindow, e sua heightProperty.
		
	}

	public void updateTableView() { // responsavel por acessar o serviço, carregar os departamentos e jogar na obsList
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); //lista de department lits e pega todos os dados feitos na classe DepartmentSericve
		obsList = FXCollections.observableArrayList(list); //carrego a lista criada dentro da obsList. Instancia o obsLIst pegando os dados originais da lista
		tableViewDepartment.setItems(obsList);
	}
								// tem que falarqual nome da view que vai carregar
	private void createDialogForm(Department obj, String absolutName, Stage parentStage) {
		try {								
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			Pane pane = loader.load();// chama painel carregando o load
			// para carregar uma janela de dialogo modal na frente da jenela existente, precisa instanciar um novo stage
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subscrbeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage(); // cria um palco na frente do outro
			dialogStage.setTitle("Enter Department Data"); // titulo da janela
			dialogStage.setScene(new Scene(pane)); //seta a scena do stage, nova scena onde o elemnto raiz é o pane
			dialogStage.setResizable(false);//janela não pode ser redimensionada
			dialogStage.initOwner(parentStage); //o stage pai da janela é o parentStage
			dialogStage.initModality(Modality.WINDOW_MODAL); //método que diz se vai ser modal ou outro comportamento 
			// vai ficar travada, enquanto nao fechar ela nao acessa a enterior
			dialogStage.showAndWait();//executa a função pra carreghar a janela do formulario e preencher o departamento
			
			
		}
		catch (IOException e){
			Alerts.showAlert("IO Exception", "Error loading error", e.getMessage(), AlertType.ERROR);
		}
	}

		@Override
	public void onDataChanged() {
		updateTableView();
	}
	
}
