package gui.questionnaires.creator;

import model.questions.FreeTextQuestion;
import model.questions.Question;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Free text questionnaire controller responsible for setting up the view.
 */

public class FreeTextController extends QuestionTypeController implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionTypeIdentifier = "Free Text Question: ";
    }

    /**
     * Constructs a question object.
     *
     * @param Id question ID
     * @param required is the question needed
     * @return the question object
     */
    @Override
    public Question getQuestion(String Id, boolean required) {
        if (isQuestionFormatCorrect()) {
            return new FreeTextQuestion(Id, titleField.getText(), descriptionField.getText(), required);
        } else {
            return null;
        }
    }

    /**
     * Clears the input fields
     */
    @Override
    public void clearInputFields() {
        titleField.clear();
        descriptionField.clear();
    }

    /**
     * Checks to see if all required fields have been filled in to build the question object.
     *
     * @return true if question contains data
     */
    @Override
    public boolean isQuestionFormatCorrect() {
        return (titleField.getText().length() > 0);
    }

    /**
     * If question already exists it updates it with the new content.
     *
     * @param existingQuestion
     */
    @Override
    public void overwriteExsitingQuestion(Question existingQuestion) {
        FreeTextQuestion freeTextQuestion = (FreeTextQuestion) existingQuestion;
        titleField.setText(freeTextQuestion.getTitle());
        descriptionField.setText(freeTextQuestion.getDescription());
    }

}