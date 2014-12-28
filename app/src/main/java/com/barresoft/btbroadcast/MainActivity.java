package com.barresoft.btbroadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static java.lang.Thread.sleep;


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final UUID MY_UUID = null;
    private BluetoothSocket mmSocket=null;
    private BluetoothDevice mmDevice=null;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //UTIL------------------------------------------------------------------------------
    private void msg(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void log(String log){
        try {
            Log.d(null, log);
        }catch (Exception e){
            Log.e(null, "(vacío)");
        };
    }
    //UTIL------------------------------------------------------------------------------

    public void scan(View view){
        log("SCAN START");
        Button btnScan = (Button)findViewById(R.id.btnScan);
        btnScan.setEnabled(false);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            msg("Device does not support Bluetooth");
        }else{
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }else{ //is enabled
                msg("BT Already on..." + "\n" +
                     "SCAN START");

                pairedDevices = BA.getBondedDevices();

                ArrayList list = new ArrayList();
                for(BluetoothDevice bt : pairedDevices){
                    list.add(bt.getName());
                    msg(bt.getName());
                }

                // Create a BroadcastReceiver for ACTION_FOUND
                /*final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        log("1");
                        String action = intent.getAction();
                        // When discovery finds a device
                        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                            log("2");
                            // Get the BluetoothDevice object from the Intent
                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            // Add the name and address to an array adapter to show in a ListView
                            //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                            msg(device.getName() + "\n" + device.getAddress());
                        }
                    }
                    public void onDestroy(){
                        super.onDestroy();
                        Log.d("onDestroy called!", "onDestroy called!");
                        unregisterReceiver(mReceiver);
                    }
                };

                // Register the BroadcastReceiver
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
                log(mReceiver.getResultData());
                */
            }
        }
        log("SCAN STOP");
        //btnScan.setEnabled(true);
    }

    private void ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run(BluetoothAdapter mBluetoothAdapter) {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        //manageConnectedSocket(mmSocket);
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
