package com.gottox.keyboardled;

import com.gottox.keyboardled.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.test.IsolatedContext;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class LedActivity extends Activity {
	Handler handler = new Handler();
	ToggleButton enable, autostart, backlight;
	Thread bg = new Thread(new Runnable() {
		@Override
		public void run() {
			while (bg.isAlive()) {
				if (isServiceRunning() != enable.isChecked()) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							enable.setChecked(isServiceRunning());
						}
					});
				}
				if ((Led.getBrightness() != 0) != backlight.isChecked()) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							backlight.setChecked(Led.getBrightness() != 0);
						}
					});
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}
	});
	private OnCheckedChangeListener autostartListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			getSharedPreferences("KeyboardLed", MODE_PRIVATE).edit()
					.putBoolean("autostart", isChecked);
		}
	};

	final OnClickListener buttonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ToggleButton b = (ToggleButton) v;
			switch (b.getId()) {
			case R.id.backlight:
				Led.setBrightness(Led.getBrightness() == 0 ? Led
						.getMaxBrightness() : 0);
				break;
			case R.id.enable:
				Intent i = new Intent(LedActivity.this, LedService.class);
				if(isServiceRunning())
					stopService(i);
				else
					startService(i);
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	Dialog about;
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			about.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.main);

		enable = (ToggleButton) this.findViewById(R.id.enable);
		autostart = (ToggleButton) this.findViewById(R.id.autostart);
		backlight = (ToggleButton) this.findViewById(R.id.backlight);

		enable.setOnClickListener(buttonListener);
		backlight.setOnClickListener(buttonListener);
		autostart.setOnCheckedChangeListener(autostartListener);
		Led.init(this);
		bg.start();
		
		about = new Dialog(this);
		about.setContentView(R.layout.about);
		about.setTitle("About");
	}

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (LedService.class.getCanonicalName().equals(service.service.getClassName())) { 
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		bg.stop();
		super.onDestroy();
	}
}