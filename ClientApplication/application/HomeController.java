package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.stage.Modality;

public class HomeController implements Initializable {
    
	private static float _balance;		// Баланс счета
	private float _USD;					// Курс доллара
	private float _EUR;					// Курс евро
	
	private static float _coming;		// Приход
	private static float _consuption;	// Расход
	
    @FXML
    private Label label;
    
    @FXML
    private Label lbl_balance;
    
    @FXML
    private Label lbl_EUR;
    
    @FXML
    private Label lbl_USD;
    
    @FXML
    private Label lbl_consuption;
    
    //@FXML
    //private VBox pnl_scroll;
     
    @FXML
    private BarChart bar_chart;
       
    @FXML
    private StackedAreaChart area_chart;
    
    @FXML
    private Button btn_mainTab;

    @FXML
    private Button btn_transferTab;
    
    @FXML
    private Button btn_operationsTab;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        btn_transferTab.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
					FXMLDocumentController(stage, "MoneyTransfer.fxml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btn_operationsTab.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
					FXMLDocumentController(stage, "MoneyOperations.fxml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        DefaultClientInformation();
        SubClientInformation();
        DrawClientInformation();
        
        plotBarChart();
        plotChart();
    }
    
    // Открытие нового окна
    protected void FXMLDocumentController(Stage stage, String formName) throws IOException 
    {
        Parent root = FXMLLoader.load(getClass().getResource(formName));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    // Дoполнительные данные клиента
    private void SubClientInformation()
    {
    	_coming = 0.00f;
    	_consuption = 0.00f;
    }
    
    // Дефолтные данные клиента (для тестирования GUI)
    private void DefaultClientInformation()
    {
    	SetBalance(999.00f);
    	SetUSD(63.67f);
    	SetEUR(70.41f);
    }
    
    // Получить баланс клиента
    public static float GetBalance()
    {
    	return _balance;
    }
    
    // Установить баланс клиента
    public static void SetBalance(float newBalance)
    {
    	_balance = newBalance;
    }

    // Операции с балансом: снять/перевести/пополнить
    public static void OperationWithBalance(float operationCount)
    {
    	float balance = GetBalance();
    	balance = balance + operationCount;
    	SetBalance(balance);
    	
    	if (operationCount < 0)
    		_consuption = _consuption + operationCount * (-1);
    	else if (operationCount >= 0)
    		_coming = _coming + operationCount;
    }
    
    // Установить курс доллара
    public void SetUSD(float newUSD)
    {
    	_USD = newUSD;
    }

    // Установить курс евро
    public void SetEUR(float newEUR)
    {
    	_EUR = newEUR;
    }
    
    // Отобразить данные клиента
    private void DrawClientInformation()
    {
    	lbl_balance.setText(Float.toString(_balance));
    	lbl_consuption.setText(Float.toString(_consuption));
    	lbl_USD.setText(Float.toString(_USD));
    	lbl_EUR.setText(Float.toString(_EUR));
    }
    
    // График движения курса валют (для понтов, пацаны)
    private void plotChart()
    {
        XYChart.Series seriesEUR= new XYChart.Series();
        seriesEUR.setName("EUR");
        seriesEUR.getData().add(new XYChart.Data(1, 4));
        seriesEUR.getData().add(new XYChart.Data(3, 10));
        seriesEUR.getData().add(new XYChart.Data(6, 15));
        seriesEUR.getData().add(new XYChart.Data(9, 8));
        seriesEUR.getData().add(new XYChart.Data(12, 5));
        seriesEUR.getData().add(new XYChart.Data(15, 18));
        seriesEUR.getData().add(new XYChart.Data(18, 15));
        seriesEUR.getData().add(new XYChart.Data(21, 13));
        seriesEUR.getData().add(new XYChart.Data(24, 19));
        seriesEUR.getData().add(new XYChart.Data(27, 70));
        seriesEUR.getData().add(new XYChart.Data(30, _EUR));
        
        XYChart.Series seriesUSD = new XYChart.Series();
        seriesUSD.setName("USD");
        seriesUSD.getData().add(new XYChart.Data(1, 20));
        seriesUSD.getData().add(new XYChart.Data(3, 15));
        seriesUSD.getData().add(new XYChart.Data(6, 13));
        seriesUSD.getData().add(new XYChart.Data(9, 12));
        seriesUSD.getData().add(new XYChart.Data(12, 14));
        seriesUSD.getData().add(new XYChart.Data(15, 18));
        seriesUSD.getData().add(new XYChart.Data(18, 25));
        seriesUSD.getData().add(new XYChart.Data(21, 25));
        seriesUSD.getData().add(new XYChart.Data(24, 23));
        seriesUSD.getData().add(new XYChart.Data(27, 26));
        seriesUSD.getData().add(new XYChart.Data(30, _USD));
        
        area_chart.getData().addAll(seriesEUR, seriesUSD);
    }
    
    // График динамики счета клиента (и сюда для понтов)
    private void plotBarChart()
    {
        String august = "August";
        String september = "September";
        String october = "October";
        String november = "November";
        String december = "December";
        
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("consuption");
        series1.getData().add(new XYChart.Data(august, 57401.85));
        series1.getData().add(new XYChart.Data(september, 41941.19));
        series1.getData().add(new XYChart.Data(october, 45263.37));
        series1.getData().add(new XYChart.Data(november, 117320.16));
        series1.getData().add(new XYChart.Data(december, _consuption));  
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("coming");
        series2.getData().add(new XYChart.Data(august, 25601.34));
        series2.getData().add(new XYChart.Data(september, 20148.82));
        series2.getData().add(new XYChart.Data(october, 10000));
        series2.getData().add(new XYChart.Data(november, 35407.15));
        series2.getData().add(new XYChart.Data(december, _coming));      
        
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("balance");
        series3.getData().add(new XYChart.Data(august, 45000.65));
        series3.getData().add(new XYChart.Data(september, 44835.76));
        series3.getData().add(new XYChart.Data(october, 18722.18));
        series3.getData().add(new XYChart.Data(november, 17557.31));
        series3.getData().add(new XYChart.Data(december, _balance));  
        
        bar_chart.getData().addAll(series1, series2, series3);
    }
    
}
