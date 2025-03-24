import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

import java.awt.*;

public class DesktopApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        // UI Components
        Label label = new Label("Enter URL:");
        TextField urlInput = new TextField();
        Button runTestButton = new Button("Run Test");

        // Run Selenium Test when button is clicked
        runTestButton.setOnAction(e -> {
            String url = urlInput.getText();
            SeleniumTestRunner.runTest(url);
        });

        // Layout
        VBox layout = new VBox(10, label, urlInput, runTestButton);
        Scene scene = new Scene(layout, 300, 200);

        primaryStage.setTitle("Selenium Test Runner");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

