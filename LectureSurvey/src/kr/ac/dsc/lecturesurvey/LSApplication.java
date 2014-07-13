package kr.ac.dsc.lecturesurvey;

import kr.ac.dsc.lecturesurvey.ipc.IPCHeader;
import kr.ac.dsc.lecturesurvey.model.User;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class LSApplication extends Application {

	//App의 Context를 갖고 있는다.
	public static Context gContext;
	public static IPCHeader gRequestHeader;
	public static User gUser;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		gContext =  this;
		
		gRequestHeader = new IPCHeader();
		gUser = new User();
		
		getSharedPreferences();
	}
	
	public void getSharedPreferences() {
		SharedPreferences prefs = getSharedPreferences("lecture_survey_sharedpref", MODE_PRIVATE);
		gRequestHeader.setAccess_token(prefs.getString("acces_token", ""));
	}
	
	public void setSharedPreferences() {
		SharedPreferences prefs = getSharedPreferences("lecture_survey_sharedpref", MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("acces_token", gRequestHeader.getAccess_token());
		edit.commit();
	}
	
	public static void ErrorPopup(Context context, int titleResId, int messageResId, DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(titleResId);
		ad.setMessage(messageResId);
		ad.setPositiveButton(R.string.popup_alert_title_info, listener);
		ad.show();
	}	
	
}
