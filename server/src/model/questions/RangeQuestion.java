package model.questions;

/**
 * This class is the range question object.
 */

public class RangeQuestion extends Question {
    private int lowerBound, upperBound;

    public RangeQuestion(String id, String title, String description, boolean required, int lowerBound, int upperBound) {
        super(id, title, description, required);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public void updateQuestion(Question question) {
        super.updateQuestion(question);
        RangeQuestion rangeQuestion = (RangeQuestion) question;
        this.lowerBound = rangeQuestion.lowerBound;
        this.upperBound = rangeQuestion.upperBound;
    }

    public int getLowerBound () {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

}
