package kr.ac.dsc.lecturesurvey;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class ImageBtn extends ImageButton {
	int itemID;
	String itemName;
	String overImageName;
	int overColor;
	
	String defaultImageName;
	int defaultColor;
	boolean bAlphaEffect;

	public ImageBtn(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public ImageBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public ImageBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public void init() {
		itemName = overImageName = defaultImageName = null;
		overColor = Color.rgb(196, 255, 192); 
		defaultColor = Color.TRANSPARENT;
		bAlphaEffect = true;
		setBackgroundColor(defaultColor);		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (bAlphaEffect) {
				if(Build.VERSION.SDK_INT >= 16)
					Alpha16(155);
				else
					Alpha(155);
				break;
			} else if (overImageName != null) {
				this.setImageResource(getResources().getIdentifier(overImageName,
						"drawable", null));
			} else if (overColor != 0) {
				this.setBackgroundColor(overColor);
			}
			break;
		}
		default: {
			if (bAlphaEffect) {
				
				if(Build.VERSION.SDK_INT >= 16)
					Alpha16(255);
				else
					Alpha(255);
				break;
			} else if (defaultImageName != null) {
				this.setImageResource(getResources().getIdentifier(
						defaultImageName, "drawable", null));
			} else if (defaultColor != 0) {
				this.setBackgroundColor(defaultColor);
			}
			break;
		}
		}
		return super.onTouchEvent(event);
	}

	public String getmItemNM() {
		return itemName;
	}

	public void setmItemNM(String mItemNM) {
		this.itemName = mItemNM;
	}

	public int getItemID() {
		return itemID;
	}

	public void setitemID(int itemID) {
		this.itemID = itemID;
	}
	
	@TargetApi(16)
	private void Alpha16(int alpha) {
		setImageAlpha(alpha);
	}
	
	private void Alpha(int alpha) {
		setAlpha(alpha);
	}
}
