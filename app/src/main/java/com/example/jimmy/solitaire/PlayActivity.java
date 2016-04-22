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
import com.example.jimmy.solitaire.R;import java.lang.Override;

/**
 * Created by Amritpal on 4/11/2016.
 */
//  This Activity does nothing.
//  This is a just in case activity
public class PlayActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You will be able to play Solitaire here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onHomeClick(View v) {
        Intent myintent = new Intent(this, Main2Activity.class);
        startActivity(myintent);
    }
}
