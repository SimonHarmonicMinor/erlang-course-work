package application;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.EventHandler;

// Класс перевода денег на счет
public class MoneyOperationsController implements Initializable {

	private float _takeSumm;	// Сумма снятия
	private float _putSumm;		// Сумма пополнения

    @FXML
    private Label lbl_balance;
    
    @FXML
    private Button btn_take;
	
    @FXML
    private Button btn_put;

    @FXML
    private Button btn_cancel;
    
    @FXML
    private Button btn_mainTab;

    @FXML
    private Button btn_transferTab;
    
    @FXML
    private Button btn_operationsTab;
    
    @FXML
    private TextField txt_transferSumm;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btn_mainTab.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
					FXMLDocumentController(stage, "Home.fxml");
					CloseWindowSimple();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btn_transferTab.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
					FXMLDocumentController(stage, "MoneyTransfer.fxml");
					CloseWindowSimple();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        btn_take.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
            	TakeSumm();
            }
        });

        btn_put.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
            	PutSumm();
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
    
    // Установить сумму снятия
    public void SetTakeSumm(float newSumm)
    {
    	_takeSumm = newSumm;
    }

    // Установить сумму пополнения
    public void SetPutSumm(float newSumm)
    {
    	_putSumm = newSumm;
    }
    
    // Снять введенную сумму
    public void TakeSumm()
    {
    	String txtSumm = txt_transferSumm.getText();
    	
    	if (isNullOrEmpty(txtSumm))
    	{
    		showErrorAlert();
    		return;
    	}

    	float trSumm = Float.parseFloat(txtSumm);
    	SetTakeSumm(trSumm);
    	
    	if (HomeController.GetBalance() < trSumm)
    	{
    		showErrorAlert();
    		return;
    	}
    	
    	HomeController.OperationWithBalance(-trSumm);
    	showSuccessAlert();
    }
    
    // Внести введенную сумму
    public void PutSumm()
    {
    	String txtSumm = txt_transferSumm.getText();
    	
    	if (isNullOrEmpty(txtSumm))
    	{
    		showErrorAlert();
    		return;
    	}

    	float trSumm = Float.parseFloat(txtSumm);
    	SetPutSumm(trSumm);
    	
    	HomeController.OperationWithBalance(trSumm);
    	showSuccessAlert();
    }
    
    private boolean isNullOrEmpty(String str)
    {
    	return str == null || str.trim().length() == 0;
    }
    
    private void CloseWindowSimple() 
    {
    	Stage stage = (Stage) btn_take.getScene().getWindow();
    	stage.close();
    }

    private void CloseWindow() 
    {
    	Stage stageNew = new Stage();
        try {
			FXMLDocumentController(stageNew, "Home.fxml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Stage stage = (Stage) btn_take.getScene().getWindow();
    	stage.close();
    }
    
    // Показать диалог успешной операции
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
    
    // Показать диалог ошибки
    private void showErrorAlert() 
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("None available!");
 
        alert.showAndWait();
    }

    // Открытие нового окна
    protected void FXMLDocumentController(Stage stage, String formName) throws IOException 
    {
        Parent root = FXMLLoader.load(getClass().getResource(formName));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
