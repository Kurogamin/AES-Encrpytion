package com.example.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainWindowController mainWindowController = new MainWindowController();
        mainWindowController.showStage();
    }

    public static void main(String[] args) {
        launch();
    }
}