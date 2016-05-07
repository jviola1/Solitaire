package com.example.jimmy.solitaire;

/**
 * Created by Amritpal on 4/11/2016.
 */
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.jimmy.solitaire.MainActivity;import com.example.jimmy.solitaire.R;

import org.w3c.dom.Text;

/**
 * Created by Amritpal on 4/11/2016.
 */
public class ScoreActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        TextView name1 = (TextView)findViewById(R.id.name1);
        TextView time1 = (TextView)findViewById(R.id.time1);
        TextView name2 = (TextView)findViewById(R.id.name2);
        TextView time2 = (TextView)findViewById(R.id.time2);
        TextView name3 = (TextView)findViewById(R.id.name3);
        TextView time3 = (TextView)findViewById(R.id.time3);
        TextView name4 = (TextView)findViewById(R.id.name4);
        TextView time4 = (TextView)findViewById(R.id.time4);
        TextView name5 = (TextView)findViewById(R.id.name5);
        TextView time5 = (TextView)findViewById(R.id.time5);
        TextView name6 = (TextView)findViewById(R.id.name6);
        TextView time6 = (TextView)findViewById(R.id.time6);


        name1.setText(GameFileHelper.getRanker(this, 0)[0]);
        time1.setText(GameFileHelper.getRanker(this, 0)[1]);
        name2.setText(GameFileHelper.getRanker(this, 1)[0]);
        time2.setText(GameFileHelper.getRanker(this, 1)[1]);
        name3.setText(GameFileHelper.getRanker(this, 2)[0]);
        time3.setText(GameFileHelper.getRanker(this, 2)[1]);
        name4.setText(GameFileHelper.getRanker(this, 3)[0]);
        time4.setText(GameFileHelper.getRanker(this, 3)[1]);
        name5.setText(GameFileHelper.getRanker(this, 4)[0]);
        time5.setText(GameFileHelper.getRanker(this, 4)[1]);
        name6.setText(GameFileHelper.getRanker(this, 5)[0]);
        time6.setText(GameFileHelper.getRanker(this, 5)[1]);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Check the scores amongst users", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    public void onHomeClick(View v) {
//        Intent myintent = new Intent(this, MainActivity.class);
//        startActivity(myintent);
//    }
}
