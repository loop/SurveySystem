package model.questions;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is the Yes/No question object.
 */

public class YesNoQuestion extends SingleChoiceQuestion {
    private static final List<String> answerOptions = new ArrayList<String>() {{add("Yes"); add("No");}};

    public YesNoQuestion(String id, String title, String description, boolean required)
    {
        super(id, title, description, required, answerOptions);
    }

}
