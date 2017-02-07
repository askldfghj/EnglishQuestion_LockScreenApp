package com.lockscreen;

import android.app.Application;

public class Global extends Application{
	private int state;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		state = 0;
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	public void setState(){
		this.state++;
	}
	public int getState(){
		return state;
	}
}
