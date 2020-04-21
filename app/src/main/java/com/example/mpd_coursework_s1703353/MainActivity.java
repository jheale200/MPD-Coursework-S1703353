package com.example.mpd_coursework_s1703353;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> TrafficLinks = new ArrayList<>();

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


        TrafficLinks.add("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
        TrafficLinks.add("https://trafficscotland.org/rss/feeds/roadworks.aspx");
        TrafficLinks.add("https://trafficscotland.org/rss/feeds/currentincidents.aspx");

    }

    @Override
    public void onClick(View view){
        Intent mpd = new Intent(getApplicationContext(), TrafficActivity.class);
        switch(view.getId()){
            case R.id.Button1:
                mpd.putExtra("rssLink", TrafficLinks.get(0));
                startActivity(mpd);
                break;
            case R.id.Button2:
                mpd.putExtra("rssLink", TrafficLinks.get(1));
                startActivity(mpd);
                break;
            case R.id.Button3:
                mpd.putExtra("rssLink", TrafficLinks.get(2));
                startActivity(mpd);
                break;
            case R.id.Button4:
                showDialog();
                break;
        }

    }

    private void showDialog(){
        AlertDialog.Builder dia = new AlertDialog.Builder(this);
        dia.setMessage("Do you want to exit?");
        dia.setCancelable(false);
        dia.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }
        });
        dia.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "That never happened", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        AlertDialog alert = dia.create();
        alert.show();
    }
}
