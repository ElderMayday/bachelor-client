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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private Button buttonRefresh;
    private Button buttonSearch;
    private Button buttonCopy;
    private Button buttonGauge;

    private EditText editIp;
    private EditText editPort;
    private EditText editInterval;

    private TextView textViewIpProposedValue;
    private TextView textViewXValue;
    private TextView textViewYValue;
    private TextView textViewZValue;
    private TextView textViewPitchValue;
    private TextView textViewRollValue;
    private TextView textViewYawValue;
    private TextView textViewGyroPitchValue;
    private TextView textViewGyroRollValue;
    private TextView textViewGyroYawValue;
    private RadioButton radioAM;

    private SensorManager sensorManager;
    private SensorListener sensorListener;

    private NetworkTask networkTask;
    private UdpTask udpTask;



    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        public void run() {
            textViewXValue.setText(String.valueOf(sensorListener.GetXAcc()));
            textViewYValue.setText(String.valueOf(sensorListener.GetYAcc()));
            textViewZValue.setText(String.valueOf(sensorListener.GetZAcc()));

            textViewPitchValue.setText(String.valueOf(sensorListener.GetPitchAM()));
            textViewRollValue.setText(String.valueOf(sensorListener.GetRollAM()));
            textViewYawValue.setText(String.valueOf(sensorListener.GetYawAM()));

            textViewGyroPitchValue.setText(String.valueOf(sensorListener.GetPitchG()));
            textViewGyroRollValue.setText(String.valueOf(sensorListener.GetRollG()));
            textViewGyroYawValue.setText(String.valueOf(sensorListener.GetYawG()));

            if (udpTask != null) {
                if (!udpTask.GetIpProposed().equals("none")) {
                    textViewIpProposedValue.setText(udpTask.GetIpProposed());
                    buttonSearch.setText("Search");
                    buttonCopy.setEnabled(true);
                    buttonRefresh.setEnabled(true);
                    udpTask = null;
                }
            }

            handler.postDelayed(this, 100);

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
        sensorListener = new SensorListener(sensorManager);
        networkTask = null;

        runnable.run();

        buttonRefresh.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                boolean modeAm = radioAM.isChecked();

                if (networkTask == null) {
                    String ipAddress = editIp.getText().toString();
                    int port = Integer.parseInt(editPort.getText().toString());
                    int interval = Integer.parseInt(editInterval.getText().toString());

                    networkTask = new NetworkTask(sensorListener, ipAddress, port, interval, modeAm);
                    networkTask.execute();

                    buttonRefresh.setText("Stop");
                    buttonCopy.setEnabled(false);
                    buttonSearch.setEnabled(false);
                } else {
                    networkTask.Stop();
                    networkTask = null;

                    buttonRefresh.setText("Start");
                    buttonCopy.setEnabled(true);
                    buttonSearch.setEnabled(true);
                }
            }
        });

        buttonSearch.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                buttonSearch.setText("Searching");
                buttonCopy.setEnabled(false);
                buttonRefresh.setEnabled(false);

                udpTask = new UdpTask();
                udpTask.execute();
            }
        });

        buttonCopy.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                editIp.setText(textViewIpProposedValue.getText());
            }
        });

        buttonGauge.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                sensorListener.GaugeGyro();
            }
        });
    }

    private void setGUI() {
        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonCopy = (Button) findViewById(R.id.buttonCopy);
        buttonGauge = (Button) findViewById(R.id.buttonGauge);

        editIp = (EditText) findViewById(R.id.editIp);
        editPort = (EditText) findViewById(R.id.editPort);
        editInterval = (EditText) findViewById(R.id.editInterval);

        textViewIpProposedValue =  (TextView) findViewById(R.id.textViewIpProposedValue);
        textViewXValue = (TextView) findViewById(R.id.textViewXValue);
        textViewYValue = (TextView) findViewById(R.id.textViewYValue);
        textViewZValue = (TextView) findViewById(R.id.textViewZValue);
        textViewPitchValue = (TextView) findViewById(R.id.textViewPitchValue);
        textViewRollValue = (TextView) findViewById(R.id.textViewRollValue);
        textViewYawValue = (TextView) findViewById(R.id.textViewYawValue);
        textViewGyroPitchValue = (TextView) findViewById(R.id.textViewPitchGyroValue);
        textViewGyroRollValue = (TextView) findViewById(R.id.textViewRollGyroValue);
        textViewGyroYawValue = (TextView) findViewById(R.id.textViewYawGyroValue);
        radioAM = (RadioButton) findViewById(R.id.radioAM);


        editIp.setText("192.168.100.1");
        editPort.setText("11000");
        editInterval.setText("40");
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
