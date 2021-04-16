/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.gson.Gson;
import dice.Route;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import static app.Program.socket;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Maycon
 */
public class RegisterController implements Initializable {
    
    @FXML
    private TextField tfName;
    
    @FXML
    private TextField tfUser;

    @FXML
    private TextField tfPass;
    
    @FXML
    private Button btnRegister;
        
    @FXML
    @SuppressWarnings("unchecked")
    private void hundleRegistrar(ActionEvent event) throws IOException{
        try {
            // Read from interface
            String name = tfName.getText();
            String user = tfUser.getText();
            String pass = tfPass.getText();

            JSONObject route = new JSONObject();
            route.put("rota", "login.registro");
            route.put("nome", name);
            route.put("usuario", user);
            route.put("senha", pass);
            route.put("is_admin", false);
            route.put("is_monitor", false);
            
            File fLog = new File("log.txt");
            FileWriter fwLog = new FileWriter(fLog, true);

            // Shows what will be sent
            showSend(route.toString());
            // Send
            PrintWriter pr = new PrintWriter(socket.getOutputStream());  
            pr.println(route);
            pr.flush();

            // received string
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            BufferedReader bf = new BufferedReader(in);
            //Read String
            String sRoute = bf.readLine();
            //Shows what came
            showRecive(sRoute);
            
            // Convert Json String to Route Object
            Gson gson = new Gson(); 
            Route rRoute = gson.fromJson(sRoute, Route.class);
            

            Pane root;
            
            if("false".equals(rRoute.getErro())){
                root = FXMLLoader.load(getClass().getResource("../Screens/Login.fxml"));
            }else{
                root = FXMLLoader.load(getClass().getResource("../Screens/Register.fxml"));
            }
            
            /*
            //Valid 
            if(){
                
                
            }else{
                
            }
            */
            
            // Opens screen
            Stage primaryStage = new Stage();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show(); 
            
            // Close Screen
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.close();

            // Close Connection Serve
            //s.close();
            
        }catch(Error e){
            
        }
    }
    
        // Shows what will be sent
    private void showSend(String route) throws IOException{
        //Terminal
        System.out.println("Send -> "+route);
        
        //Interface
        StackPane pLog = new StackPane();
        Label lLog = new Label();
        lLog.setText("Send -> "+route);
        pLog.getChildren().add(lLog);
        pLog.getStyleClass().add("box-log");
        //logs.getChildren().add(pLog);
        
        //Txt   
        File fLog = new File("log.txt");
        FileWriter fwLog = new FileWriter(fLog, true); 
        fwLog.write("Send -> "+route+"\n");
        fwLog.flush();
        fwLog.close();
    }
    
    //Shows what came
    private void showRecive(String received) throws IOException{
        File fLog = new File("log.txt");
        FileWriter fwLog = new FileWriter(fLog, true); 
        System.out.println("Received <- "+received);
        StackPane pLog = new StackPane();
        Label lLog = new Label();
        lLog.setText("Received <- "+received);
        pLog.getChildren().add(lLog);
        pLog.getStyleClass().add("box-log");
        //logs.getChildren().add(pLog);
        fwLog = new FileWriter(fLog, true); 
        fwLog.write("Received <- "+received+"\n");
        fwLog.flush();
        fwLog.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
