package com.colintony.aes4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

public class Aes4Thread extends Thread
{

	private boolean running;
	private Canvas c;
	private SurfaceHolder surfaceHolder = null;
	
    //private float Width = 800;
    //private float Height = 480;
    private float oscale = 0;
    
    public float mx, my, mx2, my2;
	
    private Bitmap[] pngs = new Bitmap[25];
    
    
    
	public Aes4Thread(SurfaceHolder holder, Context context, Handler handler)
	{
		surfaceHolder = holder;
		pngs[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.level1);
	}

	public void setRunning(boolean b)
	{
		running = b;
		
	}

	public void killThread()
	{
		// TODO Auto-generated method stub
		
	}

	private void draw(Canvas canvas) {
		canvas.save();
    	canvas.scale(oscale,oscale,0,0);
    	
    	canvas.drawBitmap(pngs[0], mx, my, null);
    	
        canvas.restore();
	}
	
	private void update() {
		if(mx2>mx)mx+=(mx2-mx)/3;
		else if(mx2<mx)mx-=(mx-mx2)/4;
	
		if(my2>my)my+=(my2-my)/3;
		else if(my2<my)my-=(my-my2)/4;
	}
	
    @Override
    public void run() {
    	while(running){
	       	c = null;
	        try {
	            c = surfaceHolder.lockCanvas(null);
	            synchronized (surfaceHolder) {
	                //if (mode == STATE_RUNNING) 
	            	update();
	                draw(c);
	            }
	        } finally {
	            if (c != null) {
	                surfaceHolder.unlockCanvasAndPost(c);
	            }
	        }
    	}
    }
    
    public void setScale(float oscale){
    	this.oscale = oscale;
    }
    
    public void setM(float mx2, float my2){
    	this.mx2 = mx2;
    	this.my2 = my2;
    }
}
