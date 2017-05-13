package com.controller;

import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main extends Application {

    static int port;

    static ServerSocket serverSocket;

    @Override
    public void start(Stage primaryStage) throws Exception{
        port = getRandomPort();
        Parent root = FXMLLoader.load(getClass().getResource("../view/dial.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("IP PHONE");
        primaryStage.show();
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (true){
                    Socket socket = serverSocket.accept();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    String sourcePort = ois.readObject()+"";

                    Platform.runLater(() -> {
                        try {
                            ObservableList<Stage> stage = FXRobotHelper.getStages();
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/call_coming.fxml"));
                            Parent root1= fxmlLoader.load();
                            CallComing callComing = fxmlLoader.getController();
                            callComing.setSocket(socket);
                            callComing.setSourcePort(sourcePort);
                            callComing.setTextField(sourcePort+" wants to call you ...");
                            Scene scene1 = new Scene(root1);
                            stage.get(0).setScene(scene1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private int getRandomPort() {
        return (int)Math.round(Math.random()*63000+1025);
    }
}
