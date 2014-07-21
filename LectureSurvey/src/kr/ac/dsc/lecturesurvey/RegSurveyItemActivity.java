package kr.ac.dsc.lecturesurvey;

import java.util.regex.Pattern;

import kr.ac.dsc.lecturesurvey.ipc.IPC;
import kr.ac.dsc.lecturesurvey.model.Survey;
import kr.ac.dsc.lecturesurvey.model.SurveyItem;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.JsonElement;

public class RegSurveyItemActivity extends Activity {

	private FrameLayout SpinnerLayout;
	private ImageView IvSpinner;
	AnimationDrawable Indicator_frameAnimation;

	private Survey mSurvey;
	private SurveyItem mSurveyItem;
	private EditText etQuestion;
	
	private boolean updateMode;
	
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mContext = this;
		
		setContentView(R.layout.reg_survey_item_activity);
		
		// get Survey Data ////////////
		Intent intent = getIntent();
		mSurvey = ((Survey) intent.getSerializableExtra("survey"));
		if (mSurvey == null)
			finish();
		
		mSurveyItem = ((SurveyItem) intent.getSerializableExtra("surveyItem"));
		if(mSurveyItem != null) updateMode = true;
		else updateMode = false;
		
        SpinnerLayout = (FrameLayout) findViewById(R.id.spinnerLayout);
        IvSpinner = (ImageView) findViewById(R.id.spinner_image);
		Indicator_frameAnimation = (AnimationDrawable) IvSpinner
				.getDrawable();
		
		showLoadingLayer(false);
		
		ImageBtn btn_postSurvey = (ImageBtn)findViewById(R.id.reg_survey_item_post);
		btn_postSurvey.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doPostSurveyItem();
			}
		});
		
		etQuestion = (EditText) findViewById(R.id.reg_survey_item_etQuestion);
		
		if(mSurveyItem != null) etQuestion.setText(mSurveyItem.getQuestion());
	}
	
	public void doPostSurveyItem()
	{
		Log.i(getClass().getSimpleName(), "doPostSurveyItem");

		String surveyItemQuestion = etQuestion.getText().toString();
		
		if(surveyItemQuestion.length() < 1)
		{				
			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_question, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	        		etQuestion.requestFocus();
        		}
	        } );
			return;
		}
		
		showLoadingLayer(true);

		SurveyItem surveyItem = new SurveyItem(mSurveyItem.getIdx(), mSurvey.getIdx(), surveyItemQuestion);
		
		new GetDataTask().execute(surveyItem);
	}
	
	private class GetDataTask extends AsyncTask<SurveyItem, Void, Boolean> {

		@Override
		protected Boolean doInBackground(SurveyItem... params) {
			if(updateMode)
				return RequestPut(params[0]);
			else 
				return RequestPost(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			showLoadingLayer(false);

			if (result) {
				// 등록성공
				LSApplication.ErrorPopup(mContext,
						R.string.popup_alert_title_info, R.string.success_save,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								setResult(RESULT_OK);
								finish();
								overridePendingTransition(R.anim.splashfadein,
										R.anim.right_out);
							}
						});
			} else {
				ErrorPopUp();
			}
		}
	}
	
	private boolean RequestPost(SurveyItem surveyItem) {
		JsonElement responseJson = IPC.getInstance().requestSurveyItemPost(LSApplication.gRequestHeader, surveyItem); 
		if(ResponseSurveyItemPost(responseJson)) {
			return true;
		}
		return false;
	}
	
	//수정
	private boolean RequestPut(SurveyItem surveyItem) {
		JsonElement responseJson = IPC.getInstance().requestSurveyItemPut(LSApplication.gRequestHeader, surveyItem); 
		if(ResponseSurveyItemPost(responseJson)) {
			return true;
		}
		return false;
	}
	
	private boolean ResponseSurveyItemPost(JsonElement json)
	{
		if(json == null) return false;
		
		if (json.isJsonObject()) {
	        return true;
		}
		return false;
	}	
	
	public void ErrorPopUp() {
		LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, IPC
				.getInstance().getLastResponseErrorMsg(), null);
	}
	
	public void showLoadingLayer(boolean bShow)
	{
		if(bShow) {
			SpinnerLayout.setVisibility(View.VISIBLE);
	    	Indicator_frameAnimation.start();
		}
		else	{
			Indicator_frameAnimation.stop();
			SpinnerLayout.setVisibility(View.GONE);    	
		}		
	}
	
	public boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
	
	//탭,space,carriage return 을 제외한 문자열 길이를 체크후 내용이 있다고 판단시 true
	public boolean validStringContentsLength(String chkString) {
		if(chkString.replaceAll("[\r\n\t\\p{Space}]", "").length() > 1) {
			return true;
		}
		return false;
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition( R.anim.splashfadein, R.anim.right_out);
	}
		
}
