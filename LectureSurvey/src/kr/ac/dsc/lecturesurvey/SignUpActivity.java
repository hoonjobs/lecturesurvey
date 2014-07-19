package kr.ac.dsc.lecturesurvey;

import java.util.regex.Pattern;



import kr.ac.dsc.lecturesurvey.LSApplication;
import kr.ac.dsc.lecturesurvey.ipc.IPC;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class SignUpActivity extends Activity {

	private FrameLayout SpinnerLayout;
	private ImageView IvSpinner;
	AnimationDrawable Indicator_frameAnimation;

	private EditText etName, etEmail, etPassword, etDeptName, etStudentID;
	
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);         
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);		
		
		setContentView(R.layout.sign_up_activity);
		
		mContext = this;
		
        SpinnerLayout = (FrameLayout) findViewById(R.id.spinnerLayout);
        IvSpinner = (ImageView) findViewById(R.id.spinner_image);
		Indicator_frameAnimation = (AnimationDrawable) IvSpinner
				.getDrawable();
		
		showLoadingLayer(false);
		
		ImageBtn btn_register = (ImageBtn)findViewById(R.id.sign_up_btnRegister);
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doRegisterAccount();
				//showLoadingLayer(true);
			}
		});
		
		etName = (EditText) findViewById(R.id.sign_up_etName);
		etDeptName = (EditText) findViewById(R.id.sign_up_etDept);
		etStudentID = (EditText) findViewById(R.id.sign_up_etStudentID);
		etEmail = (EditText) findViewById(R.id.sign_up_etEmail);
		etPassword = (EditText) findViewById(R.id.sign_up_etPassword);
		
		etName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) }); 
		etDeptName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50) }); 
		etStudentID.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) }); 
		etEmail.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50) }); 
		etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etPassword.setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etPassword.setTransformationMethod(new PasswordTransformationMethod());
		etPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
		
	}
	
	public void doRegisterAccount()
	{
		Log.i(getClass().getSimpleName(), "doRegisterAccount");

		String name = etName.getText().toString();
		String dept = etDeptName.getText().toString();
		String studentID = etStudentID.getText().toString();
		String email = etEmail.getText().toString();
		String password = etPassword.getText().toString();
		
		if(name.length() < 1)
		{
			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_name, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	        		etName.requestFocus();
        		}
	        } );
			return;
		}
		
		if(dept.length() < 1)
		{
			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_deptName, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	        		etName.requestFocus();
        		}
	        } );
			return;
		}
		
		if(studentID.length() < 1)
		{
			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_studentID, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	        		etName.requestFocus();
        		}
	        } );
			return;
		}
		
		if(email.length() < 5 || !validEmail(email))
		{
			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_email, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	        		etEmail.requestFocus();
        		}
	        } );
			return;
		}
		
		if(password.length() < 6)
		{				
			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_pw, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	        		etPassword.requestFocus();
        		}
	        } );
			return;
		}
		
		showLoadingLayer(true);
		
		new GetDataTask().execute(name, dept, studentID, email, password);

		
	}
	
	private class GetDataTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return RequestSignUp(params[0], params[1], params[2], params[3], params[4]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			showLoadingLayer(false);
			
			if(result) {
				//로그인 성공
				//access_token sharedPreferences 에 저장
				SharedPreferences prefs = getSharedPreferences("lecture_survey_sharedpref", MODE_PRIVATE);
				SharedPreferences.Editor edit = prefs.edit();
				edit.putString("acces_token", LSApplication.gRequestHeader.getAccess_token());
				edit.commit();
				
				setResult(Activity.RESULT_OK);
				finish();
			} else {
				ErrorPopUp();
			}
		}
	}
	
	public boolean RequestSignUp(String name, String dept, String studentID, String email, String password) {
		JsonElement json = kr.ac.dsc.lecturesurvey.ipc.IPC.getInstance().requestSignUp(LSApplication.gRequestHeader, name, dept, studentID, email, password); 
		if(ResponseSignUp(json))
		{
			return true;
		}
		return false;
	}
	
	public boolean ResponseSignUp(JsonElement json)
	{
		if(json == null) return false;
		
		if (json.isJsonObject()) {
		     JsonObject jsonObject = json.getAsJsonObject();
		     JsonObject body = jsonObject.getAsJsonObject("body");

		    if(body.get("access_token") == null) return false;
		    
		    String access_token = body.get("access_token").getAsString();
			Log.i("IPC_Response","access_token:"+access_token);
	        
			LSApplication.gRequestHeader.setAccess_token(access_token);

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
