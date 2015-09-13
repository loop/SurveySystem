package com.yogeshn.fyp.androidclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.yogeshn.fyp.R;

/**
 * This activity is displayed after the user has completed a quesitonnaire and uses a countdown timer.
 * <p>
 */
public class FinishedQuestionnaireActivity extends Activity {

    protected TextView activityClosing, thankYouText;

    /**
     * Loads a simple thank you message with patient name and autocloses and loads the
     * patient screen in 5 seconds.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        thankYouText = (TextView) findViewById(R.id.thankYou);
        activityClosing = (TextView) findViewById(R.id.autoClose);

        thankYouText.setText(LoginPatientDetailActivity.getPatientName() + getResources().getString(R.string.thank_you));
        activityClosing.setText(getString(R.string.auto_close_1) + " 5 " + getString(R.string.auto_close_2));

        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                activityClosing.setText(getString(R.string.auto_close_1) + " " + (millisUntilFinished / 1000) + " " + getString(R.string.auto_close_2));
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(FinishedQuestionnaireActivity.this, ConnectActivity.class));
                finish();
                LoginPatientDetailActivity.clearPatientData();
            }
        }.start();
    }

    /**
     * Back press does nothing as questionnaire has already been answered.
     */
    @Override
    public void onBackPressed() {
        //Do nothing.
    }

}