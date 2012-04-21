package com.colintony.aes4;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class MenuActivity extends Activity
{

	/** animated clouds */
	private ImageView cloud1, cloud2;
	private Animation cloud1Animation, cloud2Animation;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		Button StartGameButton = (Button)findViewById(R.id.StartGame);

		StartGameButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent StartGameIntent = new Intent(MenuActivity.this, Aes4Activity.class);
				startActivity(StartGameIntent);
			}
		});

		// Button TutorialButton = (Button)findViewById(R.id.Tutorial);
		// TutorialButton.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// Intent TutorialIntent = new
		// Intent(MenuActivity.this,TutorialActivity.class);
		// startActivity(TutorialIntent);
		// }
		// });
		
		/** animate the clouds woo */
		cloud1 = (ImageView)findViewById(R.id.cloud1Image);
		cloud2 = (ImageView)findViewById(R.id.cloud2Image);
		
		cloud1Animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f,
				TranslateAnimation.RELATIVE_TO_SELF, 0.15f,
				TranslateAnimation.RELATIVE_TO_SELF, 0f,
				TranslateAnimation.RELATIVE_TO_SELF, 0f);
		cloud1Animation.setDuration(1500);
		cloud1Animation.setRepeatCount(TranslateAnimation.INFINITE);
		cloud1Animation.setRepeatMode(TranslateAnimation.REVERSE);
		cloud1Animation.setInterpolator(new LinearInterpolator());
		cloud1.setAnimation(cloud1Animation);
		
		cloud2Animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f,
				TranslateAnimation.RELATIVE_TO_SELF, 0.1f,
				TranslateAnimation.RELATIVE_TO_SELF, 0f,
				TranslateAnimation.RELATIVE_TO_SELF, 0f);
		cloud2Animation.setDuration(2000);
		cloud2Animation.setRepeatCount(TranslateAnimation.INFINITE);
		cloud2Animation.setRepeatMode(TranslateAnimation.REVERSE);
		cloud2Animation.setInterpolator(new LinearInterpolator());
		cloud2.setAnimation(cloud2Animation);
		
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}
}
