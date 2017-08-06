package com.testjob.dowloadrss.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.testjob.dowloadrss.utils.JobScheduler;


public class StartupIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        JobScheduler.setJobScheduler(context);
    }
}
