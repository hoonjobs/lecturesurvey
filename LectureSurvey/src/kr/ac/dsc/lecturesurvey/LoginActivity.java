package kr.ac.dsc.lecturesurvey;

import java.util.regex.Pattern;

import kr.ac.dsc.lecturesurvey.ipc.IPC;
import kr.ac.dsc.lecturesurvey.model.User;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class LoginActivity extends Activity {

	private FrameLayout SpinnerLayout;
	private ImageView IvSpinner;
	AnimationDrawable Indicator_frameAnimation;
	
	EditText etPassword;
	EditText etEmail;	
	
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);         
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		mContext = this;
		
		setContentView(R.layout.login_activity);
		
        SpinnerLayout = (FrameLayout) findViewById(R.id.spinnerLayout);
        IvSpinner = (ImageView) findViewById(R.id.spinner_image);
		Indicator_frameAnimation = (AnimationDrawable) IvSpinner
				.getDrawable();
		
        etEmail = (EditText) findViewById(R.id.login_activity_etEmail);
		etEmail.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50) });
		
		etPassword = (EditText) findViewById(R.id.login_activity_etPassword);
		etPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
		
		ImageBtn btn_login = (ImageBtn) findViewById(R.id.login_activity_btnLogin);
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = etEmail.getText().toString();
				String password = etPassword.getText().toString();

				if(email.length() < 5 || !validEmail(email))
				{
					LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_email, new DialogInterface.OnClickListener() {
			        	public void onClick(DialogInterface dialog, int which) {
			        		// TODO Auto-generated method stub
			        		//error code 에 따라 재시도 또는 종료
			        		//app exit;
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
			        		//error code 에 따라 재시도 또는 종료
			        		//app exit;
			        		etPassword.requestFocus();
		        		}
			        } );
					return;
				}
				
				showLoadingLayer(true);
				
	    		JsonElement json = kr.ac.dsc.lecturesurvey.ipc.IPC.getInstance().requestLogin(LSApplication.gRequestHeader, email, password); 
	    		if(ResponseLogin(json))
	    		{
	    			//인증
	    			RequestInit();
	    		}

			}
		});		
	}
	
	public boolean ResponseLogin(JsonElement json)
	{
		if(json == null) return false;
		
		if (json.isJsonObject()) {
		     JsonObject jsonObject = json.getAsJsonObject();
		     JsonObject body = jsonObject.getAsJsonObject("body");

		    if(body.get("access_token") == null) return false;
		    
		    String access_token = body.get("access_token").getAsString();
			Log.i("IPC ResponseLogin","access_token:"+access_token);
	        
			LSApplication.gRequestHeader.setAccess_token(access_token);

	        return true;
		}
		return false;
	}

	private void RequestInit() {
		JsonElement responseJson = IPC.getInstance().requestInitSession(LSApplication.gRequestHeader);
		if(ResponseInit(responseJson)) {
			if(LSApplication.gUser.getUid() > 0) {
				//로그인 성공
				//go to main Activity
				finish();
				
                //Intent intent = new Intent(mContext, MainTabActivity.class);
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); //새로운 액티비티 실행~~
                overridePendingTransition(R.anim.alpha2000, R.anim.fadeout);
			}
		} else {
			//header에서 code 가 정상이 아닐 경우.
			//ErrorPopUp();
		}
	}
	
	private boolean ResponseInit(JsonElement response) {
		if (response == null)
			return false;

		if (response.isJsonObject()) {
			JsonObject jsonObject = response.getAsJsonObject();
			JsonObject body = jsonObject.getAsJsonObject("body");

			LSApplication.gUser = IPC.getInstance().getGson().fromJson(
					body, User.class);
			Log.i("IPC_Response",
					"UserInfomation/ uid:" + LSApplication.gUser.getUid());
			Log.i("IPC_Response",
					"UserInfomation/ name:" + LSApplication.gUser.getName());
			Log.i("IPC_Response",
					"UserInfomation/ deptname:" + LSApplication.gUser.getDeptname());
			Log.i("IPC_Response",
					"UserInfomation/ usertype:" + LSApplication.gUser.getUsertype());

			return true;
		}
		return false;
	}
	
	public static boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
	
	//탭,space,carriage return 을 제외한 문자열 길이를 체크후 내용이 있다고 판단시 true
	public static boolean validStringContentsLength(String chkString) {
		if(chkString.replaceAll("[\r\n\t\\p{Space}]", "").length() > 1) {
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
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition( R.anim.splashfadein, R.anim.right_out);
	}
		
}
