package kr.ac.dsc.lecturesurvey;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.regex.Pattern;

import kr.ac.dsc.lecturesurvey.ipc.IPC;
import kr.ac.dsc.lecturesurvey.ipc.VolleyClient;
import kr.ac.dsc.lecturesurvey.model.Survey;
import kr.ac.dsc.lecturesurvey.model.SurveyItem;
import kr.ac.dsc.lecturesurvey.ui.IconContextMenu;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.JsonElement;

public class RegSurveyItemActivity extends Activity {

	private FrameLayout SpinnerLayout;
	private ImageView IvSpinner;
	AnimationDrawable Indicator_frameAnimation;

	private Survey mSurvey;
	private SurveyItem mSurveyItem;
	private EditText etQuestion;
	
	private boolean updateMode;
	private boolean deleteMode;
	
	Context mContext;
	
	ImageView mImageView;
	
	/* ContextMenu*/
	private final int CONTEXT_MENU_ID = 1;
	private IconContextMenu iconContextMenu = null;
	
	private final int MENU_ITEM_1_ACTION = 1;
	private final int MENU_ITEM_2_ACTION = 2;
	private final int MENU_ITEM_3_ACTION = 3;
	private final int MENU_ITEM_4_ACTION = 4;	
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private Uri mAttachedUri = null;
	private Uri mImageCaptureUri = null;
	private String mImageCapturePath = null;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mContext = this;
		
		setContentView(R.layout.reg_survey_item_activity);
		
		//mImageView = (ImageView) findViewById(R.id.reg_survey_imageView);
		
		// get Survey Data ////////////
		Intent intent = getIntent();
		mSurvey = ((Survey) intent.getSerializableExtra("survey"));
		if (mSurvey == null)
			finish();
		
		mSurveyItem = ((SurveyItem) intent.getSerializableExtra("surveyItem"));
		if(mSurveyItem != null) updateMode = true;
		else {
			updateMode = false;
			mSurveyItem = new SurveyItem(0, 0, "");
		}
		
        SpinnerLayout = (FrameLayout) findViewById(R.id.spinnerLayout);
        IvSpinner = (ImageView) findViewById(R.id.spinner_image);
		Indicator_frameAnimation = (AnimationDrawable) IvSpinner
				.getDrawable();
		
		showLoadingLayer(false);
		
