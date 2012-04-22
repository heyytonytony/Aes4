package com.colintony.aes4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Aes4Activity extends Activity
{
	/** thread reference */
	private Aes4Thread thread;
	
	private Dialog dia;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aes4_layout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		
		Aes4View aes4 = (Aes4View) findViewById(R.id.aes4viewlayout);
		aes4.getPaddingTop(); //stops those squiggly yellow lines -_-
		thread = aes4.getThread();
	}
	
	// pause game
	public void pause(View view)
	{
		thread.setRunning(false);
		showDialog(0);
	}
	
	protected Dialog onCreateDialog(int id)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Game paused!").setCancelable(false).setNeutralButton("Unpause!", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				thread.setRunning(true);
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		dia = alert;
		return dia;
	}
	
	/**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
        System.gc();
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        System.gc();
    }
}