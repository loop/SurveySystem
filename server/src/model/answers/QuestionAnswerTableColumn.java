package model.answers;

import model.questionnaire.Questionnaire;
import model.questions.Question;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is the Answer model.
 */

public class QuestionAnswerTableColumn
{
    private final String questionTitle;
    private final String answers;
    private String required;

    public QuestionAnswerTableColumn(Question q, Answer a)
    {
        this.questionTitle = q.getTitle();
        this.answers = join(a.getAnswers(), " | ");
        if(q.isRequired())
        {
            required = "Required";
        }
        else
        {
            required = "Not required";
        }
    }

    public QuestionAnswerTableColumn(Question q)
    {
        this.questionTitle = q.getTitle();
        this.answers = "(No answer)";
    }

    public static ArrayList<QuestionAnswerTableColumn> createListFromQuestionsAndAnswers(Questionnaire q, AnswerSet a)
    {
        ArrayList<QuestionAnswerTableColumn> result = new ArrayList<>();
        result = parseQuestions(q.getQuestions(), a.getAnswers(), result);
        return result;
    }

    private static ArrayList<QuestionAnswerTableColumn> parseQuestions(LinkedList<Question> questions, ArrayList<Answer> answers, ArrayList<QuestionAnswerTableColumn> result)
    {
        for(Question question : questions)
        {
            boolean answerFound = false;
            for(Answer answer : answers)
            {
                if(answer.getID().equals(question.getID()))
                {
                    answerFound = true;
                    result.add(new QuestionAnswerTableColumn(question, answer));
                }
            }
            if(!answerFound)
            {
                result.add(new QuestionAnswerTableColumn(question));
            }
        }
        return result;
    }

    public static String join(List<String> list, String chosenSeperator) {

        StringBuilder sb = new StringBuilder();

        String seperator = "";

        for(String s : list) {

            sb.append(seperator);
            sb.append(s);

            seperator = chosenSeperator;
        }

        return sb.toString();
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getAnswers() {
        return answers;
    }

    public String getRequired() {
        return required;
    }
}