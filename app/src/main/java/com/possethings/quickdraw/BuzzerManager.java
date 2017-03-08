package com.possethings.quickdraw;

import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

import static android.content.ContentValues.TAG;

class BuzzerManager {
	private static BuzzerManager instance;
	private static final String LOG_TAG = "BuzzerManager";
	private static final String BUZZER_PWM_PIN_NAME = "PWM0";

	private Pwm mPWMBuzzer;
	private boolean isPlaying = false;

	public static BuzzerManager getInstance() {
		if (instance == null) {
			instance = new BuzzerManager();
		}
		return instance;
	}

	private BuzzerManager() {
		// Set up the buzzer
		PeripheralManagerService service = new PeripheralManagerService();
		try {
			mPWMBuzzer = service.openPwm(BUZZER_PWM_PIN_NAME);
			mPWMBuzzer.setEnabled(false);
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error setting up buzzer", e);
		}
	}

	public void play() {
		try {
			mPWMBuzzer.setPwmFrequencyHz(500);
			mPWMBuzzer.setPwmDutyCycle(5);
			mPWMBuzzer.setEnabled(true);
			isPlaying = true;
		} catch (IOException e) {
			Log.e(TAG, "Error on PeripheralIO API", e);
		}
	}

	public void stop() {
		try {
			mPWMBuzzer.setEnabled(false);
			isPlaying = false;
		} catch (IOException e) {
			Log.e(TAG, "Error on PeripheralIO API", e);
		}
	}

	public boolean isPlaying() {
		return isPlaying;
	}
}
