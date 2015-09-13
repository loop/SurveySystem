package gui.questionnaires.creator;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Multiple choice question controller responsible for setting up the view.
 */

public class MultipleChoiceController extends AbstractChoiceQuestionTypeController implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private ListView<String> choicesListView;
    @FXML private TextField newChoiceField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionTypeIdentifier = "Multiple Choice Question: ";
        this.choicesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        newChoiceField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    addNewChoice();
                }
            }
        });
    }
}
