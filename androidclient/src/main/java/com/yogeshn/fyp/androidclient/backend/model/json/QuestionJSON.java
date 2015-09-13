package com.yogeshn.fyp.androidclient.backend.model.json;

import java.util.HashMap;
import java.util.List;

/**
 * Class used as a template for converting Question into JSON.
 *
 */
public class QuestionJSON
{
    private String id;
    private String title;
    private String description;
    private int type;
    private boolean required;
    private List<String> answerOptions;
    private int upperBound, lowerBound;
    private HashMap<String, List<QuestionJSON>> dependentQuestions;

    /**
     * Getter for question ID.
     *
     * @return question ID
     */
    public String getId()
    {
        return id;
    }

    /**
     * Getter for question title.
     *
     * @return question title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Getter for question type.
     *
     * @return question type
     */
    public int getType()
    {
        return type;
    }

    /**
     * Getter to get if question is required flag.
     *
     * @return true if required else false
     */
    public boolean isRequired()
    {
        return required;
    }

    /**
     * Getter for question description.
     *
     * @return question description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for dependent questions.
     *
     * @return hashmap of dependent questions
     */
    public HashMap<String, List<QuestionJSON>> getDependentQuestions() {
        return dependentQuestions;
    }

    /**
     * Getter for dependent questions for a patricular question
     *
     * @return list of dependent questions
     */
    public List<QuestionJSON> getDependentQuestions(String key)
    {
        return dependentQuestions.get(key);
    }

    /**
     * Getter for upper bound of range question
     *
     * @return upper bound
     */
    public int getUpperBound() {
        return upperBound;
    }

    /**
     * Getter for lower bound of range question
     *
     * @return lower bound
     */
    public int getLowerBound() {
        return lowerBound;
    }

    /**
     * Getter for answer choices
     *
     * @return list of answers
     */
    public List<String> getAnswerOptions() {
        return answerOptions;
    }
}
