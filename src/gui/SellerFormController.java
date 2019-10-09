package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;

	private SellerService service;

	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Department> obsList;

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setServices(SellerService service, DepartmentService dep) {
		this.service = service;
		this.departmentService = dep; // professor colocou departmentService ao inv�s de dep
	}

	public void subscrbeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners)
			listener.onDataChanged();
	}

	private Seller getFormData() {
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation error");
		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
//			 verifica se o campo name esta vazio,	// caso seja true ele add um erro e
//			 repassa a super classe
//			compara o get text (nome) se � nulo e get text (nome) usando o trim para eliminar espa�o em branco, sendo que se for igual comparando com .equals "" (vazio)
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());

		if (exception.getErros().size() > 0) { // verifica se existem erros na cole��o de exce��o e se tiver ele reorna
												// a exce��o personalizada, caso contr�rio, prossegue com o m�todo
												// e retrna o obj.
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() { // inicializa o formul�rio, atrelando os campos da tabela de vendedores
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
		// nesta inicializa��o, aqui ele chama o comboBox da lista de departamenrtos
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		// seta no campo ID o ID vindo do Seller, m�todo criado e
		// instanciado no come�o da classe com entity
		txtName.setText(entity.getName());// seta no campo name o valor buscado em entity de Seller
		txtEmail.setText(entity.getEmail());// campod e texto, seta o update como email String
		Locale.setDefault(Locale.US); // garantir que a formata��o de baixo fique com .
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));// converte para String o salario em double
		if (entity.getBirthDate() != null) { // para n�o dar erro na convers�o, verifica se n�o � nulo a getBirthDate
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));	
			// o BD usa o LocalDate		// por isso precisa colocar o getBirthDate dentro de uma formata��o especifica
			// desta classe LocalDate, que usa
			// ZoneId.systemDefault() para setar a data local da maquina do cliente
		}
//		if (entity.getDepartment() == null) { // teste se od epartamento criado est� nulo!
//			
//			comboBoxDepartment.getSelectionModel().selectFirst();// se estiver ele pega o prmeiro campo ou parametro do combobox
//		}
//		else {
//		comboBoxDepartment.setValue(entity.getDepartment()); // caso n�o esteja nuo, a� sim ele busca o deparatmento inteiro e passa para o combobox
//		}
//	}
//
//	public void loadAssociatedObjects() { // m�todo criado para chamar a cria��o da lista de departamentos criada nesta
//											// classe, atrav�s do comoBox que precisa do ObservableList
//		if (departmentService == null) {
//			throw new IllegalStateException("DepartmentService was null");
//		}
//		List<Department> list = departmentService.findAll(); // recebendo a lista dentro deste parametro
//																// FXCollections.observableArrayList(list)
//		obsList = FXCollections.observableArrayList(list);// parametro padr�o do combobox e ObservableList
//		comboBoxDepartment.setItems(obsList);// add a lista atrav�s do ObservableList dentro do comoBox detando os itens
		
		if (entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else {
		comboBoxDepartment.setValue(entity.getDepartment());
		}
	}

	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null!!");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}

//	private void initializeComboBoxDepartment() { // inicializa o comboBox /////////////###########################################
//		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
//			@Override
//			protected void updateItem(Department item, boolean empty) {
//				super.updateItem(item, empty);
//				setText(empty ? "" : item.getName());
//			}
//		};
//		comboBoxDepartment.setCellFactory(factory);
//		comboBoxDepartment.setButtonCell(factory.call(null));
//	}