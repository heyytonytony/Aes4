package com.colintony.aes4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.SurfaceHolder;

public class Aes4Thread extends Thread
{

	private boolean alive, running;
	private Canvas c;
	private SurfaceHolder surfaceHolder = null;
	
    //private float Width = 800;
    //private float Height = 480;
    private float oscale = 0;
    
    public float mx, my, mx2, my2;
    public float tx, ty;
    public float rot;
    
    public int X = 0;
	public int frame = 0;
	public int direct = 0;
	//right0,up1,left2,down3
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
		tx = -85;
		alive = true;
	}

	public void setRunning(boolean b)
	{
		running = b;
		
	}

	public void killThread()
	{
		alive = false;
		
	}

	private void draw(Canvas canvas)
	{
		canvas.save();
    	canvas.scale(oscale,oscale,0,0);
    	
    	//bg
    	canvas.drawBitmap(pngs[0], mx, my, null);
    	
    	//connection arrows
    	switch(turns[0])
    	{
    		case 0:
    			canvas.drawBitmap(pngs[2], mx + 280, my + 535, null);
    			break;
    			
    		case 1:
        		canvas.save();
        		canvas.rotate(90, mx + 280 + 25,  my + 535 + 19);
        		canvas.drawBitmap(pngs[2], mx + 280, my + 535, null);
        		canvas.restore();
        		break;
        		
    		case 2:
        		canvas.save();
        		canvas.rotate(-90, mx + 280 + 25,  my + 535 + 19);
        		canvas.drawBitmap(pngs[2], mx + 280, my + 535, null);
        		canvas.restore();
        		break;
    	}
    	
    	switch(turns[1])
    	{
    		case 0:
    			canvas.drawBitmap(pngs[2], mx + 615, my + 535, null);
    			break;
    			
    		case 1:
        		canvas.save();
        		canvas.rotate(-90, mx + 615 + 25,  my + 535 + 19);
        		canvas.drawBitmap(pngs[2], mx + 615, my + 535, null);
        		canvas.restore();
    			break;
    	}
    	
    	//train
    	canvas.save();
    	canvas.translate(tx, ty);
    	canvas.rotate(rot, mx, my);
    	canvas.drawBitmap(pngs[1], mx, my, null);
    	
    	paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(20);
        paint.setAlpha(100);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.SANS_SERIF);
        canvas.drawText("X = " + X, mx+37, my+42, paint);
        
        canvas.restore();
        
		
		
		// countdown to game beginning
		if(frame < 350){
			paint.setTextSize(100);
			paint.setAlpha(255);
			if(frame < 280)
				canvas.drawText("" + (int)((300-frame)/ 71), 120, 120, paint);
			else
				canvas.drawText("Start!", 120, 120, paint);
			
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(2);
			paint.setAntiAlias(true);
			if(frame < 280)
				canvas.drawText("" + (int)((300-frame)/ 71), 120, 120, paint);
			else
				canvas.drawText("Start!", 120, 120, paint);
		}
		
        canvas.restore();
	}
	
	private void update()
	{
		frame++;
		
		//Pan across screen
		if(mx2>mx)mx+=(mx2-mx)/3;
		else if(mx2<mx)mx-=(mx-mx2)/4;
	
		if(my2>my)my+=(my2-my)/3;
		else if(my2<my)my-=(my-my2)/4;
		
		//Train
		if(frame > 350 && tx < 830 && direct == 0) tx +=1.5;
		if (ty > 200 && direct == 1) ty -=1.5;
		if (tx > 100 && direct == 2) tx -=1.5;
		if (ty < 750 && direct == 3) ty +=1.5;
		
		//turns280,535
		if(tx > 250 && tx < 280 && ty > 500){
			//rot
		}
		
		//turns
		if(tx > 655 && ty > 400){
			if(rot < 90) rot += 0.75;
			if(tx > 828 && rot > 89) direct = 3;
			if(ty > 748 && rot > 179) direct = 2;
			if(ty > 555 && rot < 180) rot += 0.75;
		}
		if(tx > 280 && ty > 400 && direct != 0 && direct != 3){
			if(tx < 500 && rot > 179) rot+= 0.75;
			if(tx < 400 && rot > 269) direct = 1;
			else if(tx < 400) direct = 5;
			if(ty < 650 && rot > 180 && rot < 360) rot+= 0.75;
			if(ty < 500 && rot == 0) direct = 0;
			if(rot > 359) rot = 0;
		}
	}
	
    @Override
	public void run()
	{
		while(alive)
		{
			if(running)
			{
				c = null;
				try
				{
					c = surfaceHolder.lockCanvas(null);
					synchronized(surfaceHolder)
					{
						// if (mode == STATE_RUNNING)
						update();
						draw(c);
					}
				}
				finally
				{
					if(c != null)
					{
						surfaceHolder.unlockCanvasAndPost(c);
					}
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
