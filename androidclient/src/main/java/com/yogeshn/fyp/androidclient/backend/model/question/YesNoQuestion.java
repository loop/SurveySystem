package com.yogeshn.fyp.androidclient.backend.model.question;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines the structure for the Yes/No question.
 *
 */
public class YesNoQuestion extends SingleChoiceQuestion
{
    @SuppressWarnings("serial")
    private static final List<String> answerOptions;

    static {
        answerOptions = new ArrayList<String>() {{
            add("Yes");
            add("No");
        }};
    }

    public YesNoQuestion(String id, String question, boolean required, String description)
	{
		super(id, question, answerOptions, required, description);
	}
}
