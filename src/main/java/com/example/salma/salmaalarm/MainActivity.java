package com.example.salma.salmaalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Calendar;

// MainActivity takes to AlarmReceiver, then the AlarmReceiver will send signal to RingtoneService,
// why the MainActivity can't talk to the RingtoneService, because if it does the song will go on
// so the AlarmReceiver allow us to have the song playing after a certain amount of time.

// TODO when you set the alarm more than once and the time is not near it gose on thought.
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // variables.
    private final String STATUS =  "Your alarm is ";
    private AlarmManager alarmManager;
    private TimePicker alarmTime;
    private TextView status;
    private boolean alarmIsOn;
    private int chosenRingTone, volumeLevel, MAXVOLUME = 10;
    // I don't know why we need this.
    private Context context;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing volume level.
        volumeLevel = MAXVOLUME;

        Log.e("the booooooooolean ", String.valueOf(alarmIsOn));

        this.context = this;
        // Initializing alarm manager.
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Initializing Time picker.
        alarmTime = (TimePicker) findViewById(R.id.timePicker);
        // Initializing the status textView.
        status = (TextView) findViewById(R.id.status);
        // Initializing calender object.
        final Calendar calendar = Calendar.getInstance();

        // Vibration toggle button initialization.
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);

        // Volume bar settings;
        SeekBar volumeSeekBar = (SeekBar) findViewById(R.id.VoluneSeekBar);
        volumeSeekBar.setMax(MAXVOLUME);
        volumeSeekBar.setProgress(MAXVOLUME);

        Button start = (Button) findViewById(R.id.start_alarm);

        // Creating the spinner.
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Setting up the array of the spinner and its layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ringtones, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // OnItemSelectedListener of the spinner.
        spinner.setOnItemSelectedListener(this);

        // Create an intent for AlarmReceiver (will go to AlarmReceiver class).
        final Intent alarmReceiverIntent = new Intent(this.context, AlarmReceiver.class);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled, enable vibration.
                    alarmReceiverIntent.putExtra("enableVibration", true);
                    Log.e("toggle", "on");
                } else {
                    // The toggle is disabled, disable vibration.
                    alarmReceiverIntent.putExtra("enableVibration", false);
                    Log.e("toggle", "off");
                }
            }
        });

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("volume", String.valueOf(progress));
                volumeLevel = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Onclick listener.
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!alarmIsOn){ // Alarm is off.
                    alarmIsOn = true;
                    String hr = "", min = "", str = "";
                    if (Build.VERSION.SDK_INT >= 23) {
                        // set the calender.
                        calendar.set(Calendar.HOUR_OF_DAY, alarmTime.getHour());
                        hr = String.valueOf(alarmTime.getHour());
                        calendar.set(Calendar.MINUTE, alarmTime.getMinute());
                        min = String.valueOf(alarmTime.getMinute());
                    } else {
                        // set the calender.
                        calendar.set(Calendar.HOUR_OF_DAY, alarmTime.getCurrentHour());
                        hr = String.valueOf(alarmTime.getCurrentHour());
                        calendar.set(Calendar.MINUTE, alarmTime.getCurrentMinute());
                        min = String.valueOf(alarmTime.getCurrentMinute());
                    }
                    str = Integer.parseInt(hr) > 12 ? String.valueOf(Integer.parseInt(hr) - 12) + ":" + min +"Pm" : hr + ":" + min+  " Am";
                    setAlarmText("Alarm On " + str);


                    Log.e("hours ", hr);
                    Log.e("Min", min);

                    // Adding extra string in the Intent to tell you started the alarm.
                    alarmReceiverIntent.putExtra("ringtone", chosenRingTone);
                    alarmReceiverIntent.putExtra("volume", volumeLevel);

                    // pending intent is the intent that delays the intent
                    // until the specified calender time.
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // setting the AlarmManager
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    // after all that we need to set the manifest to allow broadcasting.  <receiver android:name=".AlarmReceiver"></receiver>
                } else {  // Alarm is on.
                    CharSequence text = "Alarm already set!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }

        });


    }

    private void setAlarmText(String s) {
        status.setText(STATUS + s);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // This will be called when the selected item change.
        chosenRingTone = (int)id; // This will be sent as extra to the Service.

       // chosenRingTone = parent.getItemIdAtPosition(position);
        Log.e("Ringtone mainActivity", String.valueOf(chosenRingTone));
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, "Ringtone mainActivity", duration);
        toast.show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
