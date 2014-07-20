package kr.ac.dsc.lecturesurvey;

import kr.ac.dsc.lecturesurvey.ipc.IPCHeader;
import kr.ac.dsc.lecturesurvey.model.User;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

public class LSApplication extends Application {

	//App의 Context를 갖고 있는다.
	public static Context gContext;
	
	public static IPCHeader gRequestHeader;		// 서버로 Request 할 시 사용될 Header
	public static User gUser;					// 현재 사용자 정보

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		gContext =  this;
		
		gRequestHeader = new IPCHeader();
		gUser = new User();
		
		//Shared Preference를 gRequestHeader로 읽어온다.
		getSharedPreferences();
		
		//DeviceID 값이 없을때는 생성하여 Shared Preference에 저장한다.
		if(gRequestHeader.getDeviceID().length() < 3) {
			String deviceID = UniqueDeviceID.getUniqueDeviceID(this);
			setSharedPreferencesDeviceID(deviceID);
			gRequestHeader.setDeviceID(deviceID);
		}
		
		Log.i("access_token", gRequestHeader.getAccess_token());
		Log.i("DeviceID", gRequestHeader.getDeviceID());
		
	}
	
	public void getSharedPreferences() {
		SharedPreferences prefs = getSharedPreferences("lecture_survey_sharedpref", MODE_PRIVATE);
		gRequestHeader.setAccess_token(prefs.getString("acces_token", ""));
		gRequestHeader.setDeviceID(prefs.getString("deviceID", ""));
	}
	
	public void setSharedPreferences() {
		SharedPreferences prefs = getSharedPreferences("lecture_survey_sharedpref", MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("acces_token", gRequestHeader.getAccess_token());
		edit.putString("deviceID", gRequestHeader.getDeviceID());
		edit.commit();
	}

	public void setSharedPreferencesDeviceID(String deviceID) {
		SharedPreferences prefs = getSharedPreferences("lecture_survey_sharedpref", MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("deviceID", deviceID);
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
	
	public static void ErrorPopup(Context context, int titleResId, String message, DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(titleResId);
		ad.setMessage(message);
		ad.setPositiveButton(R.string.popup_alert_title_info, listener);
		ad.show();
	}	
}
