package com.possethings.quickdraw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "ButtonActivity";
	private static final String GPIO_PIN_NAME = "BCM21";

	private Gpio mButtonGpio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PeripheralManagerService service = new PeripheralManagerService();
		try {
			// Step 1. Create GPIO connection.
			mButtonGpio = service.openGpio(GPIO_PIN_NAME);
			// Step 2. Configure as an input.
			mButtonGpio.setDirection(Gpio.DIRECTION_IN);
			// Step 3. Enable edge trigger events.
			mButtonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);
			// Step 4. Register an event callback.
			mButtonGpio.registerGpioCallback(mCallback);
		} catch (IOException e) {
			Log.e(TAG, "Error on PeripheralIO API", e);
		}
	}

	// Step 4. Register an event callback.
	private GpioCallback mCallback = new GpioCallback() {
		@Override
		public boolean onGpioEdge(Gpio gpio) {
			Log.i(TAG, "GPIO changed, button pressed");

			// Step 5. Return true to keep callback active.
			return true;
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Step 6. Close the resource
		if (mButtonGpio != null) {
			mButtonGpio.unregisterGpioCallback(mCallback);
			try {
				mButtonGpio.close();
			} catch (IOException e) {
				Log.e(TAG, "Error on PeripheralIO API", e);
			}
		}
	}
}
