package com.testjob.dowloadrss.utils;

import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.testjob.dowloadrss.service.JobSchedulerService;


public class JobScheduler {

    private static final String LOG_TAG = "JobSchedulerService";


    public static void setJobScheduler(Context context) {

        ComponentName serviceName = new ComponentName(context, JobSchedulerService.class.getName());

        android.app.job.JobScheduler mJobScheduler = (android.app.job.JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (mJobScheduler.getAllPendingJobs().size() > 0){
            for (JobInfo jobInfo : mJobScheduler.getAllPendingJobs()){
                if (jobInfo.getService().getPackageName().equals(serviceName.getPackageName())){
                    return;
                }
            }
        }

        JobInfo jobInfo = new JobInfo.Builder( 1, serviceName)
                .setPeriodic(86400000) //86400000 - day;
                .setPersisted(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        int result = mJobScheduler.schedule(jobInfo);
        if (result == android.app.job.JobScheduler.RESULT_SUCCESS) Log.d(LOG_TAG, "Job scheduled successfully!");
    }
}
