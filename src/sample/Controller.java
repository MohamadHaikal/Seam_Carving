package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.SeamCarverAlgorithm;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public static String path = "test.jpg";
    public static int output_width;
    public static int output_height;
    public static String output_file = null;
    SeamCarverAlgorithm carver = null;
    String input_file = null;
    BufferedImage input_image = null;
    @FXML
    private TitledPane titledPane;
    @FXML
    ImageView imageView;
    @FXML
    TextField b_height, b_width, a_height, a_width;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        b_width.setEditable(false);
        b_height.setEditable(false);
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(inputstream);
        File input_f = new File(path);
        try {
            input_image = ImageIO.read(input_f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        b_height.setText("" + input_image.getHeight());
        b_width.setText("" + input_image.getWidth());
        imageView.setImage(image);


    }

    public void SelectImage() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("select image");

        Stage stage = (Stage) titledPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            path = file.getAbsolutePath();


            FileInputStream inputstream = new FileInputStream(path);
            Image image = new Image(inputstream);
            File input_f = new File(path);
            input_image = ImageIO.read(input_f);

            b_height.setText("" + input_image.getHeight());
            b_width.setText("" + input_image.getWidth());
            imageView.setImage(image);


        }
    }

    public void Processing() throws IOException {
        try {
            input_file = path;
            output_file = "C:\\Users\\User\\Desktop\\4.jpg";
            output_width = Integer.parseInt(a_width.getText());
            output_height = Integer.parseInt(a_height.getText());
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        carver = new SeamCarverAlgorithm(input_file);
        /* carver.EnergyMapPrint();

         */
        carver.carve(output_width, output_height);
        carver.saveFinalImage(output_file, "jpg");
       // carver.saveSeamTable();
        Parent root = FXMLLoader.load(getClass().getResource("view/carving.fxml"));
        Scene scene = new Scene(root, output_width + 50, output_height + 50);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void RLE() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("view/RLE.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Rle Algorithm ");
        stage.show();

    }


}







































