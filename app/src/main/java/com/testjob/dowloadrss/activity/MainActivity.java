package com.testjob.dowloadrss.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.testjob.dowloadrss.R;
import com.testjob.dowloadrss.dataBase.DB;
import com.testjob.dowloadrss.service.ServiceDownloader;
import com.testjob.dowloadrss.utils.JobScheduler;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private DB db;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list_view);

        db = new DB(this);
        db.open();


        setData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String link = cursor.getString(cursor.getColumnIndexOrThrow(DB.DBHelper.COLUMN_LINK));

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("link", link);

                startActivity(intent);

            }
        });


        JobScheduler.setJobScheduler(this);

    }

    private void setData() {
        Cursor cursor = db.getAllData();
        startManagingCursor(cursor);

        if (cursor.getCount() > 0) {

            if (broadcastReceiver != null){
                unregisterReceiver(broadcastReceiver);
                broadcastReceiver = null;
            }

            String[] from = new String[]{DB.DBHelper.COLUMN_TITLE, DB.DBHelper.COLUMN_PUBDATE, DB.DBHelper.COLUMN_DESCRIPTION, DB.DBHelper.COLUMN_LINK};
            int[] to = new int[]{R.id.title, R.id.pubDate, R.id.desc, R.id.link};

            SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.item_listview, cursor, from, to, 0);
            listView.setAdapter(scAdapter);
        }else {
            Intent intent = new Intent(this, ServiceDownloader.class);
            intent.putExtra("EXTRA", "start_with_activity");
            startService(intent);
            broadcastReceiver = new UpdateBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter(ServiceDownloader.ACTION_MYINTENTSERVICE);
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(broadcastReceiver, intentFilter);
            Toast.makeText(this, "Downloads RSS!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class UpdateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    }

}
