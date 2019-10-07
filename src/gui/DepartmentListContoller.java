package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListContoller implements Initializable {

	@FXML
	private TableView<Department> tableViewDepartment; // cria a tabela Department com o nome da TableView do SceneBuilder
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId; // cria a tabela Department com o seu respectivo nome ID
	
	@FXML
	private TableColumn<Department, Integer> tableColumnName; // cria a tabela Department com o seu respectivo nome NAME
	
	@FXML
	private Button btNew; // atributo para o botão criado na SceneBuilder
	
	@FXML 
	public void onBtNewAction() {
		System.out.println("onBtNewAction!"); // a ação que acontece ao apertar o botão no SceneBuilder
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

}
