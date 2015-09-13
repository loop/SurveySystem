package com.yogeshn.fyp.androidclient.backend.model.questionnaire;

import java.util.Iterator;
import java.util.List;

import com.yogeshn.fyp.androidclient.backend.model.question.Question;

/**
 * This class defines the Questionnaire object
 *
 */
@SuppressWarnings("CanBeFinal")
public class Questionnaire {
    private long id;
    private String title;
    private List<Question> questions;

    public Questionnaire(long id, List<Question> questions, String title) {
        this.id = id;
        this.questions = questions;
        this.title = title;
    }

    /**
     * Getter for questionnaire ID.
     *
     * @return questionnaire ID
     */
    public long getQuestionnaireID() {
        return this.id;
    }

    /**
     * Getter for questionnaire title.
     *
     * @return questionnaire title
     */
    public String getQuestionnaireTitle() {
        return this.title;
    }

    /**
     * Getter for a question at a specific index.
     *
     * @param index question location
     * @return question object
     */
    public Question getQuestion(int index) {
        return this.questions.get(index);
    }

    /**
     * Setter for setting the questionnaires list of questions.
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * Adds question to questionnaire list of questions.
     *
     * @param question
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    /**
     * Adds a question at a specific index.
     *
     * @param question question to add
     * @param index    location to add to
     */
    public void addQuestion(Question question, int index) {
        this.questions.add(index + 1, question);
    }

    /**
     * Adds a list of quesitons after a specific index.
     *
     * @param questions list of questions to add
     * @param index     location to add at
     */
    public void addQuestions(List<Question> questions, int index) {
        int i = questions.size() - 1;
        if (i >= 0) {
            do {
                this.questions.add(index + 1, questions.get(i));
                i--;
            } while (i >= 0);
        }
    }

    /**
     * Gets the number of questions in the questionnaire.
     *
     * @return number of questionnaires
     */
    public int getNumberOfQuestions() {
        return this.questions.size();
    }

    /**
     * Deletes the questionnaire.
     */
    public void deleteQuestionnaire() {
        questions.clear();
    }

    /**
     * Getter for all the questions.
     *
     * @return list of all questions
     */
    public List<Question> getQuestions() {
        return this.questions;
    }

    /**
     * Removes a question from the questionnaire.
     *
     * @param question question to remove
     */
    public void removeQuestion(Question question) {
        this.questions.remove(question);
    }


    /**
     * Removes a list of questions from the questionnaire.
     *
     * @param questions list of questions to remove
     */
    public void removeQuestions(List<Question> questions) {
        for (Iterator<Question> iterator = questions.iterator(); iterator.hasNext(); ) {
            Question q = iterator.next();
            this.questions.remove(q);
        }
    }

    /**
     * Checks if all required quesitons have been completed.
     *
     * @return true if all required questions completed, else false
     */
    public boolean isCompleted() {
        for (Iterator<Question> iterator = questions.iterator(); iterator.hasNext(); ) {
            Question q = iterator.next();
            if (!q.isRequired() || q.isAnswered()) {
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the location of the incomplete required questions.
     *
     * @return the index of the question
     */
    public int getFirstRequiredQuestionToComplete() {
        int i = 0;
        if (i < questions.size()) {
            do {
                Question question = questions.get(i);
                if (!question.isRequired() || question.isAnswered()) {
                    i++;
                } else {
                    return i;
                }
            } while (i < questions.size());
        }
        return -1;
    }

    /**
     * Gets the location of the incomplete not-required questions.
     *
     * @return the index of the question
     */
    public int getFirstNonRequiredQuestionToComplete() {
        int i = 0;
        if (i < questions.size()) {
            do {
                Question q = questions.get(i);
                if (!q.isRequired() && !q.isAnswered()) {
                    return i;
                } else {
                    i++;
                }
            } while (i < questions.size());
        }
        return -1;
    }
}
