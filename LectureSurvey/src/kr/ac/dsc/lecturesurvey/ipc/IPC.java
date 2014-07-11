package kr.ac.dsc.lecturesurvey.ipc;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class IPC {
	private String ServerBaseUrl = "http://hoonjobs.com/survey/json/";
	
	public IPCHeader mHeader;
	private RequestQueue mRequestQueue;
	private Context mContext = null;

	public IPC(Context context, String deviceID, String appVersion, String device) {
		super();
		this.mContext = context;
		mHeader.setDevice(deviceID);
		mHeader.setVersion(appVersion);
		mHeader.setDevice(device);
	} 

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
 
        return mRequestQueue;
    }
    
	public void requestInitSession() {
		
		String url = ServerBaseUrl + "session/put.php";
		JsonObjectRequest request = new JsonObjectRequest(Method.GET, url, null)
		getRequestQueue().add(request);
		
	}
}
