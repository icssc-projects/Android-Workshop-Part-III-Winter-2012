package edu.icssc.projects.fallingballgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class CustomView extends View implements Runnable, OnTouchListener
{
	private static final float GRAVITY = 1; //pixels per millisecond^2
	
	//time
	private long previousTime;
	private long dT;
	
	//Ball attributes
	private int ballX, ballY, ballR, ballYVelocity;
	
	//Threading
	private Handler handler;
	
	//Style to draw in
	private Paint paint;
	
	//width and height
	private int maxH;
	private int w;
	
	public CustomView(Context context) 
		{super(context);}
	public CustomView(Context context, AttributeSet attrs)
		{super(context, attrs);}
	public CustomView(Context context, AttributeSet attrs, int defStyle)
		{super(context, attrs, defStyle);}
	
	//
	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		maxH = h;
		this.w = w;
		
		paint = new Paint();
		//Red
		paint.setColor(0xFFFF0000);
		paint.setAntiAlias(true);
		
		
		ballX =(int)(Math.random() * w);
		ballY = 0;
		ballR = 20;
		
		
		previousTime = System.currentTimeMillis();
		
		
		handler = new Handler();
		handler.postDelayed(this, 16);
		
		setOnTouchListener(this);
		
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	public void run() 
	{
		dT = System.currentTimeMillis() - previousTime;
		
		ballYVelocity += GRAVITY * dT;
		if(ballYVelocity > 9)
			ballYVelocity = 9;
		
		ballY += ballYVelocity;
		
		
		if(ballY > maxH)
		{
			ballY = 0;
			ballX = (int)(Math.random() * w);
		}
		
		
		invalidate();
		
		
		previousTime = System.currentTimeMillis();
		handler.postDelayed(this, 16);
	}
	
	public boolean onTouch(View v, MotionEvent me)
	{
		if(me.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(inBall(me.getX(), me.getY()))
			{
				if(ballR > 5)
					ballR --;
				ballY = 0;
				ballYVelocity = 0;
				ballX = (int)(Math.random() * w);
				invalidate();
			}
		}
		return true;
	}
	
	private boolean inBall(float x, float y)
	{
		float dx = x - ballX;
		float dy = y - ballY;
		return Math.sqrt(dx * dx + dy * dy) <= ballR;
	}
	
	public void onDraw(Canvas c)
	{
		c.drawColor(0xFFFFFFFF);
		
		c.drawCircle(ballX, ballY, ballR, paint);
	}
	
}
