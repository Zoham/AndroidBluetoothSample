package com.zoham.apps.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter = null;
    private BluetoothDevice btDevice = null;
    private String btDeviceAddress = null;

    private List<BluetoothDevice> btDeviceSet = new ArrayList<>();

    private Button initialize;
    private Button scan;
    private Button discoverable;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize = (Button) findViewById(R.id.init);
        scan = (Button) findViewById(R.id.scan);
        discoverable = (Button) findViewById(R.id.discoverable);
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

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btDeviceSet.add(device);
            }
        }
    };

    private void setUpDeviceListView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new DevicesListAdapter(btDeviceSet);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void btScan(View view) {
        btDeviceSet.addAll(btAdapter.getBondedDevices());


        if (btAdapter.isDiscovering()) btAdapter.cancelDiscovery();

        setUpDeviceListView();

        IntentFilter foundIntentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(btBroadcastReceiver, foundIntentFilter);

        btAdapter.startDiscovery();
    }

    public void btDeviceDiscoverable(View view) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(btAdapter.EXTRA_DISCOVERABLE_DURATION, 150);
        startActivity(discoverableIntent);

        Toast.makeText(this, "Make device discoverable for 3 min", Toast.LENGTH_SHORT).show();
    }
}
