package com.colintony.aes4;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class Aes4View extends SurfaceView implements Callback
{
	
	private Aes4Thread thread;
	private float fwidth, fheight;
	
	public Aes4View(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		// register interest in hearing about changes to surface
		SurfaceHolder surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setSizeFromLayout();

		// pre-create thread
		thread = new Aes4Thread(surfaceHolder, getContext(), getHandler());
		thread.setRunning(true);
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
	
	  /* 
    Called automatically when the view's orientation changes or gets resized etc. 
*/
  @Override
  protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
      super.onSizeChanged(xNew, yNew, xOld, yOld);
      fwidth = xNew;
      fheight = yNew;
      
      float n1 = (float)xNew/800;
      float n2 = (float)yNew/480;
      thread.setScale(Math.min(n1,  n2));
  }
	
	public Aes4Thread getThread()
	{
		return thread;
	}
	
	
	private float mx2, mx3, my2, my3;
	private float sx, sy;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int point = event.getPointerId(0);
		int eventaction = event.getAction();

		if (eventaction == MotionEvent.ACTION_DOWN) {
			mx2 = event.getX(point);
	    	my2 = event.getY(point);
	    	
			sx = mx2;
	    	sy = my2;
		}
  	  	
		if (eventaction == MotionEvent.ACTION_MOVE) {
	  	  	for (int i = 0; i < event.getPointerCount(); i++) {
			    point = event.getPointerId(i);
			    if(point<2){  
		    		point = event.getPointerId(i);
		    		mx3 -= Math.max(Math.min(25,mx2-event.getX(point)),-25);
		    		my3 -= Math.max(Math.min(25,my2-event.getY(point)),-25);
		    		mx2 = (int)event.getX(point);
		    		my2 = (int)event.getY(point);
		    		
		    		mx3 = (float) Math.max(Math.min(0,mx3),-400);
		    		my3 = (float) Math.max(Math.min(0,my3),-320);
		    		
		    		thread.setM(mx3, my3);
			    }
	  	  	}
		}
		
		if (eventaction == MotionEvent.ACTION_UP) {
			if(event.getX(point) < sx + 70 && event.getX(point) > sx - 70 &&
					event.getY(point) < sy + 70 && event.getY(point) > sy - 70 )
				thread.click(sx/fwidth, sy/fheight);
		}
		
		return true; 
	}
}
