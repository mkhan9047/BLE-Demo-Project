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

import com.ficat.easyble.BleDevice;
import com.ficat.easyble.BleManager;
import com.ficat.easyble.gatt.callback.BleConnectCallback;
import com.ficat.easyble.gatt.callback.BleNotifyCallback;
import com.ficat.easyble.scan.BleScanCallback;

import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    BleManager bleManager;
    TextView deviceNameTxt;
    public static final int REQUEST_ENABLE_BT = 1;
    public BLEReceiver bleReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bleReceiver = new BLEReceiver(this);
        initViews();
        initBLE();
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

    private void initBLE(){
        BleManager.ConnectOptions connectOptions = BleManager.ConnectOptions
                .newInstance()
                .connectTimeout(12000);
         bleManager = BleManager
                .getInstance()
                .setConnectionOptions(connectOptions)//like scan options
                .setLog(true, "DEBUG_BLE")
                .init(this.getApplication());
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(bleReceiver);
    }

    public void startScan(View view) {
        bleManager.startScan(new BleScanCallback() {
            @Override
            public void onLeScan(BleDevice device, int rssi, byte[] scanRecord) {
                String name = device.name;
                String address = device.address;
                Log.e("BLE_NAME", name);
                Log.e("BLE_ADDRESS", address);
                       // if(name.contains("BPM")){
                            deviceNameTxt.setText(name);
                            connect(device);
                            bleManager.stopScan();
                        //}

            }

            @Override
            public void onStart(boolean startScanSuccess, String info) {
                if (startScanSuccess) {
                    //start scan successfully
                } else {
                    //fail to start scan, you can see details from 'info'
                    String failReason = info;
                    Log.e("BLE_INFO", info);
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void connect(BleDevice device){
        BleConnectCallback bleConnectCallback = new BleConnectCallback() {
            @Override
            public void onStart(boolean startConnectSuccess, String info, BleDevice device) {
                if (startConnectSuccess) {
                    //start to connect successfully
                } else {
                    //fail to start connection, see details from 'info'
                    String failReason = info;
                }
            }

            @Override
            public void onFailure(int failCode, String info, BleDevice device) {
                if(failCode == BleConnectCallback.FAIL_CONNECT_TIMEOUT){
                    //connection timeout
                }else{
                    //connection fail due to other reasons
                }
                Log.e("BLE_FAILING_INFO", info);
            }

            @Override
            public void onConnected(BleDevice device) {
                //device is connected
                bleManager.notify(device, convertFromInteger(0xFFF0).toString(),
                        convertFromInteger(0xFFF4).toString(), new BleNotifyCallback() {
                            @Override
                            public void onCharacteristicChanged(byte[] data, BleDevice device) {
                                    String dataString = new String(data);
                                    Log.e("RESULT", dataString);
                            }

                            @Override
                            public void onNotifySuccess(String notifySuccessUuid, BleDevice device) {
                                    Log.e("BLE_NOTIFY", "SUCCESS");
                            }

                            @Override
                            public void onFailure(int failCode, String info, BleDevice device) {
                                Log.e("BLE_NOTIFY", info);
                            }
                        });
            }

            @Override
            public void onDisconnected(String info, int status, BleDevice device) {

            }
        };
        bleManager.connect(device, bleConnectCallback);
    }


    public UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }

}
