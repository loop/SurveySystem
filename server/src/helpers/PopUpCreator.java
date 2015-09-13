package helpers;

import javafx.scene.control.Alert;

/**
 * This class is responsible for creating pop up dialogs objects.
 *
 */

public class PopUpCreator {

    public static Alert getAlert(Alert.AlertType type, String title, String headerText, String contextText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
        return alert;
    }

    public static Alert getResponse(Alert.AlertType type, String title, String headerText, String contextText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        return alert;
    }

}
