package judey;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import judey.gui.MainWindow;

/**
 * A GUI for Judey using FXML.
 */
public class Main extends Application {

    private final Judey judey = new Judey();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Judey");
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setJudey(judey);  // inject the Judey instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}