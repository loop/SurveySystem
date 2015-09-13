package com.yogeshn.fyp.androidclient.backend.model.json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Class used as a template for converting Questionnaire into JSON.
 *
 */
public class QuestionnaireJSON
{
    @SerializedName (value = "id")
    private int questionnaire_id;
    @SerializedName (value = "title")
    private String questionnaire_title;
    private List<QuestionJSON> questions;
    private long id;
    private String title;
    private List<QuestionnaireJSON> pointers;

    /**
     * Getter for questionnaire ID.
     *
     * @return questionnaire ID
     */
    public int getQuestionnaireId()
    {
        return questionnaire_id;
    }

    /**
     * Getter for questionnaire title.
     *
     * @return questionnaire title
     */
    public String getQuestionnaireTitle()
    {
        return questionnaire_title;
    }

    /**
     * Getter for list of questions in questionnaires.
     *
     * @return question list
     */
    public List<QuestionJSON> getQuestions()
    {
        return questions;
    }

    /**
     * Getter for ID.
     *
     * @return questionnaire pointer ID
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for questionnaire pointer title.
     *
     * @return questionnaire title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the list of questionnaire pointers.
     *
     * @return list of questionnaire pointers
     */
    public List<QuestionnaireJSON> getPointers()
    {
        return pointers;
    }
}
