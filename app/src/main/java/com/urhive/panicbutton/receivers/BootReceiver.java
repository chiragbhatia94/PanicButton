package com.urhive.panicbutton.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.urhive.panicbutton.helpers.UIHelper;

/**
 * Created by Chirag Bhatia on 27-04-2017.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UIHelper.startPanicButtonService(context);
    }
}
