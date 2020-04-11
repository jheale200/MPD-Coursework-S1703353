package com.example.mpd_coursework_s1703353;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TrafficActivity extends ListActivity {

    private ProgressBar pbarDialog;
    ArrayList<HashMap<String,String>> rssItemList = new ArrayList<>();
    Parser rssParser = new Parser();
    List<TrafficItems> rssitemList = new ArrayList<>();
    private ListAdapter lstadapter;
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_PUBDATE = "pubDate";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_GEORSS = "georss:point";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_feed);

        Button MainMenuBtn = findViewById(R.id.MainMenubtn);
        MainMenuBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent mmenu = new Intent(TrafficActivity.this, MainActivity.class);
                mmenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mmenu);
            }

        });

        String rss_link = getIntent().getStringExtra("rssLink");
        new LoadRSSFeedItems().execute(rss_link);
        ListView lstvw = getListView();
        EditText ItemSearch = (EditText)findViewById(R.id.ItemSearch);

        ItemSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((SimpleAdapter)TrafficActivity.this.lstadapter).getFilter().filter(s);
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
            pbarDialog = new ProgressBar(TrafficActivity.this, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            pbarDialog.setLayoutParams(layoutParams);
            pbarDialog.setVisibility(View.VISIBLE);
            relativeLayout.addView(pbarDialog);
        }

        @Override
        protected String doInBackground(String... args){
            String rss_url = args[0];
            rssitemList = rssParser.getRSSFeedItems(rss_url);
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
                map.put(TAG_TITLE, item.title);
                map.put(TAG_LINK, item.link);
                map.put(TAG_PUBDATE, item.pubDate);
                map.put(TAG_DESCRIPTION, item.description);
                map.put(TAG_GEORSS, item.georss);
                rssItemList.add(map);
            }
            runOnUiThread(new Runnable() {
                public void run(){
                    lstadapter = new SimpleAdapter(
                            TrafficActivity.this,
                            rssItemList, R.layout.item_list,
                            new String[]{TAG_LINK, TAG_TITLE, TAG_PUBDATE, TAG_DESCRIPTION, TAG_GEORSS},
                            new int[]{R.id.page_url, R.id.title, R.id.pubDate, R.id.description, R.id.georss}
                    );
                    setListAdapter(lstadapter);
                }
            });
            return null;
        }

        protected void onPostExecute(String args){
            pbarDialog.setVisibility(View.GONE);
        }

    }

}