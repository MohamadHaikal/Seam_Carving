package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class CarvingController implements Initializable {
    @FXML
    ImageView image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(Controller.output_file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image1 = new Image(inputstream);
        image.setFitHeight(Controller.output_height);
        image.setFitWidth(Controller.output_width);
        image.setImage(image1);
    }
}
