package com.example.salma.salmaalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;



public class RingtonePlayingService extends Service {
    private MediaPlayer mediaPlayer;
    private int startId;
    private boolean isRunning;
    private final int RANDOM = 0, MUSICAL = 1, COOL = 2, MORNING = 3, NICE = 4;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("in the service", "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

        // Get extras From the Alarm receiver.
        String UserPressed = intent.getExtras().getString("buttonPressed");
        int chosenRingtone = intent.getExtras().getInt("ringtone");

        Log.e("in the service", UserPressed);
        Log.e("in the service", String.valueOf(chosenRingtone));


        // Will know from boolean music_is_playing and id of the signal id = 0 (broadcast) id = 1 ()
        assert UserPressed != null;
        switch (UserPressed){
            case "Alarm On":
                startId = 1;
                break;
            case "Alarm Off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        // No music playing and user pressed Alarm On the music should start.
        if (!this.isRunning && startId == 1) {

            // Setting the notification using the notification manager.
            NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // When the notification pops up we need the main activity to pop up using an intent
            // that goes to mainActivity.
            // But I want that to happen when the alarm goes off so we need pending intent.
            Intent intentMainActivity = new Intent(this.getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentMainActivity, 0);

            // Make the Notification parameters.
            Notification notification;
            if (Build.VERSION.SDK_INT >= 16) {
                Log.e("in >= 16 ", "notification");
                notification = new Notification.Builder(this)
                        .setContentTitle("Want Alarm to Go Off!")
                        .setContentText("Click Me")
                        .setContentIntent(pendingIntent)    // takes the pending intent.
                        .setAutoCancel(true) // when we click on it , this make it disappear so we don't have to manually do it.
                        .build();

            } else {
                Log.e("in < 16 ", "notification");
                notification = new Notification.Builder(this)
                        .setContentTitle("Want Alarm to Go Off!")
                        .setContentText("Click Me")
                        .setContentIntent(pendingIntent)    // takes the pending intent.
                        .setAutoCancel(true) // when we click on it , this make it disappear so we don't have to manually do it.
                        .getNotification();
            }
            // I don't know what the value 0 means.
            notificationManager.notify(0, notification);



            // specify the ringtone.
            if (chosenRingtone == RANDOM){
                chosenRingtone = getRandomNumber();
            }

            if (chosenRingtone == MUSICAL) {
                mediaPlayer = MediaPlayer.create(this, R.raw.alarma_musical);
                mediaPlayer.start();
            } else if (chosenRingtone == COOL) {
                mediaPlayer = MediaPlayer.create(this, R.raw.best_wake_up);
                mediaPlayer.start();
            } else if (chosenRingtone == MORNING) {
                mediaPlayer = MediaPlayer.create(this, R.raw.morning_alarm);
                mediaPlayer.start();
            } else if (chosenRingtone == NICE) {
                mediaPlayer = MediaPlayer.create(this, R.raw.nice_wake_up_alarm);
                mediaPlayer.start();
            }

            Log.e("play", "case 1");
            this.isRunning = true;
            this.startId = 0;
        }
        // Music playing and user pressed Alarm Off the music should stop.
        else if (this.isRunning && startId == 0){
            Log.e("play", "case 2");
            mediaPlayer.stop();
            mediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }
        // No music playing and user pressed Alarm Off do nothing.
        else if (!this.isRunning && startId == 0) {
            Log.e("play", "case 3");
            this.startId = 0;
            this.isRunning = false;
        }
        // Music playing and user pressed Alarm On do nothing.
        else if (this.isRunning && startId == 1) {
            Log.e("play", "case 4");
            this.startId = 0;
            //this.isRunning = false;
        }
        // Any other stupid state.
        else {
            Log.e("play", "case 5");
            this.startId = 0;
            this.isRunning = false;
        }

        // If the service stopped it will not automatically restart.
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Ringtone service is stopped", Toast.LENGTH_SHORT).show();

        Log.e("on Destroy", "destroyed");
        super.onDestroy();

    }

    private int getRandomNumber() {
        int n =  (int)(Math.random() * 4) + 1;
        Log.e("in service " , String.valueOf(n));
        return n;
    }
}
