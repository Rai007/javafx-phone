package com.controller;

import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.controller.Main.port;

public class Controller implements Initializable {
    @FXML
    private TextArea phonenumber;
    @FXML
    private Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,call,backspace;

    @FXML
    private TextField portField;

    @FXML
    public void btnClick(ActionEvent actionEvent) {
        String oldStr = phonenumber.getText();
        Button event = (Button) actionEvent.getTarget();
        String id = event.getId();
        if(id.contains("btn")) {
            String newStr = oldStr + id.substring(3);
            phonenumber.setText(newStr);
        }
        if(id.equalsIgnoreCase("call")){
            try {
                ObservableList<Stage> stage = FXRobotHelper.getStages();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/holding.fxml"));
                Parent root = fxmlLoader.load();
                Holding holding = fxmlLoader.getController();
                holding.setTargetPort(Integer.parseInt(oldStr));
                holding.setText("Waiting for "+oldStr+" response...");
                holding.TCPConnect();
                Scene scene = new Scene(root);
                stage.get(0).setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (id.equalsIgnoreCase("backspace")){
            oldStr = oldStr.substring(0,oldStr.length()-1);
            phonenumber.setText(oldStr);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        portField.setText("Local server port is :"+ port);
    }
}
