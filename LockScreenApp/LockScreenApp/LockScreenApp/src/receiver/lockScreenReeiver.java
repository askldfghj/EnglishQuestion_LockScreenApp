package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.lockscreen.LockScreenAppActivity;

public class lockScreenReeiver extends BroadcastReceiver {
	private TelephonyManager telephonymanager = null;
	private boolean isPhoneIdle = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			if (telephonymanager == null) {
				telephonymanager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				telephonymanager.listen(phoneListner,
						PhoneStateListener.LISTEN_CALL_STATE);
			}

			if (isPhoneIdle) {
				Intent i = new Intent(context, LockScreenAppActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
		}
	}

	private PhoneStateListener phoneListner = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				isPhoneIdle = true;
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				isPhoneIdle = false;
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				isPhoneIdle = false;
				break;
			}
		}
	};
}
