package com.colintony.aes4;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class Aes4View extends SurfaceView implements Callback
{
	
	private Aes4Thread thread;
	
	public Aes4View(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		// register interest in hearing about changes to surface
		SurfaceHolder surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setSizeFromLayout();

		// pre-create thread
		thread = new Aes4Thread(surfaceHolder, getContext(), getHandler());
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// start the thread here so that we don't busy-wait in run()
		try
		{
			thread.start();
			thread.setRunning(true);
		}
		catch(Exception ex)
		{
			thread = new Aes4Thread(getHolder(), getContext(), getHandler());
			thread.start();
			thread.setRunning(true);
		}


	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode

		boolean retry = true;
		thread.setRunning(false);
		thread.killThread();
		while(retry)
		{
			try
			{
				Log.d("surfaceDestroyed", "trying to join thread...");
				thread.join();
				Log.d("surfaceDestroyed", "thread joined!");
				retry = false;
			}
			catch(Exception e)
			{
			}
		}
		Log.d("surfaceDestroyed", "destroy successful");

	}
	
	public Aes4Thread getThread()
	{
		return thread;
	}

}
