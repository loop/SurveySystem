package gui.questionnaires.creator;

import model.questions.Question;

/**
 * Question abstract class controller responsible for setting up the view.
 */

public abstract class QuestionTypeController {

    protected Question question;
    protected String questionTypeIdentifier;


    /**
     * Constructs a question object.
     *
     * @param Id question ID
     * @param required is the question needed
     * @return the question object
     */
    public abstract Question getQuestion(String Id, boolean required);

    /**
     * Clears the input fields
     */
    public abstract void clearInputFields();

    /**
     * Checks to see if all required fields have been filled in to build the question object.
     *
     * @return true if question contains data
     */
    public abstract boolean isQuestionFormatCorrect();

    /**
     * If question already exists it updates it with the new content.
     *
     * @param existingQuestion
     */
    public abstract void overwriteExsitingQuestion(Question existingQuestion);
}
