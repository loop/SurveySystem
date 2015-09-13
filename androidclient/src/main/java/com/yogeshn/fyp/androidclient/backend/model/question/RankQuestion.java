package com.yogeshn.fyp.androidclient.backend.model.question;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.yogeshn.fyp.R;
import com.yogeshn.fyp.androidclient.backend.model.answer.MultipleAnswers;
import com.yogeshn.fyp.androidclient.backend.adapters.DynamicListView;
import com.yogeshn.fyp.androidclient.backend.adapters.StableArrayAdapter;

/**
 * This class defines the structure for a rank question.
 *
 */
@SuppressWarnings("CanBeFinal")
public class RankQuestion extends Question {
    private StableArrayAdapter adapter;
    private DynamicListView list;
    private List<String> answerOptions;

    public RankQuestion(String id, String question, List<String> answerOptions, boolean required, String description) {
        super(id, question, required, description);
        this.answer = new MultipleAnswers(id);
        this.answerOptions = answerOptions;
    }

    @Override
    public View getView(Context context, boolean highContrastMode) {
        list = new DynamicListView(context);
        if (!highContrastMode) {
            adapter = new StableArrayAdapter(context, R.layout.layout_dynamic_list_view_row, answerOptions);
        } else {
            adapter = new StableArrayAdapter(context, R.layout.layout_dynamic_list_view_row_bw, answerOptions);
        }
        list.setContentList(answerOptions);
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        return list;
    }

    @Override
    protected void getSelectedAnswer() {
        int i = 0;
        if (i < answerOptions.size()) {
            do {
                answer.addAnswer(adapter.getItem(i));
                i++;
            } while (i < answerOptions.size());
        }
    }

    @Override
    public boolean checkIfAnswerIsReady() {
        return true;
    }

    @Override
    public List<Question> getDependentQuestions(String condition) {
        List<Question> list = new LinkedList<>();
        String[] stringSplit = stringSplit(condition);
        for (int i = 0; i < stringSplit.length; i++) {
            String dependentQuestion = stringSplit[i];
            if (this.dependentQuestions.get(dependentQuestion) == null) {
                continue;
            }
            list.addAll(this.dependentQuestions.get(dependentQuestion));
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
    }

    @Override
    protected void setAccessibility() {
        list.setFocusable(true);
        list.setNextFocusDownId(R.id.outOf);
        list.setNextFocusUpId(R.id.description);
    }
}
