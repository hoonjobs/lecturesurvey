package kr.ac.dsc.lecturesurvey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kr.ac.dsc.lecturesurvey.model.Survey;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SurveyViewActivity extends Activity {

	private Survey mSurvey;

	private ImageView icStatus;
	private TextView tvDeptname;
	private TextView tvLectureName;
	private TextView tvLectureDate;
	private TextView tvSurveyMsg;
	
	private TextView tvSurveyAlertMsg;

	private ImageBtn btn_survey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.survey_view_activity);
		
		//  get Survey Data ////////////
		Intent intent = getIntent();
		mSurvey = ((Survey)intent.getSerializableExtra("survey")) ;
		if(mSurvey == null) finish();

		icStatus = (ImageView)findViewById(R.id.survey_view_ivStatus);
		tvDeptname = (TextView)findViewById(R.id.survey_view_tvDeptName);
		tvLectureName = (TextView)findViewById(R.id.survey_view_tvLectureName);
		tvLectureDate = (TextView)findViewById(R.id.survey_view_tvLectureDate);
		tvSurveyMsg = (TextView)findViewById(R.id.survey_view_tvSurveyMsg);
		
		tvSurveyAlertMsg = (TextView)findViewById(R.id.survey_view_tvSurveyAlertMsg);
		
		btn_survey = (ImageBtn) findViewById(R.id.survey_view_btnSurvey);

		setSurvey(mSurvey);
		
	}
	
	private void setSurvey(Survey survey) {
		//설문중일때 아이콘
		if(mSurvey.getStatus() < 2) {
			icStatus.setImageResource(R.drawable.ic_alert);
			if(mSurvey.getStatus() == 0) {
				//설문대기중
				//설문시작 버튼 숨김 및 Alert 메세지 visible
				btn_survey.setVisibility(View.GONE);
				tvSurveyAlertMsg.setVisibility(View.VISIBLE);
				
				//교수님일 경우 설문지의 질문들을 관리하는 액티비티로 이동 가능,
				//교수님일 경우 설문을 시작으로 변경해야함. -> 설문시작 이미지 추가
			} else {
				//설문 시작중.. 설문시작 버튼 활성화
				btn_survey.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SurveyViewActivity.this, FillOutSurveyActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("survey", mSurvey); //새로운 액티비티에 데이터를 넘겨준다
						startActivity(intent); //새로운 액티비티 실행~~
						overridePendingTransition(R.anim.left_in, R.anim.splashfadeout);
					}
				});
			}
		}
		else {
			icStatus.setImageResource(R.drawable.ic_star);
		}
		
		//학과명 및 교수님 이름 출력
		tvDeptname.setText(survey.getDeptName() + "  /  " 
				+survey.getProfName() + " 교수님");

		tvLectureName.setText(survey.getLectureName());
		
		//강의일
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		SimpleDateFormat outputfmt = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.KOREA);
		Date dateLecture = null;
		try {
			dateLecture = fmt.parse(survey.getLectureDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tvLectureDate.setText("강의일 : " + outputfmt.format(dateLecture));

		//설문 메세지
		tvSurveyMsg.setText(survey.getMsg());
	}
	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.splashfadein, R.anim.right_out);
	}
}
