package com.yogeshn.fyp.androidclient.backend.model.question;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.yogeshn.fyp.R;
import com.yogeshn.fyp.androidclient.backend.model.answer.MultipleAnswers;

/**
 * This class defines the structure for a multiple choice question.
 * <p>
 */
@SuppressWarnings("CanBeFinal")
public class MultipleChoiceQuestion extends Question {

    private List<String> answerOptions;
    private List<CheckBox> options;

    public MultipleChoiceQuestion(String id, String question, List<String> answerOptions, boolean required, String description) {
        super(id, question, required, description);
        this.answer = new MultipleAnswers(id);
        this.answerOptions = answerOptions;
    }

    @Override
    public View getView(Context context, boolean highContrastMode) {
        options = new LinkedList<>();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        int i = 0;
        while (i < answerOptions.size()) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setId(i);
            checkBox.setText(answerOptions.get(i));
            if (!highContrastMode) {
                checkBox.setButtonDrawable(context.getResources().getDrawable(R.drawable.check_box_style));
                checkBox.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                checkBox.setButtonDrawable(context.getResources().getDrawable(R.drawable.check_box_style_bw));
                checkBox.setTextColor(context.getResources().getColor(R.color.black));
            }
            checkBox.setTextSize(context.getResources().getDimension(R.dimen.normal_text_size));

            options.add(checkBox);
            linearLayout.addView(checkBox);
            i++;
        }

        setAccessibility();

        return linearLayout;
    }

    @Override
    protected void setAccessibility() {
        for (Iterator<CheckBox> iterator = options.iterator(); iterator.hasNext(); ) {
            CheckBox checkBox = iterator.next();
            checkBox.setFocusable(true);
            if (options.size() <= 1) {
            } else {
                if (checkBox.getId() != 0) {
                    if (checkBox.getId() != options.size() - 1) {
                        checkBox.setNextFocusDownId(checkBox.getId() + 1);
                        checkBox.setNextFocusUpId(checkBox.getId() - 1);
                    } else {
                        checkBox.setNextFocusDownId(R.id.outOf);
                        checkBox.setNextFocusUpId(options.size() - 2);
                    }
                } else {
                    checkBox.setNextFocusDownId(1);
                    checkBox.setNextFocusUpId(R.id.description);
                }
            }
        }
    }

    @Override
    protected void getSelectedAnswer() {
        int i = 0;
        while (i < options.size()) {
            CheckBox c = options.get(i);
            if (c.isChecked()) {
                this.answer.addAnswer(c.getText().toString());
            }
            i++;
        }
    }

    @Override
    public boolean checkIfAnswerIsReady() {
        int i = 0;
        while (i < options.size()) {
            if (options.get(i).isChecked()) {
                return true;
            }
            i++;
        }
        return false;
    }

    @Override
    public List<Question> getDependentQuestions(String condition) {
        List<Question> list = new LinkedList<>();
        String[] a = stringSplit(condition);
        for (int i = 0; i < a.length; i++) {
            String anA = a[i];
            if (this.dependentQuestions.get(anA) == null) {
                continue;
            }
            list.addAll(this.dependentQuestions.get(anA));
        }
        return list;
    }

    @Override
    public boolean hasDependentQuestions(String condition) {
        String[] a = stringSplit(condition);
        for (int i = 0; i < a.length; i++) {
            String anA = a[i];
            if (!this.dependentQuestions.containsKey(anA)) {
                continue;
            }
            return true;
        }
        return false;
    }

    @Override
    public void loadAnswer() {
        String answer = this.answer.getAnswer();
        if (answer.equals("")) {
            return;
        }
        for (Iterator<CheckBox> iterator = options.iterator(); iterator.hasNext(); ) {
            CheckBox checkBox = iterator.next();
            if (answer.contains(checkBox.getText().toString()))
                checkBox.setChecked(true);
        }
    }
}
