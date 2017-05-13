package com.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by hahah on 2017/5/12.
 */
public class Holding implements Initializable{

    @FXML
    private TextField holdingInfo;
    @FXML
    private ImageView phoneIcon;
    @FXML
    private Text backToDial;

    public TextField getTextField() {
        return holdingInfo;
    }

    @FXML
    public void back2Dial(ActionEvent actionEvent){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        phoneIcon.setImage(new Image("file:///C:\\Users\\hahah\\IdeaProjects\\ip-phone\\image\\phone.png"));
    }
}
