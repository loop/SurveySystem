package model.answers;

import model.questionnaire.Questionnaire;
import model.questions.Question;
import com.google.gson.annotations.SerializedName;

import java.util.*;

/**
 * This class is for the set of answers for a questionnaire.
 */

public class AnswerSet
{
    @SerializedName(value = "questionnaire_id")
    private final int questionnaireID;
    @SerializedName(value = "patient_id")
    private final String patientNHS;
    private final ArrayList<Answer> answers;

    public AnswerSet(int questionnaireID, String patientNHS, ArrayList<Answer> answers)
    {
        this.questionnaireID = questionnaireID;
        this.patientNHS = patientNHS;
        this.answers = answers;
    }

    public int getQuestionnaireID() {
        return questionnaireID;
    }

    public String getPatientNHS() {
        return patientNHS;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public boolean isAnswerComplete(Questionnaire questionnaire)
    {
        LinkedList<Question> questions = questionnaire.getQuestions();

        for (Iterator<Question> iterator = questions.iterator(); iterator.hasNext(); ) {
            Question q = iterator.next();
            if (!q.isRequired()) {
            } else {
                boolean answerFound = false;
                for (Answer a : answers) {
                    if (q.getID().equals(a.getID())) {
                        answerFound = true;
                    }
                }
                if (!answerFound) {
                    return false;
                }
            }
        }
        return true;
    }
}
