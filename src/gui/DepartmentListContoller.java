package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListContoller implements Initializable {

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
	public void onBtNewAction() {
		System.out.println("onBtNewAction!"); // a ação que acontece ao apertar o botão no SceneBuilder
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
	
}
