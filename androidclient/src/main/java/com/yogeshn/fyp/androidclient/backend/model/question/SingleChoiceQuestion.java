package com.yogeshn.fyp.androidclient.backend.model.question;

import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yogeshn.fyp.R;
import com.yogeshn.fyp.androidclient.backend.model.answer.SingleAnswer;

/**
 * This class defines the structure for a single choice question.
 *
 */
@SuppressWarnings("CanBeFinal")
public class SingleChoiceQuestion extends Question {
    private List<String> answerOptions;
    private SparseArray<RadioButton> options;
    private RadioGroup radioGroup;

    public SingleChoiceQuestion(String id, String question, List<String> answerOptions, boolean required, String description) {
        super(id, question, required, description);
        this.answer = new SingleAnswer(id);
        this.answerOptions = answerOptions;
    }

    @Override
    public View getView(Context context, boolean highContrastMode) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        radioGroup = new RadioGroup(context);
        options = new SparseArray<>();

        int i = 0;
        while (i < answerOptions.size()) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(answerOptions.get(i));
            if (!highContrastMode) {
                radioButton.setButtonDrawable(context.getResources().getDrawable(R.drawable.radio_button_style));
                radioButton.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                radioButton.setButtonDrawable(context.getResources().getDrawable(R.drawable.radio_button_style_bw));
                radioButton.setTextColor(context.getResources().getColor(R.color.black));
            }
            radioButton.setTextSize(context.getResources().getDimension(R.dimen.normal_text_size));

            radioGroup.addView(radioButton);
            options.put(radioButton.getId(), radioButton);
            i++;
        }

        linearLayout.addView(radioGroup);
        return linearLayout;
    }

    @Override
    protected void getSelectedAnswer() {
        RadioButton radioButton = options.get(radioGroup.getCheckedRadioButtonId());
        if (radioButton == null) {
            return;
        }
        this.answer.addAnswer(radioButton.getText().toString());
    }

    @Override
    public boolean checkIfAnswerIsReady() {
        return radioGroup.getCheckedRadioButtonId() != -1;
    }

    @Override
    public void loadAnswer() {
        String answer = this.answer.getAnswer();
        if (answer.equals("")) {
            return;
        }
        int i = 0;
        if (i < options.size()) {
            do {
                if (options.get(options.keyAt(i)).getText().toString().equals(answer)) {
                    options.get(options.keyAt(i)).setChecked(true);
                    break;
                } else {
                    i++;
                }
            } while (i < options.size());
        }
    }

    @Override
    protected void setAccessibility() {
        int i = 0;
        while (i < options.size()) {
            RadioButton radioButton = options.get(i);
            radioButton.setFocusable(true);
            if (options.size() > 1) {
                if (radioButton.getId() != 0) {
                    if (radioButton.getId() != options.size() - 1) {
                        radioButton.setNextFocusDownId(radioButton.getId() + 1);
                        radioButton.setNextFocusUpId(radioButton.getId() - 1);
                    } else {
                        radioButton.setNextFocusDownId(R.id.outOf);
                        radioButton.setNextFocusUpId(options.size() - 2);
                    }
                } else {
                    radioButton.setNextFocusDownId(1);
                    radioButton.setNextFocusUpId(R.id.description);
                }
            }
            i++;
        }
    }
}
