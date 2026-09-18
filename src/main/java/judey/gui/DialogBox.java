package judey.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a text flow containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private StackPane dialogContainer;
    @FXML
    private Label plainDialog;
    @FXML
    private TextFlow richDialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (text.contains("[[highlight]]")) {
            setRichDialogText(text);
            plainDialog.setVisible(false);
            plainDialog.setManaged(false);
        } else {
            plainDialog.setText(text);
            richDialog.setVisible(false);
            richDialog.setManaged(false);
        }
        displayPicture.setImage(img);
    }

    private void setRichDialogText(String text) {
        String start = "[[highlight]]";
        String end = "[[/highlight]]";
        int cursor = 0;
        while (cursor < text.length()) {
            int startIndex = text.indexOf(start, cursor);
            if (startIndex < 0) {
                addText(text.substring(cursor), false);
                return;
            }
            addText(text.substring(cursor, startIndex), false);
            int endIndex = text.indexOf(end, startIndex + start.length());
            if (endIndex < 0) {
                addText(text.substring(startIndex + start.length()), true);
                return;
            }
            addText(text.substring(startIndex + start.length(), endIndex), true);
            cursor = endIndex + end.length();
        }
    }

    private void addText(String value, boolean highlighted) {
        if (value.isEmpty()) {
            return;
        }
        Text text = new Text(value);
        text.getStyleClass().add("dialog-text");
        if (highlighted) {
            text.getStyleClass().add("error-highlight");
        }
        richDialog.getChildren().add(text);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        getActiveDialog().getStyleClass().add("reply-label");
    }

    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    public static DialogBox getDukeDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }

    /** Creates a visually distinct dialog box for GUI error responses. */
    public static DialogBox getErrorDialog(String text, Image img) {
        var db = getDukeDialog(text, img);
        db.getActiveDialog().getStyleClass().add("error-label");
        return db;
    }

    public static DialogBox getBannerDialog(String text, Image img) {
        var db = getDukeDialog(text, img);
        db.getActiveDialog().getStyleClass().add("banner-label");
        return db;
    }

    /** Creates a styled dialog box for the application's help content. */
    public static DialogBox getHelpDialog(String text, Image img) {
        var db = getDukeDialog(text, img);
        db.getActiveDialog().getStyleClass().add("help-label");
        return db;
    }

    private javafx.scene.Node getActiveDialog() {
        return plainDialog.isManaged() ? plainDialog : richDialog;
    }
}
