package com.controller;

import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.application.Platform;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class CallComing implements Initializable{

    private Socket socket;
    private String sourcePort;

    void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    void setSocket(Socket socket) {
        this.socket = socket;
    }

    @FXML
    public ImageView callcomingimg;
    @FXML
    public TextField callcominginfo;
    @FXML
    public Button rejectcall;
    @FXML
    public Button acceptcall;
    @FXML
    public void rejectCall(ActionEvent actionEvent) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("close");
            oos.close();
//            socket.close();
           callover(sourcePort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void acceptCall(ActionEvent actionEvent) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("accept");
            ObservableList<Stage> stage = FXRobotHelper.getStages();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/acceptCall.fxml"));
            Parent root = fxmlLoader.load();
            AcceptCall acceptCall = fxmlLoader.getController();
            acceptCall.startUDPServer();
            acceptCall.setSocket(socket);
            acceptCall.setTargetPort(sourcePort);
            Scene scene = new Scene(root);
            stage.get(0).setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setTextField(String text){
        callcominginfo.setText(text);
    }

    private void callover(String targetPort){
        try {
            ObservableList<Stage> stage = FXRobotHelper.getStages();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/callover.fxml"));
            Parent root = fxmlLoader.load();
            Callover callover = fxmlLoader.getController();
            callover.setText("The call to "+targetPort+" ends.");
            callover.setTargetPort(targetPort+"");
            Scene scene = new Scene(root);
            stage.get(0).setScene(scene);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        callcomingimg.setImage(new Image("file:///C:\\Users\\hahah\\IdeaProjects\\ip-phone\\image\\callcoming.png"));
        new Thread(() -> {
            try {
                while (true){
                    if(!socket.isClosed()){
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        String res = (String) ois.readObject();
                        if(res.equalsIgnoreCase("close")){
                            ois.close();
                            Platform.runLater(() -> callover(sourcePort));
                            break;
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