		Button btn_postSurvey = (Button)findViewById(R.id.reg_survey_item_post);
		btn_postSurvey.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doPostSurveyItem();
			}
		});
		
		Button btn_deleteSurvey = (Button)findViewById(R.id.reg_survey_item_delete);
		btn_deleteSurvey.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doDeleteSurveyItem();
			}
		});
		
		deleteMode = false;
		
		etQuestion = (EditText) findViewById(R.id.reg_survey_item_etQuestion);
		
		if(mSurveyItem != null) {
			etQuestion.setText(mSurveyItem.getQuestion());
			
			if(mSurveyItem.getImageUrl().length() > 3) {
				VolleyClient.getImageLoader().get(mSurveyItem.getImageUrl(), 
                        ImageLoader.getImageListener(mImageView, 
                                                      R.drawable.ic_star, 
                                                      R.drawable.ic_star));				
			}			
		}
		
    	/* ContextMenu*/
        Resources res = getResources();		
        iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
        iconContextMenu.addItem(res, "카메라로 촬영", R.drawable.ic_camera, MENU_ITEM_1_ACTION);
        iconContextMenu.addItem(res, "사진첩에서 불러오기", R.drawable.ic_gallery, MENU_ITEM_2_ACTION);
        
        //set onclick listener for context menu
        iconContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
			@Override
			public void onClick(int menuId) {
				switch(menuId) {
				case MENU_ITEM_1_ACTION:
					doTakePhotoAction();
					break;
				case MENU_ITEM_2_ACTION:
					doTakeAlbumAction();
					//Toast.makeText(getApplicationContext(), "You've clicked on menu item 2", 1000).show();
					break;
				case MENU_ITEM_3_ACTION:
					//Toast.makeText(getApplicationContext(), "You've clicked on menu item 3", 1000).show();
					break;
				case MENU_ITEM_4_ACTION:
					//Toast.makeText(getApplicationContext(), "You've clicked on menu item 4", 1000).show();
					break;
				}
			}
		});
        
    	/*// ContextMenu*/
		
	}
	
	public void doPostSurveyItem()
	{
		Log.i(getClass().getSimpleName(), "doPostSurveyItem");

		String surveyItemQuestion = etQuestion.getText().toString();
		
		if(!validStringContentsLength(surveyItemQuestion))
		{				
			LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, R.string.error_msg_question, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// TODO Auto-generated method stub
	        		etQuestion.requestFocus();
        		}
	        } );
			return;
		}
		
		showLoadingLayer(true);

		SurveyItem surveyItem = new SurveyItem(mSurveyItem.getIdx(), mSurvey.getIdx(), surveyItemQuestion);
		
		new GetDataTask().execute(surveyItem);
	}
	
	public void doDeleteSurveyItem()
	{
		Log.i(getClass().getSimpleName(), "doDeleteSurveyItem");

		deleteMode = true;
		
		showLoadingLayer(true);

		SurveyItem surveyItem = new SurveyItem(mSurveyItem.getIdx(), mSurvey.getIdx(), "");
		
		new GetDataTask().execute(surveyItem);
	}

	//카메라촬영
	private void doTakePhotoAction() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		if(mImageCaptureUri == null) {
			mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
					"lecturesurvey_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
		}

		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

		try {
			intent.putExtra("return-data", false);
			startActivityForResult(intent, PICK_FROM_CAMERA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void doTakeAlbumAction() {
		// 사진첩에서 불러오기
		// Intent intent = new Intent(Intent.ACTION_PICK);
		// intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(intent, PICK_FROM_ALBUM);
		overridePendingTransition(R.anim.left_in, R.anim.splashfadeout);

	}
	
	private class GetDataTask extends AsyncTask<SurveyItem, Void, Boolean> {

		@Override
		protected Boolean doInBackground(SurveyItem... params) {
			if(deleteMode) 
				return RequestDelete(params[0]);
			else if(updateMode)
				return RequestPut(params[0]);
			else 
				return RequestPost(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			showLoadingLayer(false);

			if (result) {
				// 등록성공
				LSApplication.ErrorPopup(mContext,
						R.string.popup_alert_title_info, R.string.success_save,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								setResult(RESULT_OK);
								finish();
								overridePendingTransition(R.anim.splashfadein,
										R.anim.right_out);
							}
						});
			} else {
				ErrorPopUp();
			}
		}
	}
	
	private boolean RequestPost(SurveyItem surveyItem) {
		JsonElement responseJson = IPC.getInstance().requestSurveyItemPost(LSApplication.gRequestHeader, surveyItem); 
		if(ResponseSurveyItemPost(responseJson)) {
			return true;
		}
		return false;
	}
	
	//수정
	private boolean RequestPut(SurveyItem surveyItem) {
		JsonElement responseJson = IPC.getInstance().requestSurveyItemPut(LSApplication.gRequestHeader, surveyItem); 
		if(ResponseSurveyItemPost(responseJson)) {
			return true;
		}
		return false;
	}
	
	//삭제
	private boolean RequestDelete(SurveyItem surveyItem) {
		JsonElement responseJson = IPC.getInstance().requestSurveyItemDelete(LSApplication.gRequestHeader, surveyItem.getSurveyIdx(), surveyItem.getIdx()); 
		if(ResponseSurveyItemPost(responseJson)) {
			return true;
		}
		return false;
	}
	
	private boolean ResponseSurveyItemPost(JsonElement json)
	{
		if(json == null) return false;
		
		if (json.isJsonObject()) {
	        return true;
		}
		return false;
	}	
	
	public void ErrorPopUp() {
		LSApplication.ErrorPopup(mContext, R.string.popup_alert_title_info, IPC
				.getInstance().getLastResponseErrorMsg(), null);
	}
	
	public void showLoadingLayer(boolean bShow)
	{
		if(bShow) {
			SpinnerLayout.setVisibility(View.VISIBLE);
	    	Indicator_frameAnimation.start();
		}
		else	{
			Indicator_frameAnimation.stop();
			SpinnerLayout.setVisibility(View.GONE);    	
		}		
	}
	
	public boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
	
	//탭,space,carriage return 을 제외한 문자열 길이를 체크후 내용이 있다고 판단시 true
	public boolean validStringContentsLength(String chkString) {
		if(chkString.replaceAll("[\r\n\t\\p{Space}]", "").length() > 1) {
			return true;
		}
		return false;
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition( R.anim.splashfadein, R.anim.right_out);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode != RESULT_OK)
		{
			return;
		}

		switch (requestCode) {
		case PICK_FROM_ALBUM: {
			if (null != data) {
				Uri uri = null;
				if (data != null) {
					uri = data.getData();

					Log.d(getClass().getSimpleName(), "PICK_FROM_ALBUM " + uri);

					mAttachedUri = uri;
					showImage(uri);
				}
				break;
			}
		}
		}
	}

	private void showImage(Uri uri) {
		AsyncTask<Uri, Void, Bitmap> imageLoadAsyncTask = new AsyncTask<Uri, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Uri... uris) {
				return getBitmapFromUri(uris[0]);
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				//mImageView.setImageBitmap(bitmap);
			}
		};
		
		imageLoadAsyncTask.execute(uri);
	}
	
	/* Uri를 Bitmap으로 쓸 수 있게해준다. */
	private Bitmap getBitmapFromUri(Uri uri) {
		ParcelFileDescriptor parcelFileDescriptor = null;
		try {
			parcelFileDescriptor = this.getContentResolver()
					.openFileDescriptor(uri, "r");
			FileDescriptor fileDescriptor = parcelFileDescriptor
					.getFileDescriptor();
			Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
			parcelFileDescriptor.close();
			return image;
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "Failed to load image.", e);
			return null;
		} finally {
			try {
				if (parcelFileDescriptor != null) {
					parcelFileDescriptor.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(getClass().getSimpleName(),
						"Error closing ParcelFile Descriptor");
			}
		}
	}
}
