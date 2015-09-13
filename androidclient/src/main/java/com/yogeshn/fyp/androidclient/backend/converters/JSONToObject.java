package com.yogeshn.fyp.androidclient.backend.converters;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.yogeshn.fyp.androidclient.backend.model.question.FreeTextQuestion;
import com.yogeshn.fyp.androidclient.backend.model.question.MultipleChoiceQuestion;
import com.yogeshn.fyp.androidclient.backend.model.question.SingleChoiceQuestion;
import com.yogeshn.fyp.androidclient.backend.model.questionnaire.Questionnaire;
import com.yogeshn.fyp.androidclient.backend.model.json.QuestionJSON;
import com.yogeshn.fyp.androidclient.backend.model.json.QuestionnaireJSON;
import com.yogeshn.fyp.androidclient.backend.model.question.Question;
import com.yogeshn.fyp.androidclient.backend.model.question.RangeQuestion;
import com.yogeshn.fyp.androidclient.backend.model.question.RankQuestion;
import com.yogeshn.fyp.androidclient.backend.model.question.YesNoQuestion;

/**
 * This class is responsible for converting JSON objects into objects which could be used as data
 * in the application.
 *
 */
public class JSONToObject {

    /**
     * Creates a Questionnaire object from a JSON questionnaire object.
     *
     * @param json Parsed JSON Questionnaire object.
     * @return Questionnaire object.
     */
    public static Questionnaire createQuestionnaire(QuestionnaireJSON json) {
        return new Questionnaire(json.getQuestionnaireId(),
                convertQuestions(json.getQuestions()),
                json.getQuestionnaireTitle());
    }

    /**
     * Converts a list of parsed JSON QuestionJSON objects to a list of Question objects.
     *
     * @param questions JSON question list
     * @return convereted questions list
     */
    private static List<Question> convertQuestions(List<QuestionJSON> questions) {
        List<Question> questionList = new LinkedList<>();

        int i = 0;
        while (i < questions.size()) {
            questionList.add(createQuestionFromJSON(questions.get(i)));
            i++;
        }
        return questionList;
    }

    /**
     * Converts a question from QuestionJSON to Question object.
     *
     * @param json Parsed JSON object to convert.
     * @return Quesiton object
     */
    private static Question createQuestionFromJSON(QuestionJSON json) {
        Question question;
        switch (json.getType()) {
            case 0:
                question = new RangeQuestion(json.getId(),
                        json.getTitle(),
                        json.getLowerBound(),
                        json.getUpperBound(),
                        json.isRequired(),
                        json.getDescription());
                break;
            case 1:
                question = new MultipleChoiceQuestion(json.getId(),
                        json.getTitle(),
                        json.getAnswerOptions(),
                        json.isRequired(),
                        json.getDescription());
                addDependentQuestions(json, question);
                break;
            case 2:
                question = new YesNoQuestion(json.getId(),
                        json.getTitle(),
                        json.isRequired(),
                        json.getDescription());
                addDependentQuestions(json, question);
                break;
            case 3:
                question = new FreeTextQuestion(json.getId(),
                        json.getTitle(),
                        json.isRequired(),
                        json.getDescription());
                break;
            case 4:
                question = new SingleChoiceQuestion(json.getId(),
                        json.getTitle(),
                        json.getAnswerOptions(),
                        json.isRequired(),
                        json.getDescription());
                addDependentQuestions(json, question);
                break;
            case 5:
                question = new RankQuestion(json.getId(),
                        json.getTitle(),
                        json.getAnswerOptions(),
                        json.isRequired(),
                        json.getDescription());
                break;
            default:
                question = null;
        }

        return question;
    }

    /**
     * Ads depedent questions to get the the data objects for them as well.
     *
     * @param questionJSON
     * @param question
     */
    private static void addDependentQuestions(QuestionJSON questionJSON, Question question) {
        List<QuestionJSON> questionJSONList;
        for (Iterator<String> iterator = questionJSON.getAnswerOptions().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            if ((questionJSONList = questionJSON.getDependentQuestions(key)) == null) {
            } else {
                question.addDependentQuestions(key, convertQuestions(questionJSONList));
            }
        }
    }
}
