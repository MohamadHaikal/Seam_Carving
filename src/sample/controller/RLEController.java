package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.RLE_Algorithm;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class RLEController implements Initializable {
    public RLE_Algorithm rle;
    public static String path;
    @FXML
    private TitledPane titledPane;
    @FXML
    TextArea textArea;
    @FXML
    Text text;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        rle = new RLE_Algorithm();
    }

    public void SelectFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("select image");

        Stage stage = (Stage) titledPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            path = file.getAbsolutePath();
            try {
                Scanner s = new Scanner(new File(path)).useDelimiter("\\s+");
                while (s.hasNext()) {
                    if (s.hasNextInt()) {
                        textArea.appendText(s.nextInt() + " ");
                    } else {
                        textArea.appendText(s.next() + " ");
                    }
                }
            } catch (FileNotFoundException ex) {
                System.err.println(ex);
            }
        }

    }

    public void Compression() throws IOException {

        rle.CompressionToFile();
        textArea.clear();
        text.setText("Compression File Content");
        try {
            Scanner s = new Scanner(new File("compression.txt")).useDelimiter("\\s+");
            while (s.hasNext()) {
                if (s.hasNextInt()) {
                    textArea.appendText(s.nextInt() + " ");
                } else {
                    textArea.appendText(s.next() + " ");
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }

    public void Decompression() throws IOException {

        rle.decompressionToFile();
        textArea.clear();
        text.setText("Decompression File Content");
        try {
            Scanner s = new Scanner(new File("decompression.txt")).useDelimiter("\\s+");
            while (s.hasNext()) {
                if (s.hasNextInt()) {
                    textArea.appendText(s.nextInt() + " ");
                } else {
                    textArea.appendText(s.next() + " ");
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }


}
