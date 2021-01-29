package com.app.ble_demo;

import android.bluetooth.BluetoothDevice;

public class BLEDevice {

    BluetoothDevice bluetoothDevice;
    private int rssi;

    public BLEDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getDeviceAddress(){
        return bluetoothDevice.getAddress();
    }

    public String getName(){
        return bluetoothDevice.getName();
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
    }
}
