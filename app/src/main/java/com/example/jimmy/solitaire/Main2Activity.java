package com.example.jimmy.solitaire;

/**
 * Created by Amritpal on 4/13/2016.
 */

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.pm.ActivityInfo;
        import android.graphics.Rect;
        import android.media.Image;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.FrameLayout;
        import android.widget.RelativeLayout;
        import android.widget.ScrollView;



public class Main2Activity extends AppCompatActivity {

    GameScene gameScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if(height>width) {
            gameScene = new GameScene(this, relativeLayout, -162, width/8, height/8);
        }
        else {
            gameScene = new GameScene(this, relativeLayout, -162, height/8, width/8);
        }

        MyTimer myTimer = new MyTimer(gameScene.timeView);

        gameScene.myTimer = myTimer;

        relativeLayout.addView(gameScene);

        setContentView(relativeLayout);

        gameScene.randomGame();


//        Log.d("ranker1:", GameFileHelper.getRanker(this, 0)[0] + "  ||  " + GameFileHelper.getRanker(this, 0)[1]);
//        Log.d("ranker2:", GameFileHelper.getRanker(this,1)[0]+"  ||  "+GameFileHelper.getRanker(this, 1)[1]);
//        Log.d("ranker3:", GameFileHelper.getRanker(this,2)[0]+"  ||  "+GameFileHelper.getRanker(this, 2)[1]);
    }

    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);

        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int contentTop = v.getTop();

        Log.d("touchCorrection:",String.valueOf(-(contentTop+statusBarHeight)));

        gameScene.touchCorrection = -(contentTop+statusBarHeight);
    }

    /*  4/27/2016
           Added dialog box in the play activity to exit or not.
           *****
           ADDED TO MAIN FILE, github
           4/29/2016
           *****
        */
    public void onBackPressed() {
        onBackPressedHandler();
    }

    //Handles the event when exit in the app and back on the phone is pressed
    public void onBackPressedHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main2Activity.this);
//        Dialog Title
        alertDialog.setTitle("Exit Game");
//        Dialog Message
        alertDialog.setMessage("Are you sure you want to exit the game?");
//        "Yes" Button
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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