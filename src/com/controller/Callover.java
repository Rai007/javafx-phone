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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Callover implements Initializable{
    private String targetPort;

    public void setTargetPort(String targetPort) {
        this.targetPort = targetPort;
    }

    @FXML
    public ImageView calloverimg;
    @FXML
    public TextField calloverinfo;
    @FXML
    public Button redial;
    @FXML
    public Button back;

    public void setText(String text){
        calloverinfo.setText(text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calloverimg.setImage(new Image("file:///C:\\Users\\hahah\\IdeaProjects\\ip-phone\\image\\hang_up.png"));
    }
    @FXML
    public void reDial(ActionEvent actionEvent) {
        try {
            ObservableList<Stage> stage = FXRobotHelper.getStages();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/holding.fxml"));
            Parent root = fxmlLoader.load();
            Holding holding = fxmlLoader.getController();
            holding.setTargetPort(Integer.parseInt(targetPort));
            holding.setText("Waiting for "+targetPort+" response...");
            holding.TCPConnect();
            Scene scene = new Scene(root);
            stage.get(0).setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void back(ActionEvent actionEvent) {
        try {
            ObservableList<Stage> stage = FXRobotHelper.getStages();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/dial.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.get(0).setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
