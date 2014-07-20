package kr.ac.dsc.lecturesurvey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.JsonElement;

import kr.ac.dsc.lecturesurvey.ipc.IPC;
import kr.ac.dsc.lecturesurvey.model.Survey;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class SurveyViewActivity extends Activity {

	private FrameLayout SpinnerLayout;
	private ImageView IvSpinner;
	AnimationDrawable Indicator_frameAnimation;

	private Survey mSurvey;
	Context mContext;

	private ImageView icStatus;
	private TextView tvDeptname;
	private TextView tvLectureName;
	private TextView tvLectureDate;
	private TextView tvSurveyMsg;

	private ImageBtn btn_survey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.survey_view_activity);

		// get Survey Data ////////////
		Intent intent = getIntent();
		mSurvey = ((Survey) intent.getSerializableExtra("survey"));
		if (mSurvey == null)
			finish();

		SpinnerLayout = (FrameLayout) findViewById(R.id.spinnerLayout);
		IvSpinner = (ImageView) findViewById(R.id.spinner_image);
		Indicator_frameAnimation = (AnimationDrawable) IvSpinner.getDrawable();

		mContext = this;

		icStatus = (ImageView) findViewById(R.id.survey_view_ivStatus);
		tvDeptname = (TextView) findViewById(R.id.survey_view_tvDeptName);
		tvLectureName = (TextView) findViewById(R.id.survey_view_tvLectureName);
		tvLectureDate = (TextView) findViewById(R.id.survey_view_tvLectureDate);
		tvSurveyMsg = (TextView) findViewById(R.id.survey_view_tvSurveyMsg);

		btn_survey = (ImageBtn) findViewById(R.id.survey_view_btnSurvey);

		setSurvey(mSurvey);

	}

	private void setSurvey(Survey survey) {
		// 설문중일때 아이콘
		if (mSurvey.getStatus() < 2) {
			if (mSurvey.getStatus() == 0)
				icStatus.setImageResource(R.drawable.ic_question);
			else
				icStatus.setImageResource(R.drawable.ic_alert);
		} else {
			icStatus.setImageResource(R.drawable.ic_star);
		}
		
		// 교수님일 경우 설문지의 질문들을 관리하는 액티비티로 이동 가능,
		// 교수님일 경우 설문을 시작으로 변경해야함. -> 설문시작 이미지 추가
		if (LSApplication.gUser.getUsertype() > 0
				&& mSurvey.getUid() == LSApplication.gUser.getUid()) {
			findViewById(R.id.survey_view_layoutStudent).setVisibility(
					View.GONE);
			findViewById(R.id.survey_view_layoutProf).setVisibility(
					View.VISIBLE);

			findViewById(R.id.survey_view_btn_manage_survey_items)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(SurveyViewActivity.this,
									ManageSurveyItemsActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("survey", mSurvey); // 새로운 액티비티에
																// 데이터를 넘겨준다
							startActivity(intent); // 새로운 액티비티 실행~~
							overridePendingTransition(R.anim.left_in,
									R.anim.splashfadeout);
						}
					});

			//교수님이지만 설문지 등록자 인지 체크
			if(LSApplication.gUser.getUid() == mSurvey.getUid()) {
			findViewById(R.id.survey_view_btn_delete_survey_items)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							alertDialog(mContext,
									R.string.alert_msg_survey_delete_confirm,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											Log.i(getClass().getSimpleName(),
													"Delete Survey / idx :"
															+ mSurvey.getIdx());
											new RequestSurveyDelete()
													.execute(mSurvey.getIdx());
										}
									});

						}
					});
			} else {
				findViewById(R.id.survey_view_btn_delete_survey_items).setVisibility(View.GONE);
			}

		} else {
			// 학생
			if (mSurvey.getStatus() == 0 && mSurvey.getStatus() == 2) {
				// 설문대기중
				// 설문시작 버튼 숨김 및 Alert 메세지 visible
				btn_survey.setVisibility(View.GONE);
				
				TextView tvAlertMsg = (TextView)findViewById(R.id.survey_view_tvSurveyAlertMsg);
				tvAlertMsg.setVisibility(View.VISIBLE);
				
				if(mSurvey.getStatus() == 0) tvAlertMsg.setText(R.string.alert_msg_survey_ready);
				if(mSurvey.getStatus() == 2) tvAlertMsg.setText(R.string.alert_msg_survey_ended);				
				
			} else {
				// 설문 시작중.. 설문시작 버튼 활성화
				btn_survey.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SurveyViewActivity.this,
								FillOutSurveyActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("survey", mSurvey); // 새로운 액티비티에
															// 데이터를 넘겨준다
						startActivity(intent); // 새로운 액티비티 실행~~
						overridePendingTransition(R.anim.left_in,
								R.anim.splashfadeout);
					}
				});
			}
		}

		// 학과명 및 교수님 이름 출력
		tvDeptname.setText(survey.getDeptName() + "  /  "
				+ survey.getProfName() + " 교수님");

		tvLectureName.setText(survey.getLectureName());

		// 강의일
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.KOREA);
		SimpleDateFormat outputfmt = new SimpleDateFormat("yyyy-MM-dd hh:mm a",
				Locale.KOREA);
		Date dateLecture = null;
		try {
			dateLecture = fmt.parse(survey.getLectureDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tvLectureDate.setText("강의일 : " + outputfmt.format(dateLecture));

		// 설문 메세지
		tvSurveyMsg.setText(survey.getMsg());
	}

	private class RequestSurveyDelete extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... params) {
			return RequestSurveyDelete(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			showLoadingLayer(false);

			if (result) {
				setResult(Activity.RESULT_OK);
				finish();
			} else {
				ErrorPopUp();
			}
		}
	}

	public boolean RequestSurveyDelete(int surveyIdx) {
		JsonElement json = IPC.getInstance().requestSurveyDelete(
				LSApplication.gRequestHeader, surveyIdx);
		if (json != null) {
			return true;
		}
		return false;
	}

	public void ErrorPopUp() {
		LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, IPC
				.getInstance().getLastResponseErrorMsg(), null);
	}

	public void showLoadingLayer(boolean bShow) {
		if (bShow) {
			SpinnerLayout.setVisibility(View.VISIBLE);
			Indicator_frameAnimation.start();
		} else {
			Indicator_frameAnimation.stop();
			SpinnerLayout.setVisibility(View.GONE);
		}
	}

	public void alertDialog(Context context, int messageRedId,
			DialogInterface.OnClickListener listener) {
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(R.string.popup_alert_title_info);
		ad.setMessage(messageRedId);
		ad.setPositiveButton(context.getResources().getString(R.string.yes),
				listener).setNegativeButton(
				context.getResources().getString(R.string.no), listener);
		ad.show();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.splashfadein, R.anim.right_out);
	}
}
