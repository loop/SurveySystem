package com.yogeshn.fyp.androidclient.backend.model.answer;

import java.util.LinkedList;
import java.util.List;

/**
 * Answer object that contains multiple answers chosen by the user.
 */
@SuppressWarnings("WeakerAccess")
public class MultipleAnswers extends Answer
{
	List<String> answers;
	

	public MultipleAnswers(String id)
	{
		super(id);
		this.answers = new LinkedList<>();
	}

	public MultipleAnswers(String id, List<String> answers)
	{
		super(id);
		this.answers = answers;
	}

    /**
     * Sets answers for the question.
     *
     * @param answers list of answers
     */
    public void setAnswers(List<String> answers)
    {
        this.answers = answers;
    }

    /**
     * Removes a answer from current answers.
     *
     * @param answer answer to be removed from list
     */
    public void removeAnswer(String answer)
    {
        this.answers.remove(answer);
    }


    @Override
	public String getAnswer() 
	{
        if (!answers.isEmpty()) {

            String answer = answers.get(0);
            int i = 1;
            if (i < answers.size()) {
                do {
                    answer += ", " + answers.get(i);
                    i++;
                } while (i < answers.size());
            }
            return answer;
        } else {
            return "answer is empty";
        }
	}

	@Override
	public void addAnswer(String answer)
	{
		this.answers.add(answer);
	}

	@Override
	public void clearAnswer()
	{
		this.answers.clear();
	}

	@Override
	public List<String> getAnswerList() {
		return answers;
	}
}