package kr.ac.dsc.lecturesurvey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InitActivity extends Activity {

	
	LinearLayout m_loadingLayout;
	ImageView mIvAnimation;
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);         
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mContext = this;
		
		//동적 Layout 생성
		m_loadingLayout = new LinearLayout(this);
		m_loadingLayout.setOrientation(LinearLayout.VERTICAL);
		m_loadingLayout.setGravity(Gravity.CENTER);
		m_loadingLayout.setBackgroundColor(Color.rgb(0,178,237));
		m_loadingLayout.setBackgroundResource(R.drawable.loading);
		
		//Loading Indicator를 보여줄 ImageView를 생성
		mIvAnimation = new ImageView(this);
		
		//Layout에 추가한다.
		m_loadingLayout.addView(mIvAnimation, new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.WRAP_CONTENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT
        		)
		);
		
		//Layout을 화면에 보여줄 View에 설정한다. 
        setContentView(m_loadingLayout);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//뷰가 화면에 뿌려지기 전에는 애니메이션이 시작하지 못한다.
		//약간의 딜레이를 주기 위하여 핸들러를 이용한다.
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
        		mIvAnimation.setBackgroundResource(R.anim.indicator_frame);
        		AnimationDrawable frameAnimation = (AnimationDrawable) mIvAnimation.getBackground();
                frameAnimation.start();
                
                //RequestInit();
            }
        }, 1000);  // 1000ms 후 실행된다.
        

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                finish();
        		
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); //새로운 액티비티 실행~~
                overridePendingTransition(R.anim.alpha2000, R.anim.fadeout);
            }
        }, 1000);  // 1000ms 후 실행된다.
	}
	
}
