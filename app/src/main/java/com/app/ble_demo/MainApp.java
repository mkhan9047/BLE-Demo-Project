package com.app.ble_demo;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.os.Build;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothlowenergylibrary.BluetoothLeService;

import java.util.UUID;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setupBLE();
    }
    private void setupBLE(){
        BluetoothConfiguration config = new BluetoothConfiguration();
        config.context = getApplicationContext();
        config.bluetoothServiceClass = BluetoothLeService.class;
        config.bufferSize = 1024;
        config.characterDelimiter = '\n';
        config.deviceName = "BPM";
        config.callListenersInMainThread = true;
        //config.uuidService = UUID.fromString("FFF0"); // Required
        config.uuidService = convertFromInteger(0xFFF0);
        config.uuidCharacteristic = convertFromInteger(0xFFF4); //
        // Required
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            config.transport = BluetoothDevice.TRANSPORT_LE; // Required for dual-mode devices
        }
        config.uuid = null;// Used to filter found devices. Set null to find all
        // devices.
        BluetoothService.init(config);
    }

    public UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }

}
