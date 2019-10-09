package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service; // n�o instancia direto pelo new, mas sim cria uma inje��o de dependencia
										// atrav�s de
										// um m�todo criado abvaixo com setSellerService.

	@FXML
	private TableView<Seller> tableViewSeller; // cria a tabela Seller com o nome da TableView do
														// SceneBuilder

	@FXML
	private TableColumn<Seller, Integer> tableColumnId; // cria a tabela Seller com o seu respectivo nome ID

	@FXML
	private TableColumn<Seller, String> tableColumnName; // cria a tabela Seller com o seu respectivo nome NAME
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT; // chama m�todo que insere um bot�o de edi��o em cada
																	// row (linha)

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE; // chama um metodo que insere um bot�o de remover o
																	// departamento em cada row

	@FXML
	private Button btNew; // atributo para o bot�o criado na SceneBuilder

	private ObservableList<Seller> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) { // precisa passar o event para ter referencia, para poder acessar o
													// stage
		Stage parentStage = Utils.currentStage(event); // passa o event nesse m�todo que foi criado no de
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage); // abre o formulario SellerForm.fxml e diz
																		// qual janela abrir
	}

	public void setSellerService(SellerService service) { // deixa pronto um m�todo para acesso externo, inje��o
																	// de dependencia
		this.service = service; // principio solid, invers�o de controle
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes(); // chama o m�todo criado abaixo

	}

	private void initializeNodes() { // padr�o do JAVA FX para inicar o comportamento das colunas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); // passa o nome da coluna que � "id"
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name")); // passa o nome da coluna que � "name"
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email")); // ...
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));//...
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));//...
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); // cria um obj Stage, que busca a scena no m�todo
																// getMainScene()
		// depois busca a janela no getWindow() que � uma superclasse de Stage
		// depois faz um DownCasing colocando Stage na frente

		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());// macete para o tableView acompanhar a
																				// janela da scena
		// passando o stage criado a cima chamado a getWindow, e sua heightProperty.

	}

	public void updateTableView() { // responsavel por acessar o servi�o, carregar os departamentos e jogar na
									// obsList
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Seller> list = service.findAll(); // lista de department lits e pega todos os dados feitos na classe
													// SellerSericve
		obsList = FXCollections.observableArrayList(list); // carrego a lista criada dentro da obsList. Instancia o
															// obsLIst pegando os dados originais da lista
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	// tem que falarqual nome da view que vai carregar
	private void createDialogForm(Seller obj, String absolutName, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
//			Pane pane = loader.load();// chama painel carregando o load
//			// para carregar uma janela de dialogo modal na frente da jenela existente,
//			// precisa instanciar um novo stage
//
//			SellerFormController controller = loader.getController();
//			controller.setSeller(obj);
//			controller.setSellerService(new SellerService());
//			controller.subscrbeDataChangeListener(this);
//			controller.updateFormData();
//
//			Stage dialogStage = new Stage(); // cria um palco na frente do outro
//			dialogStage.setTitle("Enter Seller Data"); // titulo da janela
//			dialogStage.setScene(new Scene(pane)); // seta a scena do stage, nova scena onde o elemnto raiz � o pane
//			dialogStage.setResizable(false);// janela n�o pode ser redimensionada
//			dialogStage.initOwner(parentStage); // o stage pai da janela � o parentStage
//			dialogStage.initModality(Modality.WINDOW_MODAL); // m�todo que diz se vai ser modal ou outro comportamento
//			// vai ficar travada, enquanto nao fechar ela nao acessa a enterior
//			dialogStage.showAndWait();// executa a fun��o pra carreghar a janela do formulario e preencher o
//										// departamento
//
//		} catch (IOException e) {
//			Alerts.showAlert("IO Exception", "Error loading error", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?"); //cria uma confirma��o com titulo e pergunta
		//se de fato quer excluir o departamento
		//Optional � um objeto que carrega outro objeto dentro dele, estando presente ou n�o. Para chamar precisa do .get().
		if (result.get() == ButtonType.OK) {// confirma se o resultado de apertar o bot�o foi ok.
			if(service == null) { // verifica se o servi�o est� instanciado
				throw new IllegalStateException("Service was null");
			}
			try {// efeua a remo��o ou a�� que o m�todo solicita
			service.remove(obj);
			updateTableView();// por fim atualiza os dados d tabela no BD.
		}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing Object", null, e.getMessage(), AlertType.ERROR);
			}
	}

}
}