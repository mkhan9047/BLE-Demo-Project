package com.app.ble_demo;
import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus;
import com.github.douglasjunior.bluetoothlowenergylibrary.BluetoothLeService;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothService service = null;
    TextView deviceNameTxt;
    public static final int REQUEST_ENABLE_BT = 1;
    public BLEReceiver bleReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bleReceiver = new BLEReceiver(this);
        initViews();
       service =  BluetoothService.getDefaultInstance();
       service.setOnEventCallback(new BluetoothService.OnBluetoothEventCallback() {
           @Override
           public void onDataRead(byte[] buffer, int length) {
               String data = new String(buffer);
               Log.e("DATA_COMING", data);
           }

           @Override
           public void onStatusChange(BluetoothStatus status) {

           }

           @Override
           public void onDeviceName(String deviceName) {
                deviceNameTxt.setText(deviceName);
           }

           @Override
           public void onToast(String message) {

           }

           @Override
           public void onDataWrite(byte[] buffer) {

           }
       });
    }

    private void initViews(){
        deviceNameTxt = findViewById(R.id.txt_device_name);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(bleReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(bleReceiver);
    }

    public void startScan(View view) {
        service.setOnScanCallback(new BluetoothService.OnBluetoothScanCallback() {
            @Override
            public void onDeviceDiscovered(BluetoothDevice device, int rssi) {
                if(device.getName() != null){
                    if(device.getName().contains("BPM")){
                        service.connect(device);
                    }
                }

            }
            @Override
            public void onStartScan() {
                Log.e("D_S", "Started");
            }
            @Override
            public void onStopScan() {
                Log.e("D_S", "Stoped");
            }
        });
        service.startScan();

    }

    public void stopScan() {
        Utils.toast(this, "Scan again....");
    }

}
