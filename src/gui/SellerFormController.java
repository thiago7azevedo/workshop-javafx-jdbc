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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable{

	private Seller entity;
	
	private SellerService service;
	
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
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBirhtDate;
	@FXML
	private Label labelErrorBaseSalary;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
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
		}
		
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving projects", null, e.getMessage(), AlertType.ERROR);
		}
	}
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners)
			listener.onDataChanged();	
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}	
	
	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation Error!");		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) { // verifica se o campo name esta vazio, caso seja true ele add um erro e repassa a super classe
//compara o get text (nome) se é nulo e get text (nome) usando o trim para eliminar espaço em branco, sendo que se for igual comparando com .equals "" (vazio)
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if(exception.getErros().size() > 0) { // verifica se existem erros na coleção de exceção e se tiver ele reorna a exceção personalizada, caso contrário, prossegue com o método
											// e retrna o obj.
			throw exception;
		}
		
		return obj;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Enity was null!");
		}
		txtId.setText(String.valueOf(entity.getId())); // seta no campo ID o ID vindo do Seller, método criado e instanciado no começo da classe com entity
		txtName.setText(entity.getName());//seta no campo name o valor buscado em entity de Seller
		txtEmail.setText(entity.getEmail());// campod e texto, seta o update como email String
		Locale.setDefault(Locale.US); // garantir que a formatação de baixo fique com .
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));//converte para String o salario em double
		if(entity.getBirthDate() != null) { // para não dar erro na conversão, verifica se não é nulo a getBirthDate
		dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault())); //o BD usa LocalDate
		//por isso precisa colocar o getBirthDate dentro de uma formatação especifica desta classe LocalDate, que usa
		//ZoneId.systemDefault() para setar a data local da maquina do cliente
		}
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
	
	
}
