package com.example.mpd_coursework_s1703353;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TrafficActivity extends ListActivity {

    ArrayList<HashMap<String,String>> rssItemList = new ArrayList<>();
    Parser rssParser = new Parser();
    List<TrafficItems> rssitemList = new ArrayList<>();

    private ProgressBar proBarDialog;
    private ListAdapter listAdapter;

    private static String TITLE = "title";
    private static String LINK = "link";
    private static String PUBDATE = "pubDate";
    private static String DESCRIPTION = "description";
    private static String GEORSS = "georss:point";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_feed);


        Button MenuButton = findViewById(R.id.MenuButton);
        MenuButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent menu = new Intent(TrafficActivity.this, MainActivity.class);
                menu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(menu);
            }

        });

        String rss_link = getIntent().getStringExtra("rssLink");
        new LoadRSSFeedItems().execute(rss_link);

        EditText ItemSearch = (EditText)findViewById(R.id.ItemSearch);

        ItemSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((SimpleAdapter)TrafficActivity.this.listAdapter).getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public class LoadRSSFeedItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            proBarDialog = new ProgressBar(TrafficActivity.this, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            proBarDialog.setLayoutParams(layoutParams);
            proBarDialog.setVisibility(View.VISIBLE);
            relativeLayout.addView(proBarDialog);
        }

        @Override
        protected String doInBackground(String... args){
            String rss_url = args[0];

            rssitemList = rssParser.getFeedItems(rss_url);

            for (final TrafficItems item : rssitemList ) {
                if (item.link.toString().equals(""))
                    break;
                HashMap<String, String> map = new HashMap<String, String>();

                String givenDate = item.pubDate.trim();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

                try{
                    Date mDate = sdf.parse(givenDate);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy - hh:mm a", Locale.UK);
                    item.pubDate = sdf2.format(mDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                map.put(TITLE, item.title);
                map.put(LINK, item.link);
                map.put(PUBDATE, item.pubDate);
                map.put(DESCRIPTION, item.description);
                map.put(GEORSS, item.georss);
                rssItemList.add(map);
            }

            runOnUiThread(new Runnable() {
                public void run(){
                    listAdapter = new SimpleAdapter(
                            TrafficActivity.this,
                            rssItemList, R.layout.item_list,
                            new String[]{LINK, TITLE, PUBDATE, DESCRIPTION, GEORSS},
                            new int[]{R.id.page_url, R.id.title, R.id.pubDate, R.id.description, R.id.georss}
                    );
                    setListAdapter(listAdapter);
                }
            });
            return null;

        }

        protected void onPostExecute(String args){
            proBarDialog.setVisibility(View.GONE);
        }

    }





}