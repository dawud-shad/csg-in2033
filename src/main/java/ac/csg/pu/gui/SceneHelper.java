package ac.csg.pu.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneHelper {

    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary stage not set. Call SceneHelper.setStage(stage) first.");
        }
        return primaryStage;
    }

    public static void switchScene(String fxmlPath) {
        Stage stage = getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(SceneHelper.class.getResource(fxmlPath));
            Scene scene = new Scene(root); // size comes from FXML layout
            stage.setScene(scene);
            stage.sizeToScene();          // adjust window to new scene
            stage.centerOnScreen();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlPath, e);
        }
    }

    // Get stage from node
    public static Stage getStage(javafx.scene.Node node) {
        if (node == null || node.getScene() == null) {
            throw new IllegalArgumentException("Node is not attached to a scene yet.");
        }
        return (Stage) node.getScene().getWindow();
    }

}