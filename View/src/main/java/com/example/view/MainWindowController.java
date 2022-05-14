package com.example.view;

import Model.AES;
import Model.IO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

public class MainWindowController {

    private Stage stage;
    private Scene scene;

    private AES aes;

    private File fileToEncrypt;
    private File fileToDecrypt;


    MainWindowController() {
        stage = new Stage();
        aes = new AES();
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

        addTextLimiter(decryptedMessageKeyTextField, 16);
        addTextLimiter(encryptedMessageKeyTextField, 16);
        addTextLimiter(encryptedFileKeyTextField, 16);
        addTextLimiter(decryptedFileKeyTextField, 16);

        generateKeyEncryptFileButton.setOnAction(actionEvent -> encryptedFileKeyTextField.setText(generateKey(16)));
        generateKeyDecryptFileButton.setOnAction(actionEvent -> decryptedFileKeyTextField.setText(encryptedFileKeyTextField.getText()));

        generateKeyEncryptMesButton.setOnAction(actionEvent -> encryptedMessageKeyTextField.setText(generateKey(16)));
        generateKeyDecryptMesButton.setOnAction(actionEvent -> decryptedMessageKeyTextField.setText(encryptedMessageKeyTextField.getText()));

        copyEncryptedMessageButton.setOnAction(actionEvent -> copyToClipboard(encryptedTextArea));
        copyDecryptedMessageButton.setOnAction(actionEvent -> copyToClipboard(decryptedTextArea));
    }

    private void encryptMessageButtonHandler() {
        String message = messageToEncryptTextArea.getText();
        String key = encryptedMessageKeyTextField.getText();

        if(checkKey(key, 16, errorEncryptMessageLabel)) {
            if (!message.isEmpty()) {
                byte[] bytes = aes.cipher(message.getBytes(StandardCharsets.UTF_8), key, true);
                String encryptedMessage = Base64.getEncoder().encodeToString(bytes);
                encryptedTextArea.setText(encryptedMessage);
                errorEncryptMessageLabel.setText("");
            } else {
                errorEncryptMessageLabel.setText("Message to encrypt is empty");
            }
        }
    }

    private void decryptMessageButtonHandler() {
        String message = messageToDecryptTextArea.getText();
        String key = decryptedMessageKeyTextField.getText();


        if (checkKey(key, 16, errorDecryptMessageLabel)) {
            if (!message.isEmpty() && !key.isEmpty()) {
                byte[] bytes = aes.cipher(Base64.getDecoder().decode(message.getBytes(StandardCharsets.UTF_8)), key, false);
                String decryptedMessage = new String(bytes, StandardCharsets.UTF_8);
                decryptedTextArea.setText(decryptedMessage);
                errorDecryptMessageLabel.setText("");
            } else {
                errorDecryptMessageLabel.setText("Message to decrypt or key are empty");
            }
        }
    }

    private void encryptFileButtonHandler() {
        String key = encryptedFileKeyTextField.getText();

        if (checkKey(key, 16, errorEncryptFileLabel)) {
            if (fileToEncrypt != null && !key.isEmpty() && !fileNameToEncryptTextField.getText().isEmpty()) {
                byte [] file = IO.readFileToByteArray(fileToEncrypt.getAbsolutePath());
                byte [] encryptedFile = aes.cipher(file, key, true);
                //byte [] decryptedFile = aes.cipher(encryptedFile, key, false);
                IO.writeFileFromByteArray(encryptedFile, fileToEncrypt.getParent() + "\\" + fileNameToEncryptTextField.getText());
                errorEncryptFileLabel.setText("");
            } else {
                errorEncryptFileLabel.setText("Select file to encrypt or name");
            }
        }
    }

    private void decryptFileButtonHandler() {
        String key = decryptedFileKeyTextField.getText();

        if (checkKey(key, 16, errorDecryptFileLabel)) {
            if (fileToDecrypt != null && !key.isEmpty() && !fileNameToDecryptTextField.getText().isEmpty()) {
                byte [] file = IO.readFileToByteArray(fileToDecrypt.getAbsolutePath());
                byte [] decryptedFile = aes.cipher(file, key, false);
                IO.writeFileFromByteArray(decryptedFile, fileToDecrypt.getParent() + "\\" + decryptedFileNameTextArea.getText());
                errorDecryptFileLabel.setText("");
            } else {
                errorDecryptFileLabel.setText("Select file to decrypt or key field is empty");
            }
        }
    }

    private File selectFileButtonHandler(TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        File file = fileChooser.showOpenDialog(stage);

        textField.setText(file.getName());

        return file;
    }

    private void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

    private boolean checkKey(String key, int charactersAmount,Label error) {
        if(key.length() != charactersAmount) {
            error.setText("Key is too short");
            return false;
        }
        return true;
    }

    private String generateKey(int length) {
        Random random = new Random();
        String key = "";

        for (int i = 0; i < length; i++) {
            char sign = (char)(random.nextInt(93) + '!');
            key += sign;
        }
        return key;
    }

    private void copyToClipboard(TextArea text) {
        StringSelection stringSelection = new StringSelection (text.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        clipboard.setContents (stringSelection, null);
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

    @FXML
    private Button generateKeyEncryptMesButton;

    @FXML
    private Button generateKeyDecryptMesButton;

    @FXML
    private Button copyEncryptedMessageButton;

    @FXML
    private Button copyDecryptedMessageButton;

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

    @FXML
    private Button generateKeyEncryptFileButton;

    @FXML
    private Button generateKeyDecryptFileButton;
}