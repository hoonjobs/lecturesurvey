package kr.ac.dsc.lecturesurvey;

import java.util.LinkedList;
import java.util.Queue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import kr.ac.dsc.lecturesurvey.model.*;

public class FillOutSurveyActivity extends Activity {

	Survey mSurvey;
	Queue<SurveyItem> mSurveyItems;
	SurveyItem mCurrentSurveyItem;

	LinearLayout layoutButtons;
	Animation animFadeIn, animBtnEffect;
	TextView tvQuestion;
	ImageBtn btnYes, btnNeither, btnNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		animBtnEffect = AnimationUtils.loadAnimation(this, R.anim.bottom_up);

		setContentView(R.layout.fillout_survey_activity);

		layoutButtons = (LinearLayout) findViewById(R.id.fill_out_survey_layout_button);
		tvQuestion = (TextView) findViewById(R.id.fill_out_survey_tvQuestion);

		btnYes = (ImageBtn) findViewById(R.id.fill_out_survey_btnYes);
		btnNeither = (ImageBtn) findViewById(R.id.fill_out_survey_btnNeither);
		btnNo = (ImageBtn) findViewById(R.id.fill_out_survey_btnNo);

		animBtnEffect.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				tvQuestion.setVisibility(View.VISIBLE);
				btnYes.setClickable(false);
				btnNeither.setClickable(false);
				btnNo.setClickable(false);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				btnYes.setClickable(true);
				btnNeither.setClickable(true);
				btnNo.setClickable(true);
			}
		});
		
		//  get Survey Data ////////////
		Intent intent = getIntent();
		mSurvey = ((Survey)intent.getSerializableExtra("survey")) ;
		if(mSurvey == null) finish();
		
		//Test Survey Item Data
		mSurveyItems = new LinkedList<SurveyItem>();
		mSurveyItems.add(new SurveyItem(1, 1, "이것은 설문입니다.1 답변을 부탁 드립니다. \r\n줄바꿈."));
		mSurveyItems.add(new SurveyItem(2, 1, "이것은 설문입니다.2 답변을 부탁 드립니다. \r\n줄바꿈."));
		mSurveyItems.add(new SurveyItem(3, 1, "이것은 설문입니다.3 답변을 부탁 드립니다. \r\n줄바꿈."));
		mSurveyItems.add(new SurveyItem(4, 1, "이것은 설문입니다.4 답변을 부탁 드립니다. \r\n줄바꿈."));
		mSurveyItems.add(new SurveyItem(5, 1, "이것은 설문입니다.5 답변을 부탁 드립니다. \r\n줄바꿈."));
		mSurveyItems.add(new SurveyItem(6, 1, "이것은 설문입니다.6 답변을 부탁 드립니다. \r\n줄바꿈."));
		mSurveyItems.add(new SurveyItem(7, 1, "이것은 설문입니다.7 답변을 부탁 드립니다. \r\n줄바꿈."));
		mSurveyItems.add(new SurveyItem(8, 1, "이것은 설문입니다.8 답변을 부탁 드립니다. \r\n줄바꿈."));
		mSurveyItems.add(new SurveyItem(9, 1, "이것은 설문입니다.9 답변을 부탁 드립니다. \r\n줄바꿈."));
		mSurveyItems.add(new SurveyItem(10, 1, "이것은 설문입니다.10 답변을 부탁 드립니다. \r\n줄바꿈."));
		
		setQuestion();
		////
		btnYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				tvQuestion.startAnimation(animFadeIn);
//				v.startAnimation(animBtnEffect);
				setQuestion();
			}
		});
		
	}

	protected void setQuestion() {

		//Queue에서 다음 데이터를 추출한다. 다음 데이터가 없다면 null 이 대입된다.
		mCurrentSurveyItem = mSurveyItems.poll();

		if (mCurrentSurveyItem != null) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					tvQuestion.setText(mCurrentSurveyItem.getQuestion());

					// start animation
					tvQuestion.startAnimation(animFadeIn);

					layoutButtons.setVisibility(View.VISIBLE);
					btnYes.startAnimation(animBtnEffect);
					btnNeither.startAnimation(animBtnEffect);
					btnNo.startAnimation(animBtnEffect);

				}
			});
		} else {
			//다음 질문이 없다. 여기에서 설문을 종료한다.
			//	option : 서버에 설문 종료 알림. 
			finish();
			overridePendingTransition(R.anim.alpha2000, R.anim.splashfadeout);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		// 하드웨어 이전 버튼에 따른 이벤트 설정
		case KeyEvent.KEYCODE_BACK:
			exitPopup();
			break;
		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		exitPopup();
		overridePendingTransition(R.anim.alpha2000, R.anim.splashfadeout);
	}

	public void exitPopup() {
		new AlertDialog.Builder(this).setTitle("설문 종료")
				.setMessage("설문을 종료 하시겠습니까?")
				.setPositiveButton("예", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						overridePendingTransition(R.anim.alpha2000, R.anim.splashfadeout);
					}
				}).setNegativeButton("아니오", null).show();
	}
}
