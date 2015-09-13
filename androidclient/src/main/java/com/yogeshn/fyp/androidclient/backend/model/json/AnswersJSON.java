package com.yogeshn.fyp.androidclient.backend.model.json;

import java.util.LinkedList;
import java.util.List;

/**
 * JSON template for questionnaire answers parsing
 */
@SuppressWarnings("FieldCanBeLocal")
public class AnswersJSON
{
	@SuppressWarnings("unused")
	private long questionnaireId;
	@SuppressWarnings("unused")
	private String patientId;
	private List<AnswersJSON> answers;
    @SuppressWarnings("unused")
    private List<String> answer;
    @SuppressWarnings("unused")
    private String questionId;


    /**
     * Sets the question ID.
     *
     * @param questionId questionID value
     */
    public void setQuestionId(String questionId)
    {
        this.questionId = questionId;
    }

    /**
     * Sets the answer value
     *
     * @param answer answer value
     */
    public void setAnswer(List<String> answer)
    {
        this.answer = answer;
    }

    /**
     * Sets the questionnaireId
     *
     * @param questionnaireId
     */
	public void setQuestionnaireId(long questionnaireId)
	{
		this.questionnaireId = questionnaireId;
	}

    /**
     * Sets the patientId
     *
     * @param patientId
     */
	public void setPatientId(String patientId)
	{
		this.patientId = patientId;
	}

    /**
     * Adds AnswerJSON object to answer list
     *
     * @param answer
     */
	public void addAnswer(AnswersJSON answer)
	{
		if (answers == null)
			answers = new LinkedList<>();
		answers.add(answer);
	}
}