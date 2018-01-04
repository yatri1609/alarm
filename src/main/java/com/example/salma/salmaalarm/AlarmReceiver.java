package com.example.salma.salmaalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("in the receiver", "yayaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");


        // Fetch extra from the Intent.
        // To get the chosen ringtone from the spinner.
        int chosenRingtone = intent.getExtras().getInt("ringtone");
        boolean enableVibration = intent.getExtras().getBoolean("enableVibration");
        int volume = intent.getExtras().getInt("volume");

        Log.e("ringtone", String.valueOf(chosenRingtone));
        Log.e("volume in receiver", String.valueOf(volume));

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("ringtone", String.valueOf(chosenRingtone));
        serviceIntent.putExtra("enableVibration", enableVibration);
        serviceIntent.putExtra("volume", volume);


        serviceIntent.setClassName("com.example.salma.salmaalarm", "com.example.salma.salmaalarm.PopActivity");
        serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(serviceIntent);

    }
}
