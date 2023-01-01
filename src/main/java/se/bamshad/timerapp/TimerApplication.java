package se.bamshad.timerapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class TimerApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(TimerApplication.class.getResource("main.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(TimerApplication.class.getResource("main_showButtonsGUI_showTextFields.fxml"));
        //xmlLoader.setController(); //not complete. remove from fxml if using this one
        //fxmlLoader.setController(this); //we set it already in the fxml right?


        Scene scene = new Scene(fxmlLoader.load(), 620, 330);
        stage.setTitle("Bamshad Timer App - version 1.1.1");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}