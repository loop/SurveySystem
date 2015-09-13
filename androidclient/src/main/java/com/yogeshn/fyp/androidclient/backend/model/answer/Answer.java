package com.yogeshn.fyp.androidclient.backend.model.answer;

import java.util.List;

/**
 * Abstract class defining basic method for all Answers.
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public abstract class Answer
{
	protected String id;

	/**
	 * Public constructor for Answer.
	 *
	 * @param id Question ID for the answer it is for
	 */
	public Answer(String id)
	{
		this.id = id;
	}
	
	/**
	 * Gets the answer for a question.
	 * 
	 * @return Answer for a question to which it belongs.
	 */
	public abstract String getAnswer();
	
	/**
	 * Sets the answer.
	 * 
	 * @param answer answer
	 */
	public abstract void addAnswer(String answer);
	
	/**
	 * Clears the answer content.
	 */
	public abstract void clearAnswer();
	
	/**
	 * Gets the answers in a list.
	 * 
	 * @return list of answers
	 */
	public abstract List<String> getAnswerList();

}