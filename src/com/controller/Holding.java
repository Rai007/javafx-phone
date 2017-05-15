package com.controller;

import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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

import static com.controller.Main.port;

public class Holding implements Initializable{
    private int targetPort;

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    private Socket socket;

    @FXML
    private TextField holdingInfo;
    @FXML
    private ImageView phoneIcon;
    @FXML
    private Button backToDial;

    public void setText(String s) {
        holdingInfo.setText(s);
    }

    @FXML
    public void back2Dial(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("close");
            oos.close();
//            socket.close();
            callover();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void TCPConnect(){
        try {
            socket = new Socket("127.0.0.1",targetPort);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(port);
            new Thread(() -> {
                while (true){
                    try {
                        if (!socket.isClosed()){
                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                            String res = (String) ois.readObject();
                            if(res.equalsIgnoreCase("close")){
//                                socket.close();
                                Platform.runLater(() -> callover());
                                break;
                            }
                            if (res.equalsIgnoreCase("accept")){
                                Platform.runLater(() -> {
                                    try {
                                        ObservableList<Stage> stage = FXRobotHelper.getStages();
                                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/acceptCall.fxml"));
                                        Parent root = fxmlLoader.load();
                                        AcceptCall acceptCall = fxmlLoader.getController();
                                        acceptCall.startUDPClient();
                                        acceptCall.setSocket(socket);
                                        acceptCall.setTargetPort(targetPort+"");
                                        Scene scene = new Scene(root);
                                        stage.get(0).setScene(scene);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                break;
                            }
                        }
                    } catch (IOException | ClassNotFoundException e ) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            System.out.println("connect fail!");
        }
    }

    public void callover(){
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
        phoneIcon.setImage(new Image("file:///C:\\Users\\hahah\\IdeaProjects\\ip-phone\\image\\phone.png"));
    }


}
