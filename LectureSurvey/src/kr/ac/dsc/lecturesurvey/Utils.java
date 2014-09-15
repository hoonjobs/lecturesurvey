package kr.ac.dsc.lecturesurvey;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.io.IOException;

import android.util.Patterns;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class Utils {

	/**
	 * NIO Channel을 이용한 파일복사 코드 스니핏
	 * 
	 * @author 신윤섭
	 */
	/**
	 * source에서 target으로의 파일 복사
	 * 
	 * @param source
	 *            복사할 파일명을 포함한 절대 경로
	 * @param target
	 *            복사될 파일명을 포함한 절대경로
	 */
	public static void copy(String source, String target) {
		// 복사 대상이 되는 파일 생성
		File sourceFile = new File(source);

		// 스트림, 채널 선언
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;

		try {
			// 스트림 생성
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(target);
			// 채널 생성
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();

			// 채널을 통한 스트림 전송
			long size = fcin.size();
			fcin.transferTo(0, size, fcout);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 자원 해제
			try {
				fcout.close();
			} catch (IOException ioe) {
			}
			try {
				fcin.close();
			} catch (IOException ioe) {
			}
			try {
				outputStream.close();
			} catch (IOException ioe) {
			}
			try {
				inputStream.close();
			} catch (IOException ioe) {
			}
		}
	}

	
	
	////////////////////////////////////////////////////////////////////////////////////////////
	//Fade In Out Imageview animation
//	ImageView demoImage = (ImageView) findViewById(R.id.DemoImage);
//	int imagesToShow[] = { R.drawable.image1, R.drawable.image2,R.drawable.image3 };
//
//	animate(demoImage, imagesToShow, 0,false);
// http://stackoverflow.com/questions/8720626/android-fade-in-and-fade-out-with-imageview
	public static void animateFadeInOut(final ImageView imageView, final int images[],
			final int imageIndex, final boolean forever) {

		// imageView <-- The View which displays the images
		// images[] <-- Holds R references to the images to display
		// imageIndex <-- index of the first image to show in images[]
		// forever <-- If equals true then after the last image it starts all
		// over again with the first image resulting in an infinite loop. You
		// have been warned.

		int fadeInDuration = 500; // Configure time values here
		int timeBetween = 1000;
		int fadeOutDuration = 1000;

		imageView.setVisibility(View.INVISIBLE); // Visible or invisible by
													// default - this will apply
													// when the animation ends
		imageView.setImageResource(images[imageIndex]);

		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
		fadeIn.setDuration(fadeInDuration);

		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
		fadeOut.setStartOffset(fadeInDuration + timeBetween);
		fadeOut.setDuration(fadeOutDuration);

		AnimationSet animation = new AnimationSet(false); // change to false
		animation.addAnimation(fadeIn);
		animation.addAnimation(fadeOut);
		animation.setRepeatCount(1);
		imageView.setAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				if (images.length - 1 > imageIndex) {
					animateFadeInOut(imageView, images, imageIndex + 1, forever); // Calls
																			// itself
																			// until
																			// it
																			// gets
																			// to
																			// the
																			// end
																			// of
																			// the
																			// array
				} else {
					if (forever == true) {
						animateFadeInOut(imageView, images, 0, forever); // Calls itself
																// to start the
																// animation all
																// over again in
																// a loop if
																// forever =
																// true
					}
				}
			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}
		});
	}
	////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public static boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
	
	//탭,space,carriage return 을 제외한 문자열 길이를 체크후 내용이 있다고 판단시 true
	public static boolean validStringContentsLength(String chkString) {
		if(chkString.replaceAll("[\r\n\t\\p{Space}]", "").length() > 1) {
			return true;
		}
		return false;
	}
}