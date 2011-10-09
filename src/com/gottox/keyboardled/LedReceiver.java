package com.gottox.keyboardled;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LedReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(context.getSharedPreferences("KeyboardLed", Context.MODE_PRIVATE).getBoolean("autostart", false))
			context.startService(new Intent(context, LedService.class));
	}
}
