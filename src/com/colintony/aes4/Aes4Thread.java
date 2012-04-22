package com.colintony.aes4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
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
    public float tx, ty;
    
    public int X = 15;
	
    private Paint paint = new Paint();
    
    private Bitmap[] pngs = new Bitmap[25];
    
    private int[] turns = new int[3];
    
    
	public Aes4Thread(SurfaceHolder holder, Context context, Handler handler)
	{
		surfaceHolder = holder;
		pngs[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.level1b);
		pngs[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.train);
		pngs[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow);
		
		ty = 500;
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
    	
    	//bg
    	canvas.drawBitmap(pngs[0], mx, my, null);
    	
    	//connection arrows
    	if(turns[0] == 0) canvas.drawBitmap(pngs[2], mx + 280, my + 535, null);
    	if(turns[0] == 1){
    		canvas.save();
    		canvas.rotate(90, mx + 280 + 25,  my + 535 + 19);
    		canvas.drawBitmap(pngs[2], mx + 280, my + 535, null);
    		canvas.restore();
    	}
    	if(turns[0] == 2){
    		canvas.save();
    		canvas.rotate(-90, mx + 280 + 25,  my + 535 + 19);
    		canvas.drawBitmap(pngs[2], mx + 280, my + 535, null);
    		canvas.restore();
    	}
    	
    	if(turns[1] == 0) canvas.drawBitmap(pngs[2], mx + 615, my + 535, null);
    	if(turns[1] == 1){
    		canvas.save();
    		canvas.rotate(-90, mx + 615 + 25,  my + 535 + 19);
    		canvas.drawBitmap(pngs[2], mx + 615, my + 535, null);
    		canvas.restore();
    	}
    	
    	//train
    	canvas.drawBitmap(pngs[1], tx+mx, ty+my, null);
    	paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(20);
        paint.setAlpha(100);
        paint.setTextAlign(Align.CENTER);
        paint.setTypeface(Typeface.SANS_SERIF);
        
		canvas.drawText("X = " + X, mx+tx+37, my+ty+42, paint);
		
		
        canvas.restore();
	}
	
	private void update() {
		
		//Pan across screen
		if(mx2>mx)mx+=(mx2-mx)/3;
		else if(mx2<mx)mx-=(mx-mx2)/4;
	
		if(my2>my)my+=(my2-my)/3;
		else if(my2<my)my-=(my-my2)/4;
		
		//Train
		tx +=1.5;
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
    
    public void click(float cx, float cy){
    	float px = (305+mx)/800;
    	float py = (555+my)/480;
    	
    	if(cx < px +70/800f && cx > px -70/800f &&
    			cy < py +70/480f && cy > py -70/480f) turns[0] = (turns[0]+1)%3;
    		
    	px = (640+mx)/800;
    	py = (555+my)/480;
    	
    	if(cx < px +70/800f && cx > px -70/800f &&
    			cy < py +70/480f && cy > py -70/480f) turns[1] = (turns[1]+1)%2;
    }
    
    public void setScale(float oscale){
    	this.oscale = oscale;
    }
    
    public void setM(float mx2, float my2){
    	this.mx2 = mx2;
    	this.my2 = my2;
    }
}
