package com.yogeshn.fyp.androidclient.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yogeshn.fyp.R;
import com.yogeshn.fyp.androidclient.androidtextspeech.Language;
import com.yogeshn.fyp.androidclient.androidtextspeech.Speaker;
import com.yogeshn.fyp.androidclient.androidtextspeech.translate.OnCompleteLoad;
import com.yogeshn.fyp.androidclient.backend.converters.JSONToObject;
import com.yogeshn.fyp.androidclient.backend.converters.ObjectToJSON;
import com.yogeshn.fyp.androidclient.backend.model.questionnaire.Questionnaire;
import com.yogeshn.fyp.androidclient.backend.connectivity.SocketAPI;
import com.yogeshn.fyp.androidclient.backend.filegeneration.AnswerFile;
import com.yogeshn.fyp.androidclient.backend.model.json.QuestionnaireJSON;
import com.yogeshn.fyp.androidclient.backend.model.question.Question;

import java.util.List;

/**
 * This class is resposible for setting up and handling all the questions of the
 * questionnaire.
 * <p>
 */
@SuppressWarnings({"deprecation", "FieldCanBeLocal", "UnnecessaryReturnStatement", "SameParameterValue", "WeakerAccess", "CanBeFinal", "ConstantConditions"})
public class QuestionActivity extends Activity implements TextToSpeech.OnInitListener {

    protected TextView question;
    protected Button speak;
    protected TextView description;
    protected int currentQuestion = 0;
    protected Questionnaire currentQuestionnaire;
    protected Context currentActivityContext = this;
    protected boolean booleanFlow = false;
    protected static boolean highContrastModeFlag = false;

    /**
     * Upon activity creation it initialises all the UI elements and sets up the
     * text to speech engine.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        question = (TextView) findViewById(R.id.question);
        description = (TextView) findViewById(R.id.description);
        speak = (Button) findViewById(R.id.speak);
        speak.setOnClickListener(view -> {
            OnCompleteLoad onCompleteLoad = () -> System.out.println("Finished speaking");
            String textToSpeak = question.getText().toString();
            if (description.getText().length() <= 0) {
                return;
            }
            textToSpeak += " " + description.getText().toString();
            Speaker.speak(textToSpeak, Language.ENGLISH, currentActivityContext, onCompleteLoad);
        });
        if (!highContrastModeFlag) {
        } else {
            highContrastMode();
        }
        setAccessibilityFocuses();
        booleanFlow = false;
    }

    /**
     * Loads the questionnaire and sets up the view for question title and description.
     */
    @Override
    public void onStart() {
        super.onStart();
        currentQuestionnaire = getQuestionnaire();
        TextView questionnaireTitle = (TextView) findViewById(R.id.questionnaireTitle);
        questionnaireTitle.setText(currentQuestionnaire.getQuestionnaireTitle());

        if (currentQuestionnaire.getNumberOfQuestions() != 0) {
            loadQuestion(currentQuestionnaire.getQuestion(currentQuestion));
        } else {
            startActivity(new Intent(QuestionActivity.this, FinishedQuestionnaireActivity.class));
            finish();
            return;
        }
    }

    /**
     * Gets the questionnaire from server with the paticular ID and parses it to a Questionnaire object.
     *
     * @return Questionnaire from server
     */
    protected Questionnaire getQuestionnaire() {
        Gson gson = new Gson();
        String questionnaire = SocketAPI.getQuestionnaireByName(AvailableQuestionnairesActivity.getQuestionnaireID());
        return !questionnaire.equals(SocketAPI.SOCKET_TIMEOUT_EXCEPTION) ? JSONToObject.createQuestionnaire(gson.fromJson(questionnaire, QuestionnaireJSON.class)) : null;
    }

    /**
     * Changes question depending on the butto npressed.
     *
     * @param view
     */
    public void changeQuestion(View view) {
        switch (view.getId()) {
            case R.id.next:
                changeQuestion(currentQuestion + 1);
                break;
            case R.id.previous:
                changeQuestion(currentQuestion - 1);
                break;
            case R.id.skip:
                skipQuestion(currentQuestion + 1);
        }

        hideKeyboard();
    }

