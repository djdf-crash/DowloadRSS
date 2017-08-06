package com.testjob.dowloadrss.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;


public class JobSchedulerService extends JobService {
    private static final String LOG_TAG = "JobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Intent serviceIntent = new Intent(getApplicationContext(), ServiceDownloader.class);
        serviceIntent.putExtra("EXTRA", "start_with_JobSchedulerService");
        getApplicationContext().startService(serviceIntent);
        Log.d(LOG_TAG, "start downloader service");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
