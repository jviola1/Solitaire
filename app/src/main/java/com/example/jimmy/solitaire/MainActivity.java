package com.example.jimmy.solitaire;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

        Button play, manual, score, exit;
        public static final int PLAY_REQUEST_CODE = 0;
        public static final int MANUAL_REQUEST_CODE = 1;
        public static final int SCORE_REQUEST_CODE = 2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            play = (Button)findViewById(R.id.button);
            play.setOnClickListener(this);

            manual = (Button)findViewById(R.id.button1);
            manual.setOnClickListener(this);

            score = (Button)findViewById(R.id.button3);
            score.setOnClickListener(this);

            exit = (Button)findViewById(R.id.button4);
            exit.setOnClickListener(this);

        }
    //    public OnClickListener listen = new OnClickListener() {
            public void onClick(View v) {

                /* FIRST WAY
                    Intent i = new Intent(MainActivity.this, PlayActivity.class);
                        startActivity(i);

                        Toast.makeText(getApplicationContext(), "Let The Games Begin!", Toast.LENGTH_SHORT).show();
                }

            public void onClickManual(View manual){

                    Intent m = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(m);

                        Toast.makeText(getApplicationContext(), "Study The Rules", Toast.LENGTH_SHORT).show();
                }
            public void onClickScore(View score){

                    Intent myintent = new Intent(MainActivity.this, ScoreActivity.class);
                        startActivity(myintent);

                        Toast.makeText(getApplicationContext(), "What's Your Score", Toast.LENGTH_SHORT).show();
                }
                */

                /*SECOND WAY */
                switch (v.getId()) {
                    case R.id.button:
                        Intent i = new Intent(this, Main2Activity.class);
                        startActivity(i);

                        Toast.makeText(getApplicationContext(), "Let The Games Begin!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.button1:
                        Intent m = new Intent(this, SecondActivity.class);
                        startActivityForResult(m, MANUAL_REQUEST_CODE);

                        Toast.makeText(getApplicationContext(), "Study The Rules", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.button3:
                        Intent myintent = new Intent(MainActivity.this, ScoreActivity.class);
                        startActivityForResult(myintent, SCORE_REQUEST_CODE);

                        Toast.makeText(getApplicationContext(), "What's Your Score", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.button4:
                        Toast.makeText(getApplicationContext(), "Nothing Yet!", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;


    //    };


                }}


    }