    /**
     * Validates the answer. This method is overridden in TutorialActivity.
     * This does nothing in this patricular activity.
     *
     * @param answer
     * @return true
     */
    protected boolean validateAnswer(String answer) {
        return true;
    }

    /**
     * Changes the question depending on the action chosen.
     *
     * @param toWhichQuestion which question it needs to move to.
     */
    protected void changeQuestion(int toWhichQuestion) {
        Question q = currentQuestionnaire.getQuestion(currentQuestion);
        String lastAnswer = q.getAnswer();
        q.saveAnswer();
        String answer = q.getAnswer();

        if (answer.equals("")) {
            Toast.makeText(this, getString(R.string.answer_first), Toast.LENGTH_SHORT).show();
        } else {
            if (validateAnswer(answer)) {
                checkDependentQuestions(q, answer, lastAnswer);

                currentQuestion = toWhichQuestion;

                if (currentQuestion == currentQuestionnaire.getNumberOfQuestions()) {
                    if (!currentQuestionnaire.isCompleted()) {
                        Toast.makeText(this, R.string.answer_question, Toast.LENGTH_SHORT).show();
                        skipQuestion((currentQuestion = currentQuestionnaire.getFirstRequiredQuestionToComplete()));
                    } else {
                        int index = currentQuestionnaire.getFirstNonRequiredQuestionToComplete();
                        if (index == -1) {
                            sendCompletedQuestionnaireAnswers();
                        } else {
                            showOptionDialog(index);
                        }
                        showTutorialQuestionPart();
                    }
                } else {
                    loadQuestion(currentQuestionnaire.getQuestion(currentQuestion));
                }
            } else {
                Toast.makeText(this, getString(R.string.answer_not_correct), Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * Sends the answer to be created to a JSON object which is sent back to server
     * and loads the ThankYou activity.
     */
    protected void sendCompletedQuestionnaireAnswers() {
        String response = SocketAPI.sendAnswersToServer(ObjectToJSON.createJSON(currentQuestionnaire));

        switch (response) {
            case SocketAPI.SOCKET_TIMEOUT_EXCEPTION:
                AnswerFile.writeFile(this, ObjectToJSON.createJSON(currentQuestionnaire));
                break;
            default:
                SocketAPI.close();
                break;
        }

        startActivity(new Intent(this, FinishedQuestionnaireActivity.class));
        finish();
        currentQuestionnaire.deleteQuestionnaire();
    }

    /**
     * Creates the dialog to inform user if they wish to complete uncompleted questions.
     *
     * @param questions index of not completed question
     */
    protected void showOptionDialog(final int questions) {
        new Builder(this).setCancelable(false).setMessage(R.string.option_dialog).setNegativeButton(R.string.no, (dialog, which) -> {
            sendCompletedQuestionnaireAnswers();
            dialog.cancel();
        }).setPositiveButton(R.string.yes, (dialog, which) -> {
            skipQuestion((currentQuestion = questions));
            dialog.cancel();
        }).create().show();
    }

    /**
     * Action to skip the question
     *
     * @param skipIndex the current question index to skip
     */
    protected void skipQuestion(int skipIndex) {
        currentQuestion = skipIndex;
        loadQuestion(currentQuestionnaire.getQuestion(currentQuestion));

        hideKeyboard();
    }

    /**
     * Loads a question and displays it to the user.
     *
     * @param question Question to be loaded and displayed.
     */
    protected void loadQuestion(Question question) {
        questionProgress();
        TextView questionTextView = (TextView) findViewById(R.id.question);
        RelativeLayout answer = (RelativeLayout) findViewById(R.id.answer);
        TextView description = (TextView) findViewById(R.id.description);
        Button previous = (Button) findViewById(R.id.previous);

        Button skip = (Button) findViewById(R.id.skip);
        answer.removeAllViews();
        answer.addView(question.getView(this, highContrastModeFlag));
        description.setText(question.getDescription());
        questionTextView.setText(question.getQuestion());
        question.loadAnswer();
        Button next = (Button) findViewById(R.id.next);

        next.setText(currentQuestion != currentQuestionnaire.getNumberOfQuestions() - 1 ? getResources().getString(R.string.next_question) : getResources().getString(R.string.submit_answers));
        previous.setVisibility(currentQuestion != 0 ? View.VISIBLE : View.GONE);
        skip.setVisibility(!question.isRequired() && currentQuestion != currentQuestionnaire.getNumberOfQuestions() - 1 ? View.VISIBLE : View.GONE);
        showTutorialQuestionPart();
    }

    /**
     * @see com.yogeshn.fyp.androidclient.activities.TutorialQuestionnaireActivity
     */
    protected void showTutorialQuestionPart() {
        //Do nothing.
    }

    /**
     * Shows and updates the progress of up coming and completed questions.
     */
    protected void questionProgress() {
        TextView outOf = (TextView) findViewById(R.id.outOf);
        outOf.setText("" + (currentQuestion + 1) + "/" + currentQuestionnaire.getNumberOfQuestions());

        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
        p.setMax(currentQuestionnaire.getNumberOfQuestions());
        p.setProgress(currentQuestion + 1);

        ListView list = (ListView) findViewById(R.id.questionsList);
        list.setAdapter(new SideListAdapter(this, currentQuestionnaire.getQuestions()));
    }

    /**
     * Backbutton presses takes to previous quesiton or to the login activity
     * if question is already the first one.
     */
    @Override
    public void onBackPressed() {
        if (currentQuestion <= 0) {
            Intent intent = new Intent(this, LoginPatientDetailActivity.class);
            startActivity(intent);
        } else {
            changeQuestion(currentQuestion - 1);
        }
    }

    /**
     * Once activity has been stopped it will clear this activity and reload the connect activity.
     */
    @Override
    public void onStop() {
        super.onStop();
        Intent i = new Intent(this, ConnectActivity.class);
        if (currentQuestionnaire != null) {
            if (currentQuestionnaire.getNumberOfQuestions() != 0) {
                if (currentQuestion == currentQuestionnaire.getNumberOfQuestions()) {
                    return;
                }
                i.putExtra(ConnectActivity.NO_CONNECTION_DIALOG, false);
                i.putExtra(ConnectActivity.SAME_ACTIVITY, true);
                startActivity(i);
                return;
            } else {
                return;
            }
        } else {
            if (!booleanFlow) {
                i.putExtra(ConnectActivity.NO_CONNECTION_DIALOG, true);
                i.putExtra(ConnectActivity.SAME_ACTIVITY, true);
                startActivity(i);
                booleanFlow = true;
            } else {
                return;
            }
        }
    }

    /**
     * For depednt questions it checks if answers has been changed and updates the dependee
     * questions.
     *
     * @param dependentQuestion depedent question
     * @param answer            the current answer
     * @param previousAnswer    the previous answer
     */
    protected void checkDependentQuestions(Question dependentQuestion, String answer, String previousAnswer) {
        if (dependentQuestion.hasDependentQuestions(answer)) {
            currentQuestionnaire.addQuestions(dependentQuestion.getDependentQuestions(answer), currentQuestion);
        }

        if (!dependentQuestion.hasDependentQuestions(previousAnswer)) {
        } else {
            currentQuestionnaire.removeQuestions(dependentQuestion.getDependentQuestions(previousAnswer));
        }

        if (answer.equals(previousAnswer)) {
            return;
        }
        return;

    }

    /**
     * Loads the high contrast UI.
     */
    protected void highContrastMode() {
        Resources r = getResources();
        findViewById(R.id.questionRoot).setBackgroundColor(r.getColor(R.color.white));
        findViewById(R.id.previous).setBackgroundDrawable(r.getDrawable(R.color.button_background_color_bw));
        findViewById(R.id.skip).setBackgroundDrawable(r.getDrawable(R.color.button_background_color_bw));
        findViewById(R.id.next).setBackgroundDrawable(r.getDrawable(R.color.button_background_color_bw));
        findViewById(R.id.speak).setBackgroundDrawable(r.getDrawable(R.color.button_background_color_bw));
        ((TextView) findViewById(R.id.questionnaireTitle)).setTextColor(r.getColor(R.color.black));
        ((TextView) findViewById(R.id.description)).setTextColor(r.getColor(R.color.black));
        ((TextView) findViewById(R.id.question)).setTextColor(r.getColor(R.color.black));
        ((Button) findViewById(R.id.previous)).setTextColor(r.getColor(R.color.text_color_bw));
        ((Button) findViewById(R.id.skip)).setTextColor(r.getColor(R.color.text_color_bw));
        ((Button) findViewById(R.id.next)).setTextColor(r.getColor(R.color.text_color_bw));
        ((Button) findViewById(R.id.speak)).setTextColor(r.getColor(R.color.text_color_bw));
        ((ProgressBar) findViewById(R.id.progressBar1)).setProgressDrawable(r.getDrawable(R.drawable.progress_bar_style_bw));
        ((TextView) findViewById(R.id.outOf)).setTextColor(r.getColor(R.color.black));
    }

    /**
     * Updates the accessbility.
     */
    protected void setAccessibilityFocuses() {
        View questionnaireTitle = findViewById(R.id.questionnaireTitle);
        View questionsList = findViewById(R.id.questionsList);
        View questionTitle2 = findViewById(R.id.question);
        View description = findViewById(R.id.description);
        View answer = findViewById(R.id.answer);
        View questionsTotal = findViewById(R.id.outOf);
        View previousQuestion = findViewById(R.id.previous);
        View skipQuestion = findViewById(R.id.skip);
        View nextQuestion = findViewById(R.id.next);

        questionnaireTitle.setNextFocusDownId(R.id.question);
        questionnaireTitle.setNextFocusLeftId(R.id.questionsList);
        questionsList.setNextFocusRightId(R.id.questionnaireTitle);
        questionTitle2.setNextFocusDownId(R.id.description);
        questionTitle2.setNextFocusUpId(R.id.questionnaireTitle);
        description.setNextFocusDownId(R.id.answer);
        description.setNextFocusUpId(R.id.question);
        answer.setNextFocusDownId(R.id.outOf);
        answer.setNextFocusUpId(R.id.description);
        questionsTotal.setNextFocusUpId(R.id.answer);
        previousQuestion.setNextFocusRightId(findViewById(R.id.skip).getVisibility() != View.VISIBLE ? R.id.next : R.id.skip);
        previousQuestion.setNextFocusUpId(R.id.outOf);
        skipQuestion.setNextFocusRightId(R.id.next);

    }

    /**
     * Hides the keyboard.
     */
    protected void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Checks if high contrast mode is on or off.
     *
     * @return tue if high contrast is on
     */
    public static boolean getHighContrastModeFlag() {
        return highContrastModeFlag;
    }

    /**
     * Sets high contrast mode on or off.
     *
     * @param newHighContrastMode true to turn on, false to turn off.
     */
    public static void setHighContrastModeFlag(boolean newHighContrastMode) {
        highContrastModeFlag = newHighContrastMode;
    }

    /**
     * Does nothing on text to speech initialisation.
     *
     * @param i
     */
    @Override
    public void onInit(int i) {
        //Do nothing.
    }

    class SideListAdapter extends ArrayAdapter<Question> {
        private Context context;

        private List<Question> questions;

        public SideListAdapter(Context context, List<Question> questions) {
            super(context, R.layout.layout_side_list_row, questions);
            this.context = context;
            this.questions = questions;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.layout_side_list_row, parent, false);

            Question question1 = questions.get(position);

            TextView user = (TextView) row.findViewById(R.id.sideListRow);
            user.setText(question1.getQuestion());
            if (!question1.isAnswered()) {
            } else {
                user.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            if (QuestionActivity.getHighContrastModeFlag()) {
                user.setTextColor(context.getResources().getColor(R.color.black));

                return row;
            } else {
                return row;
            }
        }
    }
}