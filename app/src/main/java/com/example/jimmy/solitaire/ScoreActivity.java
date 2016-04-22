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
import android.widget.Button;import com.example.jimmy.solitaire.MainActivity;import com.example.jimmy.solitaire.R;

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Check the scores amongst users", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onHomeClick(View v) {
        Intent myintent = new Intent(this, MainActivity.class);
        startActivity(myintent);
    }
}
