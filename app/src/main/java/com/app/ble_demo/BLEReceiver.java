package com.app.ble_demo;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BLEReceiver extends BroadcastReceiver {

    Context mainContext;

    public BLEReceiver(Context context) {
        this.mainContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action != null){
                if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);
                    switch (state){
                        case BluetoothAdapter.STATE_OFF:
                            Toast.makeText(mainContext, "Bluetooth Off", Toast.LENGTH_SHORT).show();
                            break;

                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Toast.makeText(mainContext, "Bluetooth Turning Off",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        case BluetoothAdapter.STATE_ON:
                            Toast.makeText(mainContext, "Bluetooth ON", Toast.LENGTH_SHORT).show();
                            break;

                        case BluetoothAdapter.STATE_TURNING_ON:
                            Toast.makeText(mainContext, "Bluetooth Turning ON",
                                    Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
            }
    }


}
