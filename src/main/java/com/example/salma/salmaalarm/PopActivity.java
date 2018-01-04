package com.example.salma.salmaalarm;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class PopActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private final int RANDOM = 0, MUSICAL = 1, COOL = 2, MORNING = 3, NICE = 4, MAXVOLUME = 10;
    private boolean enableVibration;
    private int volume;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        Button end = (Button) findViewById(R.id.turnOffbtn);

        Log.e("in the pop", "yay");
        Log.e("in the service", "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

        // To find the intent which started the Activity.
        Intent intent = getIntent();

        // Get extras From the Alarm receiver.
        int chosenRingtone = intent.getExtras().getInt("ringtone");
        enableVibration = intent.getExtras().getBoolean("enableVibration");
        volume = intent.getExtras().getInt("volume");

        Log.e("volumeeeeee in poop", String.valueOf(volume));


        Log.e("in the service", String.valueOf(chosenRingtone));

        if (enableVibration) vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // specify the ringtone.
        if (chosenRingtone == RANDOM) chosenRingtone = getRandomNumber();

        // Set the ringtone.
        if (chosenRingtone == MUSICAL) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarma_musical);
        } else if (chosenRingtone == COOL) {
            mediaPlayer = MediaPlayer.create(this, R.raw.best_wake_up);
        } else if (chosenRingtone == MORNING) {
            mediaPlayer = MediaPlayer.create(this, R.raw.morning_alarm);
        } else if (chosenRingtone == NICE) {
            mediaPlayer = MediaPlayer.create(this, R.raw.nice_wake_up_alarm);
        } else {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarma_musical);
        }
        // Volume Settings.
        float log = (float)(Math.log(MAXVOLUME-volume) / Math.log(MAXVOLUME));
        mediaPlayer.setVolume(1-log, 1-log);
        mediaPlayer.start();

        // Vibrate if the vibration was set.
        if (enableVibration) {
            // Vibration pattern, numbers for the strength of vibration.
            // TODO need to slow down the rate of vibration.
            long[] pattern = {0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 50};
            vibrator.vibrate(pattern, 0);
            Log.e("pop", "vibration on");
        }
        Log.e("pop", "playing");
        // Stop ringing.
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("play", "end pressed ");
                mediaPlayer.stop();
                mediaPlayer.reset();
                if (enableVibration) vibrator.cancel();
                // We need to tell the MainActivity that we stopped the alarm.
                onBackPressed();
            }
        });

        // Snoozing.
        //TODO snoozing :D.


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private int getRandomNumber() {
        int n = (int) (Math.random() * 4) + 1;
        Log.e("in service ", String.valueOf(n));
        return n;
    }





    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Pop Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
