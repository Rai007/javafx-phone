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

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by hahah on 2017/5/13.
 */
public class AcceptCall implements Initializable {

    static boolean play = true;
    static boolean record = true;
    final int[] timer = {0};

    public void setTargetPort(String targetPort) {
        this.targetPort = targetPort;
    }

    private String targetPort;

    private Socket socket;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @FXML
    public ImageView acceptcallimg;
    @FXML
    public TextField calltime;
    @FXML
    public Button callhangup;
    @FXML
    public Button callloader;

    public void startUDPServer(){
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(12003);
            DatagramSocket finalDatagramSocket = datagramSocket;
            new Thread(() -> {
                try {
                    DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class,getFormat());
                    SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
                    speaker.open(getFormat());
                    speaker.start();
                    while (play){
                        byte[] buffer = new byte[2048];
                        DatagramPacket datagramPacket = new DatagramPacket(buffer,0,buffer.length);
                        finalDatagramSocket.receive(datagramPacket);
                        byte[] data = datagramPacket.getData();
                        speaker.write(data,0,data.length);
                    }
                    speaker.stop();
                    speaker.close();
                } catch (IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    DatagramSocket datagramSocket1 = new DatagramSocket();
                    DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class,getFormat());
                    TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(micInfo);
                    mic.open(getFormat());
                    byte[] tmpBuff = new byte[2048];
                    mic.start();
                    while (record){
                        int count = mic.read(tmpBuff,0,tmpBuff.length);
                        if (count > 0){
                            DatagramPacket datagramPacket = new DatagramPacket(tmpBuff,tmpBuff.length,InetAddress.getByName("127.0.0.1"),12004);
                            datagramSocket1.send(datagramPacket);
                        }
                    }
                    mic.stop();
                    mic.close();
                } catch (LineUnavailableException | IOException e) {
                    e.printStackTrace();
                }
            }).start();

            /*new Thread(() -> {
                System.out.println("server socket : "+socket.getLocalPort());
                while (true){
                        try {
                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                            String res = (String) ois.readObject();
                            if(res.equalsIgnoreCase("callover")){
                                ois.close();
                                socket.close();
                                Platform.runLater(() -> callover());
                                break;
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                }
            }).start();*/

        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    private AudioFormat getFormat() {
        float sampleRate = 12000;
        int sampleSizeInBits = 16;
        int channles = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channles, signed, bigEndian);
    }

    public void startUDPClient(){
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(12004);
            DatagramSocket finalDatagramSocket = datagramSocket;
            new Thread(() -> {
                try {
                    DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class,getFormat());
                    SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
                    speaker.open(getFormat());
                    speaker.start();
                    while (play){
                        byte[] buffer = new byte[2048];
                        DatagramPacket datagramPacket = new DatagramPacket(buffer,0,buffer.length);
                        finalDatagramSocket.receive(datagramPacket);
                        byte[] data = datagramPacket.getData();
                        speaker.write(data,0,data.length);
                    }
                    speaker.stop();
                    speaker.close();
                } catch (IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    DatagramSocket datagramSocket1 = new DatagramSocket();
                    DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class,getFormat());
                    TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(micInfo);
                    mic.open(getFormat());
                    byte[] tmpBuff = new byte[2048];
                    mic.start();
                    while (record){
                        int count = mic.read(tmpBuff,0,tmpBuff.length);
                        if (count > 0){
                            DatagramPacket datagramPacket = new DatagramPacket(tmpBuff,tmpBuff.length,InetAddress.getByName("127.0.0.1"),12003);
                            datagramSocket1.send(datagramPacket);
                        }
                    }
                    mic.stop();
                    mic.close();
                } catch (LineUnavailableException | IOException e) {
                    e.printStackTrace();
                }
            }).start();

            /*new Thread(() -> {
                System.out.println("Client socket : "+socket.getLocalPort());
                while (true){
                    if(!socket.isClosed()){
                        try {
                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                            String res = (String) ois.readObject();
                            if(res.equalsIgnoreCase("callover")){
                                socket.close();
                                ois.close();
                                Platform.runLater(() -> callover());
                                break;
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();*/
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acceptcallimg.setImage(new Image("file:///C:\\Users\\hahah\\IdeaProjects\\ip-phone\\image\\talking2.png"));
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(() -> {
            calltime.setText("Call Time : "+(timer[0]++)+"s");
        }, 0, 1, TimeUnit.SECONDS);
    }
    @FXML
    public void callHangUp(ActionEvent actionEvent) {
        play = false;
        record = false;
        try {
            System.out.println("hang up :"+socket.getLocalPort());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("callover");
            callover();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callover(){
        try {
            ObservableList<Stage> stage = FXRobotHelper.getStages();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/callover.fxml"));
            Parent root = fxmlLoader.load();
            Callover callover = fxmlLoader.getController();
            callover.setText("The call to "+targetPort+" lasted "+timer[0]+"s");
            callover.setTargetPort(targetPort);
            Scene scene = new Scene(root);
            stage.get(0).setScene(scene);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
}
