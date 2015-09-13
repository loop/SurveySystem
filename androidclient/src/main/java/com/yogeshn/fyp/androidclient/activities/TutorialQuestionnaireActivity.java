package com.yogeshn.fyp.androidclient.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;

import com.google.gson.Gson;
import com.yogeshn.fyp.R;
import com.yogeshn.fyp.androidclient.backend.converters.JSONToObject;
import com.yogeshn.fyp.androidclient.backend.model.questionnaire.Questionnaire;
import com.yogeshn.fyp.androidclient.backend.model.json.QuestionnaireJSON;

/**
 * This activity is reposible for the tutorial questionnaire and validates the answers.
 * <p>
 */
public class TutorialQuestionnaireActivity extends QuestionActivity {
    protected boolean notShowed = true;
    AlertDialog alertDialog = null;


    @Override
    public void onResume() {
        super.onResume();
        if (!notShowed) {
            return;
        }
        showDialog(getString(R.string.step_start));
        notShowed = false;
    }

    @Override
    protected Questionnaire getQuestionnaire() {
        Gson gson = new Gson();
        return JSONToObject.createQuestionnaire(gson.fromJson(readTutorialQuestionnaire(),
                QuestionnaireJSON.class));
    }

    @Override
    protected void sendCompletedQuestionnaireAnswers() {
        startActivity(new Intent(this, AvailableQuestionnairesActivity.class));
        finish();
    }

    protected boolean validateAnswer(String answer) {
        String id = currentQuestionnaire.getQuestion(currentQuestion).getID();

        return !(id.equals("tq2") && !answer.equals("Peanut Butter, Jam") || id.equals("tq3") && !answer.equals("February") || id.equals("tq4") && !answer.equals("28, 29") || id.equals("tq5") && !answer.equals("2") || id.equals("tq6") && currentQuestionnaire.getQuestion(currentQuestion).isAnswered() && !answer.equals("8") || id.equals("tq7") && !answer.equals("Monday, Tuesday, Wednesday, Thursday, Friday")) && !(id.equals("tq8") && !answer.toLowerCase(Locale.getDefault()).equalsIgnoreCase(LoginPatientDetailActivity.getPatientName()));
    }

    @Override
    protected void showTutorialQuestionPart() {
        switch (currentQuestion) {
            case 0:
                showDialog(getString(R.string.step_0));
                break;
            case 1:
                showDialog(getString(R.string.step_1));
                break;
            case 2:
                showDialog(getString(R.string.step_2));
                break;
            case 3:
                showDialog(getString(R.string.step_3));
                break;
            case 4:
                showDialog(getString(R.string.step_4));
                break;
            case 5:
                showDialog(getString(R.string.step_5));
                break;
            case 6:
                showDialog(getString(R.string.step_6));
                break;
            case 7:
                showDialog(getString(R.string.step_7));
                break;
            case 8:
                showDialog(getString(R.string.step_end));
                break;
            default:
                break;
        }
    }

    /**
     * Reads a JSON file containing the tutorial.
     *
     * @return JSON parsed QuestionnaireJSON object.
     */
    protected String readTutorialQuestionnaire() {
        String result = "";
        String s;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(getAssets().open("tutorial_questionnaire.json")));

            if ((s = in.readLine()) != null) {
                do result += s.trim();
                while ((s = in.readLine()) != null);
            }

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    protected void showDialog(String text) {

        Builder builder = new Builder(this);
        builder.setMessage(text).setCancelable(false).setPositiveButton(getString(R.string.ok_let_me_start), (dialog1, which) -> dialog1.dismiss());

        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        alertDialog.dismiss();
    }
}
