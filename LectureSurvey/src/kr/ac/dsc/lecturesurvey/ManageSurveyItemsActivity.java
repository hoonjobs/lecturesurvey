package kr.ac.dsc.lecturesurvey;

import java.util.ArrayList;

import kr.ac.dsc.lecturesurvey.ipc.IPC;
import kr.ac.dsc.lecturesurvey.model.Survey;
import kr.ac.dsc.lecturesurvey.model.SurveyItem;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ManageSurveyItemsActivity extends Activity {

	static final int MENU_ITEM_DELETE = 0;
	
	private PullToRefreshListView mPullRefreshListView;	
	private ListView mActualListView;
	
	private Survey mSurvey;
	private ArrayList<SurveyItem> arrSurvey;
	private SurveyItemAdapter adapterSurvey;	

	public boolean bRefresh = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.manage_survey_items_activity);
		
		// get Survey Data ////////////
		Intent intent = getIntent();
		mSurvey = ((Survey) intent.getSerializableExtra("survey"));
		if (mSurvey == null)
			finish();
		
		Log.i(getClass().getSimpleName(), "Intent mSurvey / idx:: " + mSurvey.getIdx());
		
		
		if(LSApplication.gUser.getUsertype() >= 0) {
			ImageBtn btnRegSurvey = (ImageBtn) findViewById(R.id.activity_header_btnRegSurvey);
			btnRegSurvey.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ManageSurveyItemsActivity.this, RegSurveyItemActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("survey", mSurvey); //새로운 액티비티에 데이터를 넘겨준다
					startActivityForResult(intent, 0); //새로운 액티비티 실행~~
					overridePendingTransition(R.anim.left_in, R.anim.splashfadeout);
				}
			});
			btnRegSurvey.setVisibility(View.VISIBLE);
		}
		
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

		arrSurvey = new ArrayList<SurveyItem>();
		adapterSurvey = new SurveyItemAdapter(this, arrSurvey);

		new GetDataTask().execute();
		
		mActualListView.setAdapter(adapterSurvey);
		
		//ListView에서 Item Click시 이벤트
		mActualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
				ListView listView = mPullRefreshListView.getRefreshableView();
				SurveyItem item = (SurveyItem) listView.getItemAtPosition(position);
				Intent intent = new Intent(ManageSurveyItemsActivity.this, RegSurveyItemActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("survey", mSurvey); //새로운 액티비티에 데이터를 넘겨준다
				intent.putExtra("surveyItem", item); //새로운 액티비티에 데이터를 넘겨준다
				startActivityForResult(intent, 0); //새로운 액티비티 실행~~
				overridePendingTransition(R.anim.left_in, R.anim.splashfadeout);
            }
        });
		
		//ListView에 Long Click시에 메뉴가 나오도록 설저
		mActualListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

				menu.setHeaderTitle(R.string.menu);
				menu.add(0, MENU_ITEM_DELETE, 0, R.string.delete);
			}
		});
	}
	
	
	//메뉴에서 아이템 선택시
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		SurveyItem surveyItem = (SurveyItem) mActualListView.getItemAtPosition(info.position);
         
		switch (item.getItemId()) {
		case MENU_ITEM_DELETE:
			new DeleteDataTask().execute(surveyItem);
			break;
		}
		
		return super.onContextItemSelected(item);
	}

	private class DeleteDataTask extends AsyncTask<SurveyItem, Void, Boolean> {

		@Override
		protected Boolean doInBackground(SurveyItem... params) {
			JsonElement json = IPC.getInstance().requestSurveyItemDelete(LSApplication.gRequestHeader, mSurvey.getIdx(), params[0].getIdx());
			if(json == null) return false;
			if (json.isJsonObject()) {
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			bRefresh = true;
			new GetDataTask().execute();		
			super.onPostExecute(result);
		}
	}
	
	
	private class GetDataTask extends AsyncTask<Void, Void, ArrayList<SurveyItem>> {

		@Override
		protected ArrayList<SurveyItem> doInBackground(Void... params) {
			return RequestSurveyItemList();
		}

		@Override
		protected void onPostExecute(ArrayList<SurveyItem> arrNewSurveys) {
			//if(!result) return;
			
//			mPullRefreshListView.setVisibility(View.VISIBLE);
//			ListView actualListView = mPullRefreshListView.getRefreshableView();
//			actualListView.setVisibility(View.VISIBLE);			
			
			ResetList();
			 
			arrSurvey.addAll(arrNewSurveys);
			
			adapterSurvey.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();
			
			super.onPostExecute(arrNewSurveys);
		}
	}
	
	private ArrayList<SurveyItem> RequestSurveyItemList() {
		JsonElement json = IPC.getInstance().requestSurveyItemList(LSApplication.gRequestHeader, mSurvey.getIdx()); 
		return ResponseSurveyItemList(json);
//		{
//			return true;
//		}
//		return false;
	}

	private ArrayList<SurveyItem> ResponseSurveyItemList(JsonElement json) {
		
		if(json == null) return null;
		
		if (json.isJsonObject()) {
		    JsonObject jsonObject = json.getAsJsonObject();
		    //JsonObject header = jsonObject.getAsJsonObject("header");
		    JsonObject body = jsonObject.getAsJsonObject("body");
		   
		    JsonArray Items = body.getAsJsonArray("items");

		    ArrayList<SurveyItem> arrNewSurveys;
		    arrNewSurveys = IPC.getInstance().getGson().fromJson(Items, new TypeToken<ArrayList<SurveyItem>>(){}.getType());
		    return arrNewSurveys;
		    
//		    if(bRefresh) {
//		    	ResetList();
//		    }
//		    
//		    arrSurvey.addAll(arrNewSurveys);
		    
		    //return true;
		}
		return null;
	}

	protected void ResetList() {
    	arrSurvey.clear();
    	bRefresh = false;
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != RESULT_OK)
		{
			return;
		}
		
		Log.i(getClass().getSimpleName(), "onActivityResult :: refresh list");
		bRefresh = true;
		new GetDataTask().execute();
		mPullRefreshListView.setRefreshing(false);
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition( R.anim.splashfadein, R.anim.right_out);
	}

}
