package com.yogeshn.fyp.androidclient.backend.model.question;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.yogeshn.fyp.R;
import com.yogeshn.fyp.androidclient.backend.model.answer.SingleAnswer;

/**
 * This class defines the structure for a range question.
 * <p>
 */
@SuppressWarnings({"ResourceType", "CanBeFinal"})
public class RangeQuestion extends Question
{
	private int lowestValue, higestValue;
	private SeekBar seekbarSelector;
	private TextView textView;

	public RangeQuestion(String id, String question, int lowestValue, int higestValue, boolean required, String description)
	{
		super(id, question, required, description);
		this.lowestValue = lowestValue;
		this.higestValue = higestValue;
		this.answer = new SingleAnswer(id);
	}

	@Override
	public View getView(Context context, boolean highContrastMode) 
	{			
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(getLayoutParams());

        seekbarSelector = new SeekBar(context);
        seekbarSelector.setId(10);
        seekbarSelector.setMax(higestValue - lowestValue);
        if (!highContrastMode) {
            seekbarSelector.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_bar_style));
            seekbarSelector.setThumb(context.getResources().getDrawable(R.drawable.seekbar_thumb));
        } else {
            seekbarSelector.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_bar_style_bw));
            seekbarSelector.setThumb(context.getResources().getDrawable(R.drawable.seekbar_thumb_bw));
        }
        seekbarSelector.setPadding(30, 0, 30, 0);
        seekbarSelector.incrementProgressBy(1);
        seekbarSelector.setProgress((lowestValue + higestValue) / 2 - lowestValue);
        seekbarSelector.setLayoutParams(getLayoutParams());
        seekbarSelector.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("" + (progress + lowestValue));
            }
        });

		textView = new TextView(context);
		textView.setId(11);
		textView.setText(Integer.toString((lowestValue + higestValue) / 2));
		textView.setLayoutParams(getLayoutParams());
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
        if (!highContrastMode) {
            textView.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.black));
        }
        textView.setTextSize(context.getResources().getDimension(R.dimen.normal_text_size));

		
		linearLayout.addView(seekbarSelector);
		linearLayout.addView(textView);
		return linearLayout;
	}
	
	@Override
	protected void setAccessibility()
	{
		textView.setFocusable(true);
		textView.setNextFocusDownId(R.id.outOf);
		textView.setNextFocusUpId(seekbarSelector.getId());
		
		seekbarSelector.setFocusable(true);
		seekbarSelector.setNextFocusDownId(textView.getId());
		seekbarSelector.setNextFocusUpId(R.id.description);
	}

	@Override
	protected void getSelectedAnswer()
	{
		this.answer.addAnswer(lowestValue + Integer.toString(seekbarSelector.getProgress()));
	}

	@Override
	public boolean checkIfAnswerIsReady()
	{
		return true;
	}

	@Override
	public void loadAnswer()
	{
        if (answer.getAnswer().equals("")) {
            return;
        }
        seekbarSelector.setProgress(Integer.parseInt(answer.getAnswer()) - lowestValue);
    }

	private static LayoutParams getLayoutParams()
	{
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		return layoutParams;
	}

}
