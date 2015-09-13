package model.answers;

import com.google.gson.annotations.SerializedName;

import java.util.*;

/**
 * This class is the Answer model.
 */

public class Answer {

    @SerializedName(value = "question_id")
    protected final String id;
    @SerializedName(value = "answer")
    protected final ArrayList<String> answers;

    public Answer(String id, ArrayList<String> answers)
    {
        this.id = id;
        this.answers = answers;
    }

    public String getID() { return this.id; }

    public ArrayList<String> getAnswers() { return this.answers; }

}
