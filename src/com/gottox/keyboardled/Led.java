package com.gottox.keyboardled;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.util.Log;

final public class Led {
	private final static String BRIGHTNESS_FILE = "/sys/class/leds/keyboard-backlight/brightness";
	private final static String MAX_BRIGHTNESS_FILE = "/sys/class/leds/keyboard-backlight/max_brightness";
	
	static public boolean init(Context context) {
		Process p;
		int uid = context.getApplicationContext().getApplicationInfo().uid;
		try {
			p = Runtime.getRuntime().exec("su");

			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.writeBytes("chown " + uid + " " + BRIGHTNESS_FILE + "\n");
			os.writeBytes("chmod u+rw " + BRIGHTNESS_FILE + "\n");
			os.writeBytes("exit\n");
			os.flush();
			p.waitFor();
			return p.exitValue() == 0;
		} catch (Exception e) {
			return false;
		}
	}
	
	static private int getValue(String path) {
		try {
			FileInputStream fstream = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new DataInputStream(fstream)));
			String ret = br.readLine();
			br.close();
			return Integer.parseInt(ret);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	static public int getMaxBrightness() {
		return getValue(MAX_BRIGHTNESS_FILE);
	}

	static public int getBrightness() {
		return getValue(BRIGHTNESS_FILE);
	}

	static public void setBrightness(int brightness) {
		try {
			FileOutputStream fstream = new FileOutputStream(BRIGHTNESS_FILE);
			fstream.write(("" + brightness + "\n").getBytes("ASCII"));
			fstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
