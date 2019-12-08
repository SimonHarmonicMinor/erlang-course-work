package application;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.EventHandler;

// Класс перевода денег на счет
public class MoneyTransferController implements Initializable {

	private float _transferSumm;		// Сумма перевода

    @FXML
    private Label lbl_balance;
    
    @FXML
    private Button btn_OK;

    @FXML
    private Button btn_cancel;
    
    @FXML
    private TextField txt_transferSumm;

    @FXML
    private TextField txt_transferName;
	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btn_OK.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
            	TransferSumm();
            }
        });
        
        btn_cancel.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
            	CloseWindow();
            }
        });
        
        DrawClientInformation();
    }

    // Отобразить данные клиента
    private void DrawClientInformation()
    {
    	lbl_balance.setText(Float.toString(HomeController.GetBalance()));
    }
    
    // Установить сумму перевода
    public void SetTransferSumm(float newSumm)
    {
    	_transferSumm = newSumm;
    }
    
    // Перевести введенную сумму
    public void TransferSumm()
    {
    	String txtName = txt_transferName.getText();
    	String txtSumm = txt_transferSumm.getText();
    	
    	if (isNullOrEmpty(txtName) || isNullOrEmpty(txtSumm))
    	{
    		showErrorAlert();
    		return;
    	}

    	float trSumm = Float.parseFloat(txtSumm);
    	SetTransferSumm(trSumm);
    	
    	if (HomeController.GetBalance() < trSumm)
    	{
    		showErrorAlert();
    		return;
    	}
    	
    	HomeController.OperationWithBalance(-trSumm);
    	showSuccessAlert();
    }
    
    private boolean isNullOrEmpty(String str)
    {
    	return str == null || str.trim().length() == 0;
    }
    
    private void CloseWindow() 
    {
    	Stage stage = (Stage) btn_OK.getScene().getWindow();
    	stage.close();
    }
    
    // Показать диалог успешного перевода
    private void showSuccessAlert() 
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Successfully!");
 
        ButtonType ok = new ButtonType("OK");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ok);
        Optional<ButtonType> option = alert.showAndWait();
 
        if (option.get() == null) {
        	CloseWindow();
        } else if (option.get() == ok) {
        	CloseWindow();
        } else {
        	CloseWindow();
        }
    }
    
    // Показать диалог ошибки перевода
    private void showErrorAlert() 
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("None available!");
 
        alert.showAndWait();
    }
}
