package model.questions;

/**
 * Abstract question class
 */

public abstract class Question {
    protected final String id;
    protected String title;
    protected String description;
    protected boolean required;
    protected final int type;
    protected String condition = "";

    /**
     * Constructor used to built a new question object.
     *
     * @param id question id
     * @param title question title
     * @param description question description
     * @param required question required to be answered
     */
    public Question(String id, String title, String description, boolean required)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.required = required;
        this.type = getClassID(this.getClass().getSimpleName());
    }

    /**
     * Updates the question details
     *
     * @param question question object to be updated
     */
    public void updateQuestion(Question question) {
        this.title = question.title;
        this.description = question.description;
        this.required = question.required;
    }

    public boolean isRequired()
    {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getID()
    {
        return this.id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    private int getClassID(String className)
    {
        switch (className) {
            case "RangeQuestion":
                return 0;
            case "MultipleChoiceQuestion":
                return 1;
            case "YesNoQuestion":
                return 2;
            case "FreeTextQuestion":
                return 3;
            case "SingleChoiceQuestion":
                return 4;
            case "RankQuestion":
                return 5;
            default:
                return -1;
        }
    }
}
