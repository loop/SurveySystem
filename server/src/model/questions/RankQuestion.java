package model.questions;

import java.util.List;

/**
 * This class is the rank question object.
 */

public class RankQuestion extends Question {
    private List<String> answerOptions;

    public RankQuestion(String id, String title, String description, boolean required, List<String> answerOptions) {
        super(id, title, description, required);
        this.answerOptions = answerOptions;
    }

    @Override
    public void updateQuestion(Question question) {
        super.updateQuestion(question);
        RankQuestion rankQuestion = (RankQuestion) question;
        this.answerOptions = rankQuestion.answerOptions;
    }
}
