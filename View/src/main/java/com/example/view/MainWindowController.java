package com.example.view;

import Model.Class;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainWindowController {

    private Stage stage;
    private Scene scene;

    private File fileToEncrypt;
    private File fileToDecrypt;


    MainWindowController() {
        stage = new Stage();
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        loader.setController(this);

        scene = new Scene(loader.load(), 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Cryptography");
    }

    @FXML
    private void initialize() {
        encryptMessageButton.setOnAction( actionEvent -> encryptMessageButtonHandler());
        decryptMessageButton.setOnAction( actionEvent -> decryptMessageButtonHandler());

        encryptFileButton.setOnAction(actionEvent -> encryptFileButtonHandler());
        decryptFileButton.setOnAction(actionEvent -> decryptFileButtonHandler());

        selectFileToDecryptButton.setOnAction(actionEvent -> fileToDecrypt = selectFileButtonHandler(fileNameToDecryptTextField));
        selectFileToEncryptButton.setOnAction(actionEvent -> fileToEncrypt = selectFileButtonHandler(fileToEncryptNameTextField));
    }

    private void encryptMessageButtonHandler() {
        String message = messageToEncryptTextArea.getText();
        if (!message.isEmpty()) {
            //TODO
            errorEncryptMessageLabel.setText("");
        } else {
            errorEncryptMessageLabel.setText("Message to encrypt is empty");
        }

    }

    private void decryptMessageButtonHandler() {
        String message = messageToDecryptTextArea.getText();
        String key = decryptedMessageKeyTextField.getText();

        if (!message.isEmpty() && !key.isEmpty()) {
            //TODO
            errorDecryptMessageLabel.setText("");
        } else {
            errorDecryptMessageLabel.setText("Message to decrypt or key are empty");
        }
    }

    private void encryptFileButtonHandler() {

        if (fileToEncrypt != null) {

            //TODO
            errorEncryptFileLabel.setText("");
        } else {
            errorEncryptFileLabel.setText("Select file to encrypt");
        }

    }

    private void decryptFileButtonHandler() {
        String key = decryptedFileKeyTextField.getText();
        if (fileToDecrypt != null && !key.isEmpty()) {
            //TODO
            errorDecryptFileLabel.setText("");
        } else {
            errorDecryptFileLabel.setText("Select file to decrypt or key field is empty");
        }

    }

    private File selectFileButtonHandler(TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        File file = fileChooser.showOpenDialog(stage);

        textField.setText(file.getName());

        return file;
    }

    public void showStage() {
        stage.show();
    }


    //message

    @FXML
    private TextArea messageToEncryptTextArea;

    @FXML
    private TextArea messageToDecryptTextArea;

    @FXML
    private TextArea encryptedTextArea;

    @FXML
    private TextArea decryptedTextArea;

    @FXML
    private TextField decryptedMessageKeyTextField;

    @FXML
    private TextField encryptedMessageKeyTextField;

    @FXML
    private Button encryptMessageButton;

    @FXML
    private Button decryptMessageButton;

    @FXML
    private Label errorEncryptMessageLabel;

    @FXML
    private Label errorDecryptMessageLabel;

    //file

    @FXML
    private Button selectFileToEncryptButton;

    @FXML
    private Button encryptFileButton;

    @FXML
    private Button selectFileToDecryptButton;

    @FXML
    private Button decryptFileButton;

    @FXML
    private TextField fileToEncryptNameTextField;

    @FXML
    private TextField fileNameToEncryptTextField;

    @FXML
    private TextField encryptedFileKeyTextField;

    @FXML
    private TextField fileNameToDecryptTextField;

    @FXML
    private TextField decryptedFileNameTextArea;

    @FXML
    private TextField decryptedFileKeyTextField;

    @FXML
    private TextField decryptedExtensionFileTextArea;

    @FXML
    private Label errorEncryptFileLabel;

    @FXML
    private Label errorDecryptFileLabel;

    //@FXML
    //protected void onHelloButtonClick() {
    //welcomeText.setText("Welcome to JavaFX Application!");
    //}
}