package com.yogeshn.fyp.androidclient.backend.model.answer;

import java.util.LinkedList;
import java.util.List;


/**
 * Answer that contains a single answer by the user.
 */
public class SingleAnswer extends Answer
{
	private String answer;

	public SingleAnswer(String id)
	{
		super(id);
		this.answer = "";
	}

	public SingleAnswer(String id, String answer)
	{
		super(id);
		this.answer = answer;
	}

    /**
     * Sets the answer to the given string
     *
     * @param answer new answer string
     */
    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

	@Override
	public String getAnswer() 
	{
		return answer;
	}

	@Override
	public void addAnswer(String answer)
	{
		this.answer = answer;
	}

	@Override
	public void clearAnswer()
	{
		this.answer = "";
	}

	@Override
	public List<String> getAnswerList()
	{
		List<String> stringList = new LinkedList<>();
		stringList.add(answer);
		return stringList;
	}
}
