package gui.questionnaires.creator;

import helpers.PopUpCreator;
import model.questions.Question;
import model.questions.RangeQuestion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Range question question controller responsible for setting up the view.
 */

public class RangeController extends QuestionTypeController implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField lowerBoundField;
    @FXML private TextField upperBoundField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lowerBoundField.setPromptText("0");
        upperBoundField.setPromptText("10");
        questionTypeIdentifier = "Range Question: ";
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
        boolean defined = isQuestionFormatCorrect();
        if (defined) {
            int lowerBound = Integer.parseInt(lowerBoundField.getText());
            int upperBound = Integer.parseInt(upperBoundField.getText());
            return new RangeQuestion(Id, titleField.getText(), descriptionField.getText(), required, lowerBound, upperBound);
        } else if (!defined) {
            return new RangeQuestion(Id, "2bda2998d9b0ee197da142a0447f6725", descriptionField.getText(), required, 0, 0);
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
        lowerBoundField.clear();
        upperBoundField.clear();
    }

    /**
     * Checks to see if all required fields have been filled in to build the question object.
     *
     * @return true if question contains data
     */
    @Override
    public boolean isQuestionFormatCorrect() {
        boolean isItDefined = true;
        int lowerBound = 0;
        int upperBound = 0;
        try {
            lowerBound = Integer.parseInt(lowerBoundField.getText());
            upperBound = Integer.parseInt(upperBoundField.getText());

            if (lowerBound >= upperBound) {
                PopUpCreator.getAlert(Alert.AlertType.INFORMATION, "Range Question Error", "Upper and lower bound not valid", "Please lower bound is higher than upper bound");
                isItDefined = false;
            } else if (upperBound <= lowerBound) {
                PopUpCreator.getAlert(Alert.AlertType.INFORMATION, "Range Question Error", "Upper and lower bound not valid", "Please upper bound is higher than lower bound");
                isItDefined = false;
            }
        } catch (NumberFormatException e) {
            PopUpCreator.getAlert(Alert.AlertType.INFORMATION, "Range Question Error", "Upper and lower bound not valid", "Please ensure that the values for lower and upper bound are correct. They can only be number values.");
            isItDefined = false;
        }
        return isItDefined && (titleField.getText().length() > 0 && lowerBoundField.getText().length() > 0 && upperBoundField.getText().length() > 0 && upperBound > lowerBound);
    }

    /**
     * If question already exists it updates it with the new content.
     *
     * @param existingQuestion
     */
    @Override
    public void overwriteExsitingQuestion(Question existingQuestion) {
        RangeQuestion rangeQuestion = (RangeQuestion) existingQuestion;
        titleField.setText(rangeQuestion.getTitle());
        descriptionField.setText(rangeQuestion.getDescription());
        lowerBoundField.setText(Integer.toString(rangeQuestion.getLowerBound()));
        upperBoundField.setText(Integer.toString(rangeQuestion.getUpperBound()));
    }
}
