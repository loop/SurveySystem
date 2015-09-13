package com.yogeshn.fyp.androidclient.backend.model.question;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

import com.yogeshn.fyp.R;
import com.yogeshn.fyp.androidclient.backend.model.answer.SingleAnswer;

/**
 * This class defines the structure for the free text question.
 *
 */
@SuppressWarnings("deprecation")
public class FreeTextQuestion extends Question
{
	private EditText editText;

	public FreeTextQuestion(String id, String question, boolean required, String description)
	{
		super(id, question, required, description);
		this.answer = new SingleAnswer(id);
	}

	@Override
	public View getView(final Context context, boolean highContrastMode) 
	{
        editText = new EditText(context);
        editText.requestFocus();
        editText.setHint(context.getString(R.string.answer_here_hint));
        editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        editText.setSingleLine(false);
        editText.setBackgroundDrawable(context.getResources().getDrawable(android.R.drawable.editbox_background_normal));
        editText.setTextSize(context.getResources().getDimension(R.dimen.normal_text_size));
        editText.setPadding(20, 20, 20, 20);
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

		return editText;
	}

	@Override
	protected void getSelectedAnswer()
	{
		this.answer.addAnswer(editText.getText().toString());
	}

	@Override
	public boolean checkIfAnswerIsReady()
	{
        return !editText.getText().toString().trim().equals("");
    }

	@Override
	public void loadAnswer() 
	{
		editText.setText(answer.getAnswer());
	}

	@Override
	protected void setAccessibility()
	{
		editText.setFocusable(true);
		editText.setNextFocusDownId(R.id.outOf);
		editText.setNextFocusUpId(R.id.description);
	}

}