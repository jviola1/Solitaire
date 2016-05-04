package com.example.jimmy.solitaire;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by jay on 16/4/29.
 */
public class GameFileHelper {

    public static String[] RankLoader(Context context){

        String[] PileInfo = new String[13];

        try {
            FileInputStream inputStream = context.openFileInput("rank.sav");
            byte[] temp = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            while ((len = inputStream.read(temp))>0){
                sb.append(new String(temp, 0, len));
            }

            inputStream.close();

            PileInfo = sb.toString().split("##");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return PileInfo;
    }

    public static void RankSaver(Context context, String name, int score){

        String info = "";

        String[] player1 = RankLoader(context)[0].split("&");
        String[] player2 = RankLoader(context)[1].split("&");
        String[] player3 = RankLoader(context)[2].split("&");


        if(Integer.parseInt(player1[1])<score){
            info += name+"&"+String.valueOf(score)+"##"+RankLoader(context)[1]+"##"+RankLoader(context)[2];
        }
        else if(Integer.parseInt(player2[1])<score){
            info += RankLoader(context)[0]+"##"+name+"&"+String.valueOf(score)+"##"+RankLoader(context)[2];
        }
        else if(Integer.parseInt(player3[1])<score){
            info += RankLoader(context)[0]+"##"+RankLoader(context)[1]+"##"+name+"&"+String.valueOf(score);
        }
        else {
            info += RankLoader(context)[0]+"##"+RankLoader(context)[1]+"##"+RankLoader(context)[2];
        }

        try{
            FileOutputStream check = context.openFileOutput("rank.sav", Context.MODE_APPEND);
            check.close();
            FileOutputStream outputStream = context.openFileOutput("rank.sav", Context.MODE_PRIVATE);
            outputStream.write(info.getBytes());
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
