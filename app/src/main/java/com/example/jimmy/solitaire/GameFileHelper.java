package com.example.jimmy.solitaire;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by jay on 16/4/29.
 */

public class GameFileHelper {

    public static String[] getRanker(Context context, int index){
        return new String[]{RankLoader(context)[index].split("&")[0], MyTimer.IntegerToHMS(Integer.parseInt(RankLoader(context)[index].split("&")[1]))};
    }

    public static String[] RankLoader(Context context){

        if(!context.getFileStreamPath("RankPlayers.sav").exists()){
            try {
                FileOutputStream check = context.openFileOutput("RankPlayers.sav", Context.MODE_APPEND);
                check.write("--&0##--&0##--&0##--&0##--&0##--&0##--&0##--&0##--&0##--&0".getBytes());
                check.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        String[] Info = new String[13];

        try {
            FileInputStream inputStream = context.openFileInput("RankPlayers.sav");
            byte[] temp = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            while ((len = inputStream.read(temp))>0){
                sb.append(new String(temp, 0, len));
            }

            inputStream.close();

            Info = sb.toString().split("##");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Info;
    }

    private static String contentGenerater(int index, String[] rankers, String[] newRaner){



        for(int i=0;i<10;i++){
            if(rankers[i].split("&")[1].equals("0")&&index<=i){
                for(int j = index;j<i&&j<9;j++){
                    rankers[j+1] = rankers[j];
                }
                break;
            }
        }

        rankers[index] = newRaner[0]+"&"+newRaner[1];

        String info = "";

        for (int i=0;i<10;i++){
            info += rankers[i]+"##";
        }

        return info;
    }

    public static int RankSaver(Context context, String name, int time){

        int r = 0;

        if(context.getFileStreamPath("RankPlayers.sav").exists()){
            String info = "";

            for(int i =0;i<10;i++){
                if(Integer.parseInt(RankLoader(context)[i].split("&")[1])>time || RankLoader(context)[i].split("&")[1].equals("0")){
                    info = contentGenerater(i, RankLoader(context), new String[]{name, String.valueOf(time)});

                    r = i+1;

                    break;
                }
            }


            try{

                FileOutputStream outputStream = context.openFileOutput("RankPlayers.sav", Context.MODE_PRIVATE);
                outputStream.write(info.getBytes());
                outputStream.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else {
            try {
                FileOutputStream check = context.openFileOutput("RankPlayers.sav", Context.MODE_APPEND);
                check.write("--&0##--&0##--&0##--&0##--&0##--&0##--&0##--&0##--&0##--&0".getBytes());
                check.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return r;

    }
}
