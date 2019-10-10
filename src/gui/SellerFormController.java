package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
		this.departmentService = dep; // professor colocou departmentService ao invés de dep
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
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners)
			listener.onDataChanged();
	}

	private Seller getFormData() { // método pega os dados que foram preenchidos no formulario e carrega um objet com esses dados, retornanro no fim
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation error");
		obj.setId(Utils.tryParseToInt(txtId.getText())); //pega o campo id para o campo ID do objeto, já convertido para inteiro em Utils

		if (txtName.getText() == null || txtName.getText().trim().equals("")) { // testa se o camo nome eh nulo ou vazio antes de ir para o obj
//			 verifica se o campo name esta vazio,	// caso seja true ele add um erro e
//			 repassa a super classe
//			compara o get text (nome) se é nulo e get text (nome) usando o trim para eliminar espaço em branco, sendo que se for igual comparando com .equals "" (vazio)
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) { //testa se o camo email eh nulo ou vazio antes de ir para o obj
			exception.addError("email", "Field can't be empty");
		}
		obj.setEmail(txtEmail.getText());
		
		if(dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty");
		}
		else {
			
		Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
		//converte o valor da hora local setado n PC do usuário, neste caso DatePicker, para uma variavel instnt independente de local
		obj.setBirthDate(Date.from(instant)); //seta em obj a data, antes formatando para DATE
		}
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "Field can't be empty");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));// pega o campo saláro e seta no baseSalary, isso já convertido em Utils		
		
		obj.setDepartment(comboBoxDepartment.getValue());		
		
		if (exception.getErrors().size() > 0) { // verifica se existem erros na coleção de exceção e se tiver ele reorna
												// a exceção personalizada, caso contrário, prossegue com o método
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

	private void initializeNodes() { // inicializa o formulário, atrelando os campos da tabela de vendedores
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
		// nesta inicialização, aqui ele chama o comboBox da lista de departamenrtos
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		// seta no campo ID o ID vindo do Seller, método criado e
		// instanciado no começo da classe com entity
		txtName.setText(entity.getName());// seta no campo name o valor buscado em entity de Seller
		txtEmail.setText(entity.getEmail());// campod e texto, seta o update como email String
		Locale.setDefault(Locale.US); // garantir que a formatação de baixo fique com .
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));// converte para String o salario em double
		if (entity.getBirthDate() != null) { // para não dar erro na conversão, verifica se não é nulo a getBirthDate
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));	
			// o BD usa o LocalDate		// por isso precisa colocar o getBirthDate dentro de uma formatação especifica
			// desta classe LocalDate, que usa
			// ZoneId.systemDefault() para setar a data local da maquina do cliente
		}
	
		if (entity.getDepartment() == null) { // teste se od epartamento criado está nulo
			comboBoxDepartment.getSelectionModel().selectFirst(); // se estiver ele pega o prmeiro campo ou parametro do combobox
		}
		else {
		comboBoxDepartment.setValue(entity.getDepartment()); // caso não esteja nulo, aí sim ele busca o deparatmento inteiro e passa para o combobox
		}
	}

	public void loadAssociatedObjects() { // método criado para chamar a criação da lista de departamentos desta classe, através do comoBox que precisa do ObservableList
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null!!");
		}
		List<Department> list = departmentService.findAll(); // recebendo a lista dentro deste parametro
		obsList = FXCollections.observableArrayList(list); // parametro padrão do combobox e ObservableList
		comboBoxDepartment.setItems(obsList);// add a lista através do ObservableList dentro do comoBox detando os itens
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		
//		if (fields.contains("name")) {
//			labelErrorName.setText(errors.get("name"));
//		}
//		else {
//			labelErrorName.setText("");
//		}
		
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		
//		if (fields.contains("email")) {
//			labelErrorEmail.setText(errors.get("email"));
//		}
//		else {
//		labelErrorEmail.setText("");

		labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));
		
//		if (fields.contains("baseSalary")) {
//			labelErrorBaseSalary.setText(errors.get("baseSalary"));
//		}
//		else {
//		labelErrorBaseSalary.setText("");
		
		labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));
//		
//		if (fields.contains("birthDate")) {
//			labelErrorBirthDate.setText(errors.get("birthDate"));
//		}
//		else {
//		labelErrorBirthDate.setText("");
//	}
	}
	
	private void initializeComboBoxDepartment() {// inicializa o comboBox /////
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
