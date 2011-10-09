package com.gottox.keyboardled;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

public class LedService extends Service {

	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Led.init(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		switch(newConfig.hardKeyboardHidden) {
		case Configuration.HARDKEYBOARDHIDDEN_YES:
			Led.setBrightness(0);
			break;
		case Configuration.HARDKEYBOARDHIDDEN_NO:
			int max = Led.getMaxBrightness();
			Led.setBrightness(max);
			break;
		default:
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onDestroy() {
		Led.setBrightness(0);
		super.onDestroy();
	}

}
