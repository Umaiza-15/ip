package judey.gui;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Duration;

import judey.Judey;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Judey judey;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/UserExplorer.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaJudey.png"));

    @FXML
    public void initialize() {
        scrollPane.setVvalue(0.0);
    }

    /** Injects the Judey instance */
    public void setJudey(Judey j) {
        judey = j;
        dialogContainer.getChildren().add(
                DialogBox.getBannerDialog(judey.getWelcomeMessage(), dukeImage)
        );
        scrollPane.setVvalue(0.0);
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Judey's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = judey.getResponse(input);
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));
        if (input.trim().equals("help")) {
            dialogContainer.getChildren().add(DialogBox.getHelpDialog(response, dukeImage));
        } else if (response.startsWith("Oopsie!")) {
            dialogContainer.getChildren().add(DialogBox.getErrorDialog(response, dukeImage));
        } else {
            dialogContainer.getChildren().add(DialogBox.getDukeDialog(response, dukeImage));
        }
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
        userInput.clear();

        if (input.trim().equals("bye")) {
            scheduleWindowClose();
        }
    }

    /** Disables further input and closes the GUI after the farewell remains visible briefly. */
    private void scheduleWindowClose() {
        userInput.setDisable(true);
        sendButton.setDisable(true);

        PauseTransition farewellDelay = new PauseTransition(Duration.seconds(1.5));
        farewellDelay.setOnFinished(event -> {
            Scene scene = userInput.getScene();
            if (scene != null) {
                Window window = scene.getWindow();
                if (window != null) {
                    window.hide();
                }
            }
        });
        farewellDelay.play();
    }
}
