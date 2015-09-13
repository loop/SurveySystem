package model.questions;

import java.util.List;

/**
 * This class is the single choice question object.
 */

public class SingleChoiceQuestion extends Question {
    private List<String> answerOptions;

    public SingleChoiceQuestion(String id, String title, String description, boolean required, List<String> answerOptions) {
        super(id, title, description, required);
        this.answerOptions = answerOptions;
    }

    @Override
    public void updateQuestion(Question question) {
        super.updateQuestion(question);
        SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) question;
        this.answerOptions = singleChoiceQuestion.answerOptions;
    }

    public List<String> getAnswerOptions() {
        return this.answerOptions;
    }

}
