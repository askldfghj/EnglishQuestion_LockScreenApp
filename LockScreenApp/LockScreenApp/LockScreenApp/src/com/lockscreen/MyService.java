package com.lockscreen;

import receiver.lockScreenReeiver;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class MyService extends Service {
	private BroadcastReceiver mReceiver = null;

	// Intent myIntent;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mReceiver = new lockScreenReeiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		if(intent != null){
			if(intent.getAction()==null){
				if(mReceiver==null){
					mReceiver = new lockScreenReeiver();					
					IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
					registerReceiver(mReceiver, filter);
				}
			}
		}
		return START_REDELIVER_INTENT;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}
}
