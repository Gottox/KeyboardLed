package com.gottox.keyboardled;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LedReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, LedService.class));
	}
}
