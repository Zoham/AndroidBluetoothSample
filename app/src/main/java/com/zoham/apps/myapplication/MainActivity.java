package com.zoham.apps.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter = null;
    private BluetoothDevice btDevice = null;
    private String btDeviceAddress = null;

    private Set<BluetoothDevice> btDeviceSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btInit(View view) {
        try {
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            if (btAdapter == null) {
                Toast.makeText(this, "Bluetooth Not Found On This Device", Toast.LENGTH_SHORT).show();
                finish();
            } else if (!btAdapter.isEnabled()) {
                Toast.makeText(this, "Turn on Bluetooth", Toast.LENGTH_SHORT).show();

                Intent btEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(btEnable, 1);
            } else
                Toast.makeText(this, "Bluetooth Found And Running", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


}
