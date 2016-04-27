package com.example.jimmy.solitaire;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Handler;
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
import android.content.DialogInterface;


public class MainActivity extends Activity implements View.OnClickListener {

    Button play, manual, score, exit, watch;
    public static final int PLAY_REQUEST_CODE = 0;
    public static final int MANUAL_REQUEST_CODE = 1;
    public static final int SCORE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.button);
        play.setOnClickListener(this);

        manual = (Button) findViewById(R.id.button1);
        manual.setOnClickListener(this);

        score = (Button) findViewById(R.id.button3);
        score.setOnClickListener(this);

        exit = (Button) findViewById(R.id.button4);
        exit.setOnClickListener(this);

        watch = (Button) findViewById(R.id.button5);
        watch.setOnClickListener(this);

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
                onBackPressed();
                break;

            case R.id.button5:
                Intent w = new Intent(this, TimerActivity.class);
                startActivity(w);
                break;

            default:
                break;


            //    };


        }
    }

    //    Pressing phone back arrow Twice will exit the game
//    boolean backToExit = false;
    @Override
    public void onBackPressed() {
        /*  *****
            Better Way to Exit the app with DialogBox below
            *****
         */
        onBackPressedHandler();
        return;

        /*  *****
            Simple Way to Exit the app below
            *****
        */
//        if (backToExit) {
//            super.onBackPressed();
//            return;
//        }
//        this.backToExit  = true;
//        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                backToExit =false;
//            }
//        }, 2000);

    }
    //Handles the event when exit in the app and back on the phone is pressed
    public void onBackPressedHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
//        Dialog Title
        alertDialog.setTitle("Exit Solitaire");
//        Dialog Message
        alertDialog.setMessage("Are you sure you want to exit Solitaire?");
//        "Yes" Button
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Toast.makeText(getApplicationContext(), "BYE!", Toast.LENGTH_SHORT).show();
                    }
                });
//        "No" Button
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();


    }
}
