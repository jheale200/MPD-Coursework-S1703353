package com.example.mpd_coursework_s1703353;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> RSSTrafficLinks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Button1 = findViewById(R.id.Button1);
        Button1.setOnClickListener(this);

        Button Button2 = findViewById(R.id.Button2);
        Button2.setOnClickListener(this);

        Button Button3 = findViewById(R.id.Button3);
        Button3.setOnClickListener(this);

        Button Button4 = findViewById(R.id.Button4);
        Button4.setOnClickListener(this);


        RSSTrafficLinks.add("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
        RSSTrafficLinks.add("https://trafficscotland.org/rss/feeds/roadworks.aspx");
        RSSTrafficLinks.add("https://trafficscotland.org/rss/feeds/currentincidents.aspx");

    }

    @Override
    public void onClick(View view){
        Intent mpd = new Intent(getApplicationContext(), TrafficActivity.class);
        switch(view.getId()){
            case R.id.Button1:
                mpd.putExtra("rssLink", RSSTrafficLinks.get(0));
                startActivity(mpd);
                break;
            case R.id.Button2:
                mpd.putExtra("rssLink", RSSTrafficLinks.get(1));
                startActivity(mpd);
                break;
            case R.id.Button3:
                mpd.putExtra("rssLink", RSSTrafficLinks.get(2));
                startActivity(mpd);
                break;
            case R.id.Button4:
                showtbDialog();
                break;
        }

    }

    private void showtbDialog(){
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage("Do you want to exit the MPDTraffic App?");
        bld.setCancelable(false);
        bld.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "Thank you for using the MPDTraffic App", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }
        });
        bld.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "You Pressed No", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        AlertDialog alert = bld.create();
        alert.show();
    }
}
