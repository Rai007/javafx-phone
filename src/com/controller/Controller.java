package com.controller;

import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    private Main app;

    public Main getApp() {
        return app;
    }

    public void setApp(Main app) {
        this.app = app;
    }

    @FXML
    private TextArea phonenumber;
    @FXML
    private Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,call,hang_up;

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
            ObservableList<Stage> stage = FXRobotHelper.getStages();
            Scene scene = null;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/holding.fxml"));
                Parent root = fxmlLoader.load();
                Holding holding = fxmlLoader.getController();
                holding.getTextField().setText("Waiting for "+oldStr+" accepting...");
                scene = new Scene(root);
                stage.get(0).setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
