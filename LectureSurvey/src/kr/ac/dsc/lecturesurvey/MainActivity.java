package kr.ac.dsc.lecturesurvey;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import kr.ac.dsc.lecturesurvey.model.Survey;

public class MainActivity extends Activity {

	private PullToRefreshListView mPullRefreshListView;	
	
	private ArrayList<Survey> arrSurvey;
	private SurveyAdapter adapterSurvey;

	protected boolean initFlag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main_activity);
		
		
		if(LSApplication.gUser.getUsertype() >= 0) {
			ImageBtn btnRegSurvey = (ImageBtn) findViewById(R.id.activity_header_btnRegSurvey);
			btnRegSurvey.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MainActivity.this, RegSurveyActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent); //새로운 액티비티 실행~~
					overridePendingTransition(R.anim.left_in, R.anim.splashfadeout);
				}
			});
			btnRegSurvey.setVisibility(View.VISIBLE);
		}
		 
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.main_activity_listview);
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				String label = DateUtils.formatDateTime(getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				
				mPullRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});
		
		ListView actualListView = mPullRefreshListView.getRefreshableView();

		arrSurvey = new ArrayList<Survey>();
		adapterSurvey = new SurveyAdapter(this, arrSurvey);

		new GetDataTask().execute();
		
		actualListView.setAdapter(adapterSurvey);
		
		
		//ListView에서 Item Click시 이벤트
		actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
				ListView listView = mPullRefreshListView.getRefreshableView();
				Survey item = (Survey) listView.getItemAtPosition(position);
				Intent intent = new Intent(MainActivity.this, SurveyViewActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("survey", item); //새로운 액티비티에 데이터를 넘겨준다
				startActivity(intent); //새로운 액티비티 실행~~
				overridePendingTransition(R.anim.left_in, R.anim.splashfadeout);
            }
        });	
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			CreateList();
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(!result) return;
			
			if (!initFlag) {
				runOnUiThread(addviewThread);
			}
			
			adapterSurvey.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();
			
			super.onPostExecute(result);
		}
	}

	private Runnable addviewThread = new Runnable() {
		@Override
		public void run() {
			if (!initFlag ) {
				initFlag = true;
				mPullRefreshListView.setVisibility(View.VISIBLE);
				ListView actualListView = mPullRefreshListView.getRefreshableView();
				actualListView.setVisibility(View.VISIBLE);
			}
	   }
	};
	
	private void CreateList() {
		ResetList();
		arrSurvey.add(new Survey(1, 1, "Android 실습-1", "2014-07-12", "컴퓨터소프트웨어학과", "최영철", 1, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(2, 2, "Android 실습-2", "2014-07-12", "컴퓨터소프트웨어학과", "최성훈", 2, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(3, 3, "Android 실습-3", "2014-07-12", "컴퓨터소프트웨어학과", "최성훈", 1, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(4, 4, "Android 실습-4", "2014-07-12", "컴퓨터소프트웨어학과", "최영철", 2, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(1, 1, "Android 실습-1", "2014-07-12", "컴퓨터소프트웨어학과", "최영철", 1, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(2, 2, "Android 실습-2", "2014-07-12", "컴퓨터소프트웨어학과", "최성훈", 2, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(3, 3, "Android 실습-3", "2014-07-12", "컴퓨터소프트웨어학과", "최성훈", 1, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(4, 4, "Android 실습-4", "2014-07-12", "컴퓨터소프트웨어학과", "최영철", 2, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(1, 1, "Android 실습-1", "2014-07-12", "컴퓨터소프트웨어학과", "최영철", 1, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(2, 2, "Android 실습-2", "2014-07-12", "컴퓨터소프트웨어학과", "최성훈", 2, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(3, 3, "Android 실습-3", "2014-07-12", "컴퓨터소프트웨어학과", "최성훈", 1, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
		arrSurvey.add(new Survey(4, 4, "Android 실습-4", "2014-07-12", "컴퓨터소프트웨어학과", "최영철", 2, "설문 메세지입니다.\r\n가볍게 설문에 응답해주세요.\r\n설문에 응답시 가산점이 부여됩니다.", false));
	}
	
	protected void ResetList() {
		arrSurvey.clear();
	}
}
