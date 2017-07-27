package com.btmat.btmat;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothActivity";
    private final String address = "20:16:07:04:81:81";
    BluetoothThread btt;
    Handler writeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btt = new BluetoothThread(address, new Handler() {

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
        Toast.makeText(getApplicationContext(),"Connecting", Toast.LENGTH_SHORT).show();
    }
}
