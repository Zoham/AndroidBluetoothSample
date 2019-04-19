package com.zoham.apps.myapplication;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DevicesListAdapter extends RecyclerView.Adapter<DevicesListAdapter.ListViewHolder> {

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        public View listItem;
        public TextView deviceName;
        public TextView deviceAddress;

        public ListViewHolder(View listItem) {
            super(listItem);

            this.listItem = listItem;
            deviceName = (TextView) listItem.findViewById(R.id.deviceName);
            deviceAddress = (TextView) listItem.findViewById(R.id.deviceAddress);
        }
    }

    private List<BluetoothDevice> deviceSet;

    public DevicesListAdapter(List<BluetoothDevice> deviceSet) {
        this.deviceSet = deviceSet;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ListViewHolder listViewHolder = new ListViewHolder(listItem);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder viewHolder, int i) {
        viewHolder.deviceName.setText(deviceSet.get(i).getName());
        viewHolder.deviceAddress.setText(deviceSet.get(i).getAddress());
    }

    @Override
    public int getItemCount() {
        return deviceSet.size();
    }
}