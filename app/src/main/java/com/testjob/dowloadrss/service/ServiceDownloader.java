package com.testjob.dowloadrss.service;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;

import com.testjob.dowloadrss.dataBase.DB;
import com.testjob.dowloadrss.dataBase.model.RssItem;
import com.testjob.dowloadrss.utils.Network;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class ServiceDownloader extends IntentService {

    private static String URL_BASE = "http://feeds.abcnews.com/abcnews/topstories";

    private static final String PUB_DATE = "pubDate";
    private static final String DESCRIPTION = "description";
    private static final String CHANNEL = "channel";
    private static final String LINK = "link";
    private static final String TITLE = "title";
    private static final String ITEM = "item";

    public static final String ACTION_MYINTENTSERVICE = "com.testjob.intentservice.RESPONSE";
    public static final String EXTRA_KEY_OUT = "EXTRA_OUT";

    private final String LOG_TAG = "serviceDownloader";

    public ServiceDownloader(String name) {
        super("ServiceDownloader");
    }

    public ServiceDownloader(){
        super("ServiceDownloader");
    }

    public void parse() {
        Log.d(LOG_TAG, "start downloader service: parse");
        Context ctx = getApplicationContext();
        if (!Network.isNetworkAvailable(ctx)) {
            return;
        }

        DB db = new DB(ctx);
        db.open();
        db.delAllRec();

        XmlPullParser parser = Xml.newPullParser();
        InputStream stream = null;
        try {
            // auto-detect the encoding from the stream
            stream = new URL(URL_BASE).openConnection().getInputStream();
            parser.setInput(stream, null);
            int eventType = parser.getEventType();
            boolean done = false;
            RssItem item = null;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)) {
                            Log.i("new item", "Create new item");
                            item = new RssItem();
                        } else if (item != null) {
                            if (name.equalsIgnoreCase(LINK)) {
                                Log.i("Attribute", "setLink");
                                item.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                Log.i("Attribute", "description");
                                item.setDescription(parser.nextText().trim());
                            } else if (name.equalsIgnoreCase(PUB_DATE)) {
                                Log.i("Attribute", "date");
                                item.setPubDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)) {
                                Log.i("Attribute", "title");
                                item.setTitle(parser.nextText().trim());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        Log.i("End tag", name);
                        if (name.equalsIgnoreCase(ITEM) && item != null) {
                            Log.i("Added", item.toString());
                            db.addRec(item);
                        } else if (name.equalsIgnoreCase(CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        db.close();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "start downloader service: onHandleIntent");
        parse();
        if (intent != null) {
            String extra = intent.getStringExtra("EXTRA");
            if (extra.equals("start_with_activity")){
                Intent responseIntent = new Intent();
                responseIntent.setAction(ACTION_MYINTENTSERVICE);
                responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                sendBroadcast(responseIntent);
            }
        }
    }
}
