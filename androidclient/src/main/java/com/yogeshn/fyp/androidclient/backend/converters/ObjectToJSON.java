package com.yogeshn.fyp.androidclient.backend.converters;

import com.google.gson.Gson;
import com.yogeshn.fyp.androidclient.activities.LoginPatientDetailActivity;
import com.yogeshn.fyp.androidclient.backend.model.questionnaire.Questionnaire;
import com.yogeshn.fyp.androidclient.backend.model.json.AnswersJSON;
import com.yogeshn.fyp.androidclient.backend.model.question.Question;

import java.util.Iterator;

/**
 * This class converts questionnaire objects in JSON objects.
 *
 */
public class ObjectToJSON
{
	/**
	 * Converts the Questionnaire object into AnswersJSON object and parses it into JSON.
	 * 
	 * @param questionnaire Questionnaire object to convert.
	 * @return AnswersJON in JSON string format
	 */
	public static String createJSON(Questionnaire questionnaire)
	{
		AnswersJSON answersJSON = new AnswersJSON();
		answersJSON.setPatientId(LoginPatientDetailActivity.getPatientNHSNumber());
		answersJSON.setQuestionnaireId(questionnaire.getQuestionnaireID());

        for (Iterator<Question> iterator = questionnaire.getQuestions().iterator(); iterator.hasNext(); ) {
            Question question = iterator.next();
            answersJSON.addAnswer(getAnswerJSON(question));
        }

		return new Gson().toJson(answersJSON);
	}
	
	/**
	 * Converts a Question object into AnswerJSON object.
	 * 
	 * @param question Question object to be converted.
	 * @return Converted AnswerJSON object.
	 */
	private static AnswersJSON getAnswerJSON(Question question)
	{
        AnswersJSON answerJSON = new AnswersJSON();
		answerJSON.setQuestionId(question.getID());
		answerJSON.setAnswer(question.getAnswerAsList());
		return answerJSON;
	}

}