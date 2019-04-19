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

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter = null;
    private BluetoothDevice btDevice = null;
    private String btDeviceAddress = null;

    private Set<BluetoothDevice> btDeviceSet = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(btBroadcastReceiver);
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

    private final BroadcastReceiver btBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btDeviceSet.add(device);
            }
        }
    };

    private void setUpDeviceListView() {

    }

    public void btScan(View view) {
        btDeviceSet.addAll(btAdapter.getBondedDevices());


        if(btAdapter.isDiscovering()) btAdapter.cancelDiscovery();

        setUpDeviceListView();

        IntentFilter foundIntentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(btBroadcastReceiver, foundIntentFilter);

        btAdapter.startDiscovery();
    }

    public void btDeviceDiscoverable(View view) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(btAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }
}
