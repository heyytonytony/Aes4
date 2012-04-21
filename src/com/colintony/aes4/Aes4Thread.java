package com.colintony.aes4;

import android.content.Context;
import android.os.Handler;
import android.view.SurfaceHolder;

public class Aes4Thread extends Thread
{

	private boolean running;
	
	public Aes4Thread(SurfaceHolder holder, Context context, Handler handler)
	{
		// TODO Auto-generated constructor stub
	}

	public void setRunning(boolean b)
	{
		running = b;
		
	}

	public void killThread()
	{
		// TODO Auto-generated method stub
		
	}

}
