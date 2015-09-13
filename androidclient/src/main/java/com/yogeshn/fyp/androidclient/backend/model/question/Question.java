package com.yogeshn.fyp.androidclient.backend.model.question;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.yogeshn.fyp.androidclient.backend.model.answer.Answer;

/**
 * Abstract class which defines methods for all questions.
 * 
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public abstract class Question
{
	protected String id;
	protected String question;
	protected Answer answer;
	protected boolean required;
	private String description;
	protected Map<String, List<Question>> dependentQuestions = new HashMap<>();

    /**
     * General constructor for Question object.
     *
     * @param id question ID.
     * @param question question text.
     * @param required is the question required
     * @param description question description
     */
    public Question (String id, String question, boolean required, String description)
    {
        this.id = id;
        this.question = question;
        this.required = required;
        this.description = description;
    }

	/**
	 * Gets the layout for the question.
	 * 
	 * @param context
	 * @param highContrastMode Defines whether highContrastMode is on or not.
	 * @return view
	 */
	public abstract View getView(Context context, boolean highContrastMode);
	
	/**
	 * Reads answer from question and saves it to JSON format.
	 */
	protected abstract void getSelectedAnswer();

    /**
     * Checks if question has been answered and is ready to be saved,
     *
     * @return true if ready, else false
     */
	public abstract boolean checkIfAnswerIsReady();
	
	/**
	 * Loads the current answer.
	 */
	public abstract void loadAnswer();
	
	/**
	 * Sets accessibility of the questions.
	 */
	protected abstract void setAccessibility();
	
	/**
	 * Returns the question text.
	 * Adds an asterisk (*) to a question that
	 * is set to be required.
	 * 
	 * @return Question text.
	 */
	public String getQuestion()
	{
		if (required)
			return question +" (Required)";
		return this.question;
	}
	
	/**
	 * Returns the currently selected answer.
	 * 
	 * @return Answer to the question.
	 */
	public String getAnswer()
	{
		return this.answer.getAnswer();
	}
	
	/**
	 * Saves the currently selected answer.
	 */
	public void saveAnswer()
	{
        if (answer.getAnswer().equals("")) {
        } else {
            answer.clearAnswer();
        }
        if (!checkIfAnswerIsReady()) {
            return;
        }
        getSelectedAnswer();
    }
	
	/**
	 * Setter for question text,
	 * 
	 * @param question question text
	 */
	public void setQuestion(String question)
	{
		this.question = question;
	}
	
	/**
	 * Setter for answer,
	 * 
	 * @param answer answer for question
	 */
	public void setAnswer(Answer answer)
	{
		this.answer = answer;
	}
		
	/**
	 * Check if question is required.
	 * 
	 * @return true if required, else false.
	 */
	public boolean isRequired()
	{
		return this.required;
	}
	
	/**
	 * Setter to set if question should be required.
	 * 
	 * @param required boolean flag to set question as required
	 */
	public void setRequired(boolean required)
	{
		this.required = required;
	}
	
	/**
	 * Adds dependent question and its condition to a question.
	 * 
	 * @param answerCondition condition under which the dependent question appears to
	 * @param question dependent question displayed once the condition is met.
	 */
	public void addDependentQuestion(String answerCondition, Question question)
	{		
		List<Question> list = getDependentQuestions(answerCondition);
		list.add(question);
		dependentQuestions.put(answerCondition, list);
	}
	
	/**
	 * Adds a list of dependent questions and their condition to a question.
	 * 
	 * @param condition condition under which the dependent question appears to
	 * @param questions list of dependent question displayed once the condition is met.
	 */
	public void addDependentQuestions(String condition, List<Question> questions)
	{
        int i = 0;
        while (i < questions.size()) {
            addDependentQuestion(condition, questions.get(i));
            i++;
        }
	}

    /**
     * Getter for a list of dependent questions and their condition to a question.
     *
     * @param condition condition under which the dependent question appears to
     */
	public List<Question> getDependentQuestions(String condition)
	{
        if (this.dependentQuestions.get(condition) == null) {
            return new LinkedList<>();
        }
        return this.dependentQuestions.get(condition);
    }
	
	/**
     * Checks if the question has dependent question for a given condition.
	 * 
	 * @param condition condition which is used to search for question
	 * @return true if it has dependent questions, else false.
	 */
	public boolean hasDependentQuestions(String condition)
	{
        return !(0 >= getDependentQuestions(condition).size());
    }
	
	/**
	 * Getter for question ID.
	 * 
	 * @return question ID
	 */
	public String getID()
	{
		return this.id;
	}
	
	/**
	 * Checks if question has been answered.
	 * 
	 * @return true if answered, else false
	 */
	public boolean isAnswered()
	{
        return !(answer.getAnswer().equals(""));
    }
	
	/**
	 * Getter for answers as a List.
	 * 
	 * @return list of answers
	 */
	public List<String> getAnswerAsList()
	{
		return answer.getAnswerList();
	}
	
	/**
	 * Getter for question description.
	 * 
	 * @return question description
	 */
	public String getDescription()
	{
		return this.description;
	}

    public String[] stringSplit(String stringToSplit) {
        String[] splitStringArray = stringToSplit.split(",");
        int i = 0;
        if (i >= splitStringArray.length) {
            return splitStringArray;
        }
        do {
            splitStringArray[i] = splitStringArray[i].trim();
            i++;
        } while (i < splitStringArray.length);
        return splitStringArray;
    }
}
