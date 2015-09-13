package model.questionnaire;

import model.types.QuestionnaireStates;
import model.questions.Question;

import java.util.LinkedList;

/**
 * This class is the questionnaire object.
 */

public class Questionnaire
{
    private int id;
    private String title;
    private String state;
    private final LinkedList<Question> questions = new LinkedList<>();
    private static final String[] states = {QuestionnaireStates.DRAFT, QuestionnaireStates.DEPLOYED, QuestionnaireStates.ARCHIVED};

    public Questionnaire(String title)
    {
        this.title = title;
        this.state = QuestionnaireStates.DRAFT;
    }

    public Questionnaire(String title, int state)
    {
        this.title = title;
        this.state = states[state];
    }

    public Questionnaire(int id, String title, String state)
    {
        this.id = id;
        this.title = title;
        this.state = state;
    }

    public int getId()
    {
        return this.id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getState()
    {
        return this.state;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public LinkedList<Question> getQuestions()
    {
        return this.questions;
    }

    public void addQuestion(Question question)
    {
        this.questions.add(question);
    }
}
