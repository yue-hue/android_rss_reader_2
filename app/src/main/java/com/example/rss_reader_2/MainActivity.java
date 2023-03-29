package com.example.rss_reader_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<News> newsList = new ArrayList<News>();
    RecyclerView recyclerView;
    RVAdapter adapter;
    RVAdapter.OnNewsClickListener newsClickListener;
    ArrayList<String> titles;
    ArrayList<String> links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean hasConnect = hasConnection(this);
        if (hasConnect) {
            titles = new ArrayList<String>();
            links = new ArrayList<String>();

            recyclerView = findViewById(R.id.list);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

            GetPage pageLoad = new GetPage();
            pageLoad.execute();

            RVAdapter.OnNewsClickListener newsClickListener = new RVAdapter.OnNewsClickListener() {
                @Override
                public void onNewsClick(News news, int position) {
                    String link = news.getNewsLink();
                    Intent intent = new Intent(MainActivity.this, OpenNews.class);
                    intent.putExtra("link", link);
                    startActivity(intent);
                }
            };

            adapter = new RVAdapter(MainActivity.this, newsList, newsClickListener);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("No internet connection");
            builder.setMessage("No internet connection.\nCheck your connection and try again later.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            }).show();

        }
    }

    public class GetPage extends AsyncTask<Void, Void, Boolean> {
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("https://feeds.bbci.co.uk/news/rss.xml");
                URLConnection urlConnection = url.openConnection();

                urlConnection.setDoInput(true);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(urlConnection.getInputStream(), "UTF_8");

                boolean insideItem = false;
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                titles.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                links.add(xpp.nextText());
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }
                    eventType = xpp.next();
                }
                return true;
            } catch (MalformedURLException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                exception = e;
            } catch (XmlPullParserException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                exception = e;
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                exception = e;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (isSuccess) {
                for (int i = 0; i < titles.size(); i++) {
                    newsList.add(new News(titles.get(i), links.get(i)));
                }

                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "RSS loading failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static boolean hasConnection(final Context context) {
        boolean hasConnect = false;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            hasConnect = true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            hasConnect = true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            hasConnect = true;
        } else {
            hasConnect = false;
        }
        return hasConnect;
    }
}