package com.marco.service2;

/**
 * Created by pippo on 08/08/15.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MyService.class);
        context.startService(serviceIntent);
    }
}