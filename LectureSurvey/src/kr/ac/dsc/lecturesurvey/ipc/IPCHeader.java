package kr.ac.dsc.lecturesurvey.ipc;

import android.os.Build;

public class IPCHeader {
	private String access_token = "";
	private String device = Build.MODEL + "_" + Build.VERSION.RELEASE;
	private String deviceID = "";
	private String code = "00";
	private String msg = "";
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getHeaderJSON() {
		String sHeader = "\"header\":{ "
				+ "\"device\":\""
				+ device
				+ "\","
				+ "\"device_id\":\""
				+ deviceID
				+ "\","
				+ "\"access_token\":\""
				+ access_token
				+ "\","
				+ "\"code\":\"00"
				+ "\","
				+ "\"msg\":\"\"}";
		return sHeader;
	}
	
}
