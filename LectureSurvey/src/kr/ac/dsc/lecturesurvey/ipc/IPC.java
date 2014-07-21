package kr.ac.dsc.lecturesurvey.ipc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import kr.ac.dsc.lecturesurvey.model.Survey;
import kr.ac.dsc.lecturesurvey.model.SurveyItem;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class IPC {

	private static IPC instance = new IPC();
	private String ServerBaseUrl = "http://hoonjobs.com/survey/json/";
	private Gson gson = new Gson();

	public IPCHeader mRequestHeader;
	public IPCHeader mResponseHeader;

	private Context mContext = null;
	private RestClient mRestClient;

	private IPC() {
		mRequestHeader = new IPCHeader();
		mResponseHeader = new IPCHeader();
		mRestClient = new RestClient();
	}

	public static IPC getInstance() {
		if (instance == null)
			instance = new IPC();

		return instance;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public String getLastResponseErrorMsg() {
		return mResponseHeader.getMsg();
	}

	public JsonElement RequestPost(String url,
			ArrayList<NameValuePair> postParameters) {

		// 전송후 리턴 JSON
		JsonElement responseJson = mRestClient.Request(url, postParameters);

		return ResponseJSONHeaderCheck(responseJson);

	}

	public JsonElement RequestPostMultipart(String url,
			MultipartEntity postParameters) {

		// 전송후 리턴 JSON
		JsonElement responseJson = mRestClient.RequestMultipart(url,
				postParameters);

		return ResponseJSONHeaderCheck(responseJson);

	}

	// 전송 및 header code 처리
	public JsonElement RequestJSON(String url, String json) {
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(json);

		String rstJSON = getGson().toJson(jsonElement);
		Log.i("IPC_Request", rstJSON);

		// post Entity
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		try {
			postParameters.add(new BasicNameValuePair("json", URLEncoder
					.encode(rstJSON, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JsonElement responseJson = RequestPost(url, postParameters);

		return responseJson;

	}

	public JsonElement ResponseJSONHeaderCheck(JsonElement responseJson) {
		if (responseJson == null)
			return null;

		// header 상태 점검
		if (responseJson.isJsonObject()) {
			JsonObject jsonObject = responseJson.getAsJsonObject();
			JsonObject header = jsonObject.getAsJsonObject("header");

			mResponseHeader = gson.fromJson(header, IPCHeader.class);
			if (!mResponseHeader.getCode().equals("00")) {
				if (mContext != null) {
					mContext = null;
				}

				return null;
			}
		}
		return responseJson;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////

	public JsonElement requestInitSession(IPCHeader header) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("session/get.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JSON_Body = "\"body\":\"\"";
		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}

	public JsonElement requestLogin(IPCHeader header, String email, String pw) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("login/get.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JSON_Body = "\"body\":{ " + "\"email\":\"" + email + "\","
				+ "\"pw\":\"" + pw + "\"" + "}";

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}

	public JsonElement requestSignUp(IPCHeader header, String name,
			String dept, String studentID, String email, String password) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("member/post.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JSON_Body = "\"body\":{ " + "\"name\":\"" + name + "\","
				+ "\"dept\":\"" + dept + "\"," + "\"studentID\":\"" + studentID
				+ "\"," + "\"email\":\"" + email + "\"," + "\"pw\":\""
				+ password + "\"" + "}";

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}

	public JsonElement requestSurveyList(IPCHeader header, int page) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("survey/list/get.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JSON_Body = "\"body\":{ " + "\"page\":" + page + "}";

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}

	public JsonElement requestSurveyPost(IPCHeader header, Survey survey) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("survey/post.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JsonSurvey = gson.toJson(survey);
		String JSON_Body = "\"body\": " + JsonSurvey;

		// + "\"name\":\"" + name + "\", "
		// + "\"dept\":\"" + dept + "\", "
		// + "\"date\":\"" + date + "\", "
		// + "\"msg\":\"" + msg + "\" "
		// + "";

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}

	public JsonElement requestSurveyDelete(IPCHeader header, int surveyIdx) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("survey/delete.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JSON_Body = "\"body\":{ " + "\"idx\":" + surveyIdx + "}";

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}

	public JsonElement requestSurveyStatusUpdate(IPCHeader header,
			int surveyIdx, int status) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("survey/status/put.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JSON_Body = "\"body\":{ " + "\"idx\":" + surveyIdx + ","
				+ "\"status\":" + status + "}";

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}
	
	public JsonElement requestSurveyItemList(IPCHeader header, int surveyId) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("survey/item/list/get.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JSON_Body = "\"body\":{ " + "\"surveyIdx\":" + surveyId + "}";

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}
	
	public JsonElement requestSurveyItemPost(IPCHeader header, SurveyItem surveyItem) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("survey/item/post.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JsonSurvey = gson.toJson(surveyItem);
		String JSON_Body = "\"body\": " + JsonSurvey;

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}
	
	public JsonElement requestSurveyItemPut(IPCHeader header, SurveyItem surveyItem) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("survey/item/put.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JsonSurvey = gson.toJson(surveyItem);
		String JSON_Body = "\"body\": " + JsonSurvey;

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}

	public JsonElement requestSurveyItemDelete(IPCHeader header, int surveyId, int surveyItemId) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("survey/item/delete.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JSON_Body = "\"body\":{ " 
				+ "\"surveyIdx\":" + surveyId + "," 
				+ "\"idx\":" + surveyItemId
				+ "}";

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
	}
	
	public JsonElement requestFillOutPost(IPCHeader header, int surveyIdx, int surveyItemIdx, int answer) {
		StringBuffer path = new StringBuffer(ServerBaseUrl);
		String url = path.append("survey/fill/post.php").toString();
		Log.i("IPC_Request", url);

		String rstJSON = "";
		String JSON_Header = header.getHeaderJSON();
		String JSON_Body = "\"body\":{ "
				+ "\"surveyIdx\":" + surveyIdx + ","
				+ "\"itemIdx\":" + surveyItemIdx + ","
				+ "\"answer\":" + answer + "}";

		rstJSON = "{" + JSON_Header + "," + JSON_Body + "}";

		return RequestJSON(url, rstJSON);
		
	}

}
