package com.possethings.quickdraw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity";
	private static final String START_BUTTON_PIN_NAME = "BCM21";
	private Gpio mGPIOStartButton;
	private long lastButtonPress = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PeripheralManagerService service = new PeripheralManagerService();
		try {
			// Set up the start button
			mGPIOStartButton = service.openGpio(START_BUTTON_PIN_NAME);
			mGPIOStartButton.setDirection(Gpio.DIRECTION_IN);
			mGPIOStartButton.setEdgeTriggerType(Gpio.EDGE_FALLING);
			mGPIOStartButton.registerGpioCallback(mCallback);
		} catch (IOException e) {
			Log.e(TAG, "Error on PeripheralIO API", e);
		}
	}

	private GpioCallback mCallback = new GpioCallback() {
		@Override
		public boolean onGpioEdge(Gpio gpio) {
			
			if (System.currentTimeMillis() - lastButtonPress > 200) {
				lastButtonPress = System.currentTimeMillis();
				Log.i(TAG, "Start the game!");
				BuzzerManager buzzerManager = BuzzerManager.getInstance();
				if (buzzerManager.isPlaying()) {
					buzzerManager.stop();
				} else {
					buzzerManager.play();
				}
			}
			return true;
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Step 6. Close the resource
		if (mGPIOStartButton != null) {
			mGPIOStartButton.unregisterGpioCallback(mCallback);
			try {
				mGPIOStartButton.close();
			} catch (IOException e) {
				Log.e(TAG, "Error on PeripheralIO API", e);
			}
		}
	}
}
