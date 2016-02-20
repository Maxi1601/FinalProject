package com.yifat.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PowerReceiver extends BroadcastReceiver{

    // Called when the BroadcastReceiver is receiving an Intent broadcast
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals(intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context, "Power Connected", Toast.LENGTH_LONG).show();
        }

        else if (action.equals(intent.ACTION_POWER_DISCONNECTED)){
            Toast.makeText(context, "Power Disconnected", Toast.LENGTH_LONG).show();
        }

        else if(action.equals(intent.ACTION_BOOT_COMPLETED)) {
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }

    }

}
