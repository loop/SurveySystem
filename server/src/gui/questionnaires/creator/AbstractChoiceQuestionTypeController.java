package gui.questionnaires.creator;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.questions.Question;
import model.questions.SingleChoiceQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Choice question abstract class controller responsible for setting up the view.
 */
public abstract class AbstractChoiceQuestionTypeController extends QuestionTypeController {
    @FXML
    private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private ListView<String> choicesListView;
    @FXML private TextField newChoiceField;
    protected Question question;
    protected String questionTypeIdentifier;

    /**
     * Adds a new choice to the choices list.
     */
    public void addNewChoice() {
        if (newChoiceField.getText().length() > 0) {
            choicesListView.getItems().add(newChoiceField.getText());
            newChoiceField.clear();
        }
    }

    /**
     * Removes choices from the choices list.
     */
    public void removeSelectedChoices() {
        ArrayList<Integer> selected = new ArrayList<>(choicesListView.getSelectionModel().getSelectedIndices());
        Collections.reverse(selected);
        for (Integer integer : selected) {
            choicesListView.getItems().remove(integer.intValue());
        }
    }

    /**
     * Constructs a question object.
     *
     * @param Id question ID
     * @param required is the question needed
     * @return the question object
     */
    public Question getQuestion(String Id, boolean required) {
        if (isQuestionFormatCorrect()) {
            List<String> choices = new ArrayList<>(choicesListView.getItems());
            return new SingleChoiceQuestion(Id, titleField.getText(),descriptionField.getText(), required, choices);
        } else {
            return null;
        }
    }

    /**
     * Clears the input fields
     */
    public void clearInputFields() {
        titleField.clear();
        descriptionField.clear();
        choicesListView.getItems().clear();
    }

    /**
     * Checks to see if all required fields have been filled in to build the question object.
     *
     * @return true if question contains data
     */
    public boolean isQuestionFormatCorrect() {
        return (titleField.getText().length() > 0 && choicesListView.getItems().size() > 0);
    }

    /**
     * If question already exists it updates it with the new content.
     *
     * @param existingQuestion
     */
    public void overwriteExsitingQuestion(Question existingQuestion) {
        SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) existingQuestion;
        titleField.setText(singleChoiceQuestion.getTitle());
        descriptionField.setText(singleChoiceQuestion.getDescription());
        choicesListView.getItems().addAll(singleChoiceQuestion.getAnswerOptions());
    }
}
