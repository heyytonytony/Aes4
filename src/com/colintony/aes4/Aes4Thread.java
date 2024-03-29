package com.colintony.aes4;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

public class Aes4Thread extends Thread
{

	public static final String PREFS_NAME = "Aes4Preferences";
	static SharedPreferences saves;
	SharedPreferences.Editor editor;
	
	private boolean alive, running;
	private Canvas c;
	private SurfaceHolder surfaceHolder = null;
	private Context cont;
	
    //private float Width = 800;
    //private float Height = 480;
    private float oscale = 0;
    
    public float mx, my, mx2, my2;
    public float tx, ty;
    public float rot;
    
    public int newDir = 0;
    public int X = 0;
	public int frame = 0;
	public int direct = 0;
	public int state = 0;
	public int turn = 0;
	//right0,up1,left2,down3
    private Paint paint = new Paint();
    
    private Bitmap[] pngs = new Bitmap[25];
    
    private int[] turns = new int[3];
    private int[] var = new int[4];
    
    private int after;
    private int level;
    
	public Aes4Thread(SurfaceHolder holder, Context context, Handler handler)
	{
		surfaceHolder = holder;
		cont = context;
		pngs[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.level1d);
		pngs[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.train);
		pngs[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow);
		pngs[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
		pngs[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.won);
		
		ty = 500;
		tx = -85;
		alive = true;
		
		var[0] = 2;
		var[1] = 3;
		var[2] = 3;
		var[3] = 15;
		state = 0;
		level = 1;
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
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(500);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.SANS_SERIF);
		
        
		canvas.save();
    	canvas.scale(oscale,oscale,0,0);
    	
    	//bg
    	canvas.drawBitmap(pngs[0], mx, my, null);
    	
    	paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        canvas.drawText(""+var[0], mx+250, my+730, paint);
        canvas.drawText(""+var[1], mx+250, my+370, paint);
        canvas.drawText(""+var[2], mx+644, my+718, paint);
        paint.setColor(0xff3b668e);
        canvas.drawText(""+var[3], mx+585, my+330, paint);
        canvas.drawText(""+var[3], mx+730, my+330, paint);
    	
    	//connection arrows
    	switch(turns[0])
    	{
    		case 0:
    			//right
    			canvas.drawBitmap(pngs[2], mx + 280, my + 535, null);
    			break;
    			
    		case 1:
    			//down
        		canvas.save();
        		canvas.rotate(90, mx + 280 + 25,  my + 535 + 19);
        		canvas.drawBitmap(pngs[2], mx + 280, my + 535, null);
        		canvas.restore();
        		break;
        		
    		case 2:
    			//up
        		canvas.save();
        		canvas.rotate(-90, mx + 280 + 25,  my + 535 + 19);
        		canvas.drawBitmap(pngs[2], mx + 280, my + 535, null);
        		canvas.restore();
        		break;
    	}
    	switch(turns[1])
    	{
    		case 0:
    			//right
    			canvas.drawBitmap(pngs[2], mx + 615, my + 535, null);
    			break;
    			
    		case 1:
    			//up
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
        paint.setTextSize(20);
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
		
		//win screen
		if(state == 100) canvas.drawBitmap(pngs[4], mx, my, null);
		
		//level
        paint.setStyle(Paint.Style.FILL);
    	paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        canvas.drawText("level: " + level,85+mx, 40+my, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		paint.setAntiAlias(true);
        canvas.drawText("level: " + level,85+mx, 40+my, paint);
        
        canvas.restore();
	}
	
	

	
	
	private void update()
	{
		frame++;
		if(state == 100) after++;
		
		//Pan across screen
		if(mx2>mx)mx+=(mx2-mx)/3;
		else if(mx2<mx)mx-=(mx-mx2)/4;
	
		if(my2>my)my+=(my2-my)/3;
		else if(my2<my)my-=(my-my2)/4;
		
		//Train movement
		if(frame > 330 && direct == 0) tx +=1.5;
		if (direct == 1) ty -=1.5;
		if (direct == 2) tx -=1.5;
		if (direct == 3) ty +=1.5;
		
		//Direction
		rot = (rot + 360) % 360;
		if(rot == 0) direct = 0; //right
		else if(rot == 270) direct = 1; //up
		else if(rot == 180) direct = 2; //left
		else if(rot == 90) direct = 3; //down
		
		//Turns
		if(ty > 499 && ty < 501)
		{
			//first switch station
			switch(turns[0])
			{
				case 0:
					//right
					if(tx > 175 && tx < 177)
					{
						newDir = 0;
						Log.d("first switch", "going straight (right)");
					}
					break;
					
				case 1:
					//down
					if(tx > 175 && tx < 177)
					{
						newDir = 3;
						state = 1;
						Log.d("first switch", "going down");
					}
					break;
					
				case 2:
					//up
					if(tx > 70 && tx < 72)
					{
						newDir = 1;
						state = 2;
						Log.d("first switch", "going up");	
					}
					break;
			}
			
			//second switch station
			switch(turns[1])
			{
				case 0:
					//right
					if(tx > 404 && tx < 406)
					{
						newDir = 0;
						state = 3;
						Log.d("second switch", "going straight (right)");
					}
					break;
					
				case 1:
					//up
					if(tx > 404 && tx < 406)
					{
						newDir = 1;
						state = -1;
						Log.d("second switch", "going up");
					}
					break;
			}
		}
		
		//state1 loop
		if(state == 1)
		{
			//first turn
			if(turn == 0 && ty > 556 && ty < 558) //tx == 356.0
			{
				newDir = 2;
				turn++;
			}
			//second turn
			else if(turn == 1 && tx > 213 && tx < 215) //ty == 737.0
			{
				newDir = 1;
				turn++;
			}
			//third turn
			else if(turn == 2 && ty > 679 && ty < 681) //tx == 33.5
			{
				newDir = 0;
				state = 0;
				turn = 0;
			}
			
			//performing state operation
			if(tx > 284 && tx < 286 && ty > 736 && ty < 738) X += var[0];
		}
		
		//state2 loop
		if(state == 2)
		{
			//first turn
			if(turn == 0 && direct == 1 && ty > 569 && ty < 571) //tx == 251.0
			{
				newDir = 2;
				turn++;
			}
			//second turn
			else if(turn == 1 && direct == 2 && tx > 370 && tx < 372)
			{
				newDir = 3;
				turn++;
			}
			//third turn
			else if(turn == 2 && direct == 3 && ty > 319 && ty < 321) //tx == 191.0
			{
				newDir = 0;
				state = 0;
				turn = 0;
			}
			
			//performing state operation
			if(tx > 319 && tx < 321 && ty > 355 && ty < 357) X += var[1];
		}
		
		//state3 loop
		if(state == 3)
		{
			//first turn
			if(turn == 0 && tx > 673 && tx < 675) //ty == 500.0
			{
				newDir = 3;
				turn++;
			}
			//second turn
			else if(turn == 1 && ty > 553 && ty < 555) //tx == 854.0
			{
				newDir = 2;
				turn++;
			}
			//third turn
			else if(turn == 2 && tx > 559 && tx < 561) //ty == 732.5
			{
				newDir = 1;
				turn++;
			}
			//fourth turn
			else if(turn == 3 && ty > 679 && ty < 681) //tx == 380.0
			{
				newDir = 0;
				state = 0;
				turn = 0;
			}
			
			//performing state operation
			if(tx > 684 && tx < 686 && ty > 733 && ty < 735) X *= var[2];
		}
		
		//state -1, going going towards blue checkpoint
		if(state == -1)
		{
			if(X != var[3] && ty > 497 && ty < 499)
			{
				newDir = 2;
				turn++;
			}
			else if(X == var[3] && ty > 393 && ty < 395)
			{
				newDir = 0;
			}
			else if(turn == 1 && tx > 288 && tx < 290)
			{
				newDir = 3;
				turn++;
			}
			else if(turn == 2 && ty > 319 && ty < 321)
			{
				newDir = 0;
				state = 0;
				turn = 0;
			}
			
			if(tx > 919 && tx < 921)
			{
				state = 100;
			}
		}
		
		//win!
		if(state == 100)
		{
			tx -= 1.5;
		}
		
		//changing directions
		if(newDir != direct)
		{
			switch(newDir)
			{
				case 0:
					if(direct == 1) rot += 0.75;
					else if(direct == 3) {rot -= 0.75; tx -= 1;}
					break;
					
				case 1:
					if(direct == 2) rot += 0.75;
					else if(direct == 0) {rot -= 0.75; ty += 1;}
					break;
					
				case 2:
					if(direct == 3) rot += 0.75;
					else if(direct == 1) {rot -= 0.75; tx += 1;}
					break;
					
				case 3:
					if(direct == 0) rot += 0.75;
					else if(direct == 2) {rot -= 0.75; ty -= 1;}
					break;
			}
		}

		Log.d("lala", "tx: " + tx + "   ty: " + ty + "  rot: " + rot + "  direct: " + direct + "  newdir: " + newDir);
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
    	
    	if(after > 100){
    		after = 0;
    		state = 0;
    		
    		ty = 500;
    		tx = -85;
    		
    		var[0] = (int)(4*Math.random()+1);
    		var[1] = (int)(4*Math.random()+1);
    		var[2] = (int)(6*Math.random()+1);
    		var[3] = ((var[0]*(int)(2*Math.random()+1))+(var[1]*(int)(2*Math.random()+1)))*(var[2]*(int)(Math.random()+1));
    		
    		frame = 0;
    		level++;
    		
            saves = cont.getSharedPreferences(PREFS_NAME, 0);
            editor = saves.edit();
            int max = saves.getInt("max", 0);
            
    		if( level > max ){
    			max = level;
    		    editor.putInt("max", max);
    		    editor.commit();
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
