package model.questions;

import java.util.List;

/**
 * This class is the multiple question object.
 */

public class MultipleChoiceQuestion extends Question {
    private List<String> answerOptions;

    public MultipleChoiceQuestion(String id, String title, String description, boolean required, List<String> answerOptions) {
        super(id, title, description, required);
        this.answerOptions = answerOptions;
    }

    @Override
    public void updateQuestion(Question question) {
        super.updateQuestion(question);
        MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;
        this.answerOptions = multipleChoiceQuestion.answerOptions;
    }
}
