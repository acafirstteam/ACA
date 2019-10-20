package am.newway.aca.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import am.newway.aca.util.Util;

public
class MyStartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive( Context context, Intent intent) {
        Util.scheduleJob(context);
    }
}
