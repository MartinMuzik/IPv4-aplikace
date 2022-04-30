package cz.sspbrno.ipv4aplikace;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private Label resultLabel;
    @FXML
    private TextField inputField;

    @FXML
    protected void onAnalyzeButtonClick() {
        resultLabel.setText(Analyzer.startAnalyze(inputField.getText()));
    }
}