package kr.ac.dsc.lecturesurvey;

import java.util.ArrayList;

import kr.ac.dsc.lecturesurvey.ipc.IPC;
import kr.ac.dsc.lecturesurvey.model.Survey;
import kr.ac.dsc.lecturesurvey.model.Respondent;
import kr.ac.dsc.lecturesurvey.model.User;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SurveyRespondentsActivity extends Activity {
	private PullToRefreshListView mPullRefreshListView;	
	private ListView mActualListView;
	
	private Survey mSurvey;
	private ArrayList<Respondent> arrSurvey;
	private SurveyRespondentsAdapter adapterSurvey;

	public boolean bRefresh = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.basic_listview_activity);
		
		// get Survey Data ////////////
		Intent intent = getIntent();
		mSurvey = ((Survey) intent.getSerializableExtra("survey"));
		if (mSurvey == null)
			finish();
		
		Log.i(getClass().getSimpleName(), "Intent mSurvey / idx:: " + mSurvey.getIdx());
		
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.manage_survey_items_activity_listview);
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				String label = DateUtils.formatDateTime(getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				
				mPullRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
				// Do work to refresh the list here.
				bRefresh = true;
				new GetDataTask().execute();
			}
		});
		
		mActualListView = mPullRefreshListView.getRefreshableView();

		arrSurvey = new ArrayList<Respondent>();
		adapterSurvey = new SurveyRespondentsAdapter(this, arrSurvey);

		new GetDataTask().execute();
		
		mActualListView.setAdapter(adapterSurvey);
		
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, ArrayList<Respondent>> {

		@Override
		protected ArrayList<Respondent> doInBackground(Void... params) {
			return RequestRespondentList();
		}

		@Override
		protected void onPostExecute(ArrayList<Respondent> arrNewSurveys) {
			
			ResetList();
			 
			arrSurvey.addAll(arrNewSurveys);
			
			adapterSurvey.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();
			
			super.onPostExecute(arrNewSurveys);
		}
	}
	
	private ArrayList<Respondent> RequestRespondentList() {
		JsonElement json = IPC.getInstance().requestSurveyRespondents(LSApplication.gRequestHeader, mSurvey.getIdx()); 
		return ResponseRespondentResult(json);
	}

	private ArrayList<Respondent> ResponseRespondentResult(JsonElement json) {
		
		if(json == null) return null;
		
		if (json.isJsonObject()) {
		    JsonObject jsonObject = json.getAsJsonObject();
		    //JsonObject header = jsonObject.getAsJsonObject("header");
		    JsonObject body = jsonObject.getAsJsonObject("body");
		   
		    JsonArray Items = body.getAsJsonArray("items");

		    ArrayList<Respondent> arrNewSurveys;
		    arrNewSurveys = IPC.getInstance().getGson().fromJson(Items, new TypeToken<ArrayList<Respondent>>(){}.getType());
		    return arrNewSurveys;
		}
		return null;
	}

	protected void ResetList() {
    	arrSurvey.clear();
    	bRefresh = false;
	}	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition( R.anim.splashfadein, R.anim.right_out);
	}
}
