package com.btmat.btmat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothActivity";
     String address = "20:17:04:24:86:21";
     BluetoothThread btt;
    Handler writeHandler;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.res_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_connect:


                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


                if(mBluetoothAdapter.isEnabled()==false)
                {
                    mBluetoothAdapter.enable();
                }
                while(mBluetoothAdapter.isEnabled()==false);
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                List<String> s = new ArrayList<String>();
                for(BluetoothDevice bt : pairedDevices) {
                    s.add(bt.getName());
                    s.add(bt.getAddress());
                }


                View v=findViewById(R.id.button);

                PopupMenu popup = new PopupMenu(MainActivity.this,v,Gravity.RIGHT);

                for(int i=0;i<s.size();i+=2)
                {
                    popup.getMenu().add(Menu.NONE,i,i,s.get(i+1).toString() + " (" + s.get(i).toString() + ")");

                }


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        address=item.getTitle().toString().substring(0,17);
                        CreateBTThread();
                        return true;
                    }
                });


                popup.show();
               // popup.getMenuInflater()
                        //.inflate(R.menu.popup_list, popup.getMenu());



                break;

            default:
                break;
        }

        return true;
    }

    @SuppressLint("HandlerLeak")
    void CreateBTThread()
    {

         btt = new BluetoothThread(address, new  Handler() {

            @Override
            public void handleMessage(Message message) {

                String s = (String) message.obj;

                // Do something with the message
                if (s.equals("CONNECTED")) {
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                } else if (s.equals("DISCONNECTED")) {
                    Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
                } else if (s.equals("CONNECTION FAILED")) {
                    Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                } else {
                    TextView tv = (TextView) findViewById(R.id.TextBox1);
                    tv.setText(s);

                }
            }
        });

        writeHandler = btt.getWriteHandler();
        btt.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

       // Toast.makeText(getApplicationContext(),"Connecting", Toast.LENGTH_SHORT).show();
    }

}
