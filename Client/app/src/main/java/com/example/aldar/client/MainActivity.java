package com.example.aldar.client;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private Button buttonRefresh;
    private EditText editIp;
    private EditText editPort;
    private EditText editInterval;
    private TextView textViewXyValue;
    private TextView textViewXzValue;
    private TextView textViewYzValue;
    private TextView textViewXyAngleValue;

    private SensorManager sensorManager;
    private NetworkTask networkTask;
    private AccelerometerListener accelerometerListener;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        public void run() {
            textViewXyValue.setText(String.valueOf(accelerometerListener.xyAcc));
            textViewXzValue.setText(String.valueOf(accelerometerListener.xzAcc));
            textViewYzValue.setText(String.valueOf(accelerometerListener.yzAcc));

            if (accelerometerListener.GetXyValid() == true)
                textViewXyAngleValue.setText(String.valueOf(accelerometerListener.GetXyAngle()));
            else
                textViewXyAngleValue.setText("Set in XY plane");

            handler.postDelayed(this, 500);

            if (networkTask != null) {
                Exception e = networkTask.GetException();

                if (e != null)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setGUI();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerListener = new AccelerometerListener(sensorManager);
        networkTask = null;

        runnable.run();

        buttonRefresh.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                if (networkTask == null)
                {
                    String ipAddress = editIp.getText().toString();
                    int port = Integer.parseInt(editPort.getText().toString());
                    int interval = Integer.parseInt(editInterval.getText().toString());

                    networkTask = new NetworkTask(accelerometerListener, ipAddress, port, interval);
                    networkTask.execute();
                    buttonRefresh.setText("Stop");
                }
                else
                {
                    networkTask.Stop();
                    networkTask = null;
                    buttonRefresh.setText("Start");
                }
            }
        });
    }

    private void setGUI() {
        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        editIp = (EditText) findViewById(R.id.editIp);
        editPort = (EditText) findViewById(R.id.editPort);
        editInterval = (EditText) findViewById(R.id.editInterval);
        textViewXyValue = (TextView) findViewById(R.id.textViewXyValue);
        textViewXzValue = (TextView) findViewById(R.id.textViewXzValue);
        textViewYzValue = (TextView) findViewById(R.id.textViewYzValue);
        textViewXyAngleValue = (TextView) findViewById(R.id.textViewXyAngleValue);
        editIp.setText("192.168.100.3");
        editPort.setText("11000");
        editInterval.setText("100");
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
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
