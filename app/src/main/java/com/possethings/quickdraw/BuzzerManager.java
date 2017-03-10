package com.possethings.quickdraw;

import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

class BuzzerManager {
	private static BuzzerManager instance;
	private static final String LOG_TAG = "BuzzerManager";
	private static final String BUZZER_PWM_PIN_NAME = "PWM0";

	private Pwm mPWMBuzzer;
	private boolean isPlaying = false;
	Timer tuneTimer = new Timer();

	static BuzzerManager getInstance() {
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

	void playTune(List<BuzzerNote> tune) {
		try {
			mPWMBuzzer.setEnabled(true);
		} catch (IOException e) {
			Log.e(TAG, "Error on PeripheralIO API", e);
			return;
		}
		isPlaying = true;
		playTuneNote(tune, 0);
	}

	private void playTuneNote(final List<BuzzerNote> tune, final int noteIndex) {
		try {
			if (noteIndex >= tune.size()) {
				mPWMBuzzer.setEnabled(false);
				return;
			}

			BuzzerNote buzzerNote = tune.get(noteIndex);
			mPWMBuzzer.setPwmFrequencyHz(buzzerNote.getFrequency());
			mPWMBuzzer.setPwmDutyCycle(buzzerNote.getVolume());

			tuneTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					playTuneNote(tune, noteIndex + 1);
				}
			}, buzzerNote.getDuration());
		} catch (IOException e) {
			Log.e(TAG, "Error on PeripheralIO API", e);
		}
	}

	void play() {
		try {
			mPWMBuzzer.setPwmFrequencyHz(1319);
			mPWMBuzzer.setPwmDutyCycle(50);
			mPWMBuzzer.setEnabled(true);
			isPlaying = true;
		} catch (IOException e) {
			Log.e(TAG, "Error on PeripheralIO API", e);
		}
	}

	void stop() {
		try {
			mPWMBuzzer.setEnabled(false);
			isPlaying = false;
		} catch (IOException e) {
			Log.e(TAG, "Error on PeripheralIO API", e);
		}
	}

	boolean isPlaying() {
		return isPlaying;
	}
}
