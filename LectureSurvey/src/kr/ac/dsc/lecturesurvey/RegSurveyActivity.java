package kr.ac.dsc.lecturesurvey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

import kr.ac.dsc.lecturesurvey.ipc.IPC;
import kr.ac.dsc.lecturesurvey.model.Survey;

import com.google.gson.JsonElement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class RegSurveyActivity extends Activity {

	private FrameLayout SpinnerLayout;
	private ImageView IvSpinner;
	AnimationDrawable Indicator_frameAnimation;

	private EditText etLectureName, etLectureDept, etSurveyMsg;
	private TextView tvLectureDate;
	
	private int mYear, mMonth, mDay, mHour, mMinute;
	
	GregorianCalendar mCalendar;
	
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mContext = this;
		
		setContentView(R.layout.reg_survey_activity);
		
        SpinnerLayout = (FrameLayout) findViewById(R.id.spinnerLayout);
        IvSpinner = (ImageView) findViewById(R.id.spinner_image);
		Indicator_frameAnimation = (AnimationDrawable) IvSpinner
				.getDrawable();
		
		showLoadingLayer(false);
		
		ImageBtn btn_postSurvey = (ImageBtn)findViewById(R.id.reg_survey_post);
		btn_postSurvey.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doPostSurvey();
			}
		});
		
		etLectureName = (EditText) findViewById(R.id.reg_survey_etLectureName);
		etLectureDept = (EditText) findViewById(R.id.reg_survey_etLectureDept);
		tvLectureDate = (TextView) findViewById(R.id.reg_survey_tvLectureDate);
		etSurveyMsg = (EditText) findViewById(R.id.reg_survey_etSurveyMsg);
		
		etLectureName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100) }); 
		etLectureDept.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50) });
		
		Button btnLectureDateSel = (Button) findViewById(R.id.reg_survey_btnLectureDate);
		btnLectureDateSel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(RegSurveyActivity.this, mDateSetListener, mYear, mMonth, mDay).show();
			}
		});
		
		mCalendar = new GregorianCalendar();
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
		mMinute = mCalendar.get(Calendar.MINUTE);
	}
	
	private void updateDisplay()
	{
		mCalendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.KOREA);
		
		String time = fmt.format(mCalendar.getTime());
		tvLectureDate.setText(time);

		Toast.makeText(RegSurveyActivity.this, time, Toast.LENGTH_SHORT).show();
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			new TimePickerDialog(RegSurveyActivity.this, mTimeSetListener, mHour, mMinute, false).show();			
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}
	};

	public void doPostSurvey()
	{
		Log.i(getClass().getSimpleName(), "doRegisterAccount");

		String lectureName = etLectureName.getText().toString();
		String lectureDept = etLectureDept.getText().toString();

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		String lectureDate = fmt.format(mCalendar.getTime());
		String surveyMsg = etSurveyMsg.getText().toString();
		
		if(lectureName.length() < 1)
		{
			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_lectureName, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	        		etLectureName.requestFocus();
        		}
	        } );
			return;
		}
		
		if(lectureDept.length() < 1)
		{
			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_lectureName, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	        		etLectureDept.requestFocus();
        		}
	        } );
			return;
		}
		
		if(lectureDate.length() < 1)
		{				
			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_lectureDate, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	        		tvLectureDate.requestFocus();
        		}
	        } );
			return;
		}
		
		showLoadingLayer(true);

		Survey survey = new Survey(0, lectureName, lectureDate, lectureDept, LSApplication.gUser.getName(), 0, surveyMsg, false);
		JsonElement responseJson = IPC.getInstance().requestSurveyPost(LSApplication.gRequestHeader, survey); 
		if(ResponseSurveyPost(responseJson)) {
			//등록성공
			showLoadingLayer(false);

			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.success_save, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	    			setResult(RESULT_OK);
	    			finish();
	    			overridePendingTransition( R.anim.splashfadein, R.anim.right_out);
        		}
	        } );
			
		}
		
		showLoadingLayer(false);
	}
	
	public boolean ResponseSurveyPost(JsonElement json)
	{
		if(json == null) return false;
		
		if (json.isJsonObject()) {
	        return true;
		}
		return false;
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
