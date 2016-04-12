package com.example.jimmy.solitaire;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.io.File;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

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

        

        relativeLayout.addView(gameScene);

        setContentView(relativeLayout);


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

}