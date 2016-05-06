package com.example.jimmy.solitaire;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;

/**
 * Created by jay on 16/3/15.
 */
public class StockAndWaste extends Pile{
    ArrayList<Card> stockList = new ArrayList<Card>();
    ArrayList<Card> wasteList = new ArrayList<Card>();

    int width = 93;
    int height = 143;

    Rect stockArea;
    Rect wasteArea;

    public StockAndWaste(Point stockPoint, Point wastePoint){
        this.stockArea = new Rect(stockPoint.x, stockPoint.y, stockPoint.x + width, stockPoint.y + height);
        this.wasteArea = new Rect(wastePoint.x, wastePoint.y, wastePoint.x + width, wastePoint.y + height);
    }

    public boolean isEmpty(){
        if(stockList.size()==0&&wasteList.size()==0){
            return true;
        }
        else
            return false;
    }

    public void addCard(Card card){
        stockList.add(card);

        card.setLocation(stockArea.left, stockArea.top);
    }

    public void loadCardToStock(Card card){
        stockList.add(card);

        card.setLocation(stockArea.left, stockArea.top);
    }

    public void loadCardToWaste(Card card){
        card.openCard();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            card.img.setElevation(wasteList.size()+1);
        }

        wasteList.add(card);
        card.setLocation(wasteArea.left, wasteArea.top);
    }

    public void addCardToTempList(ArrayList<Card> tempList){
        if(wasteList.size()!=0){
            tempList.add(wasteList.get(wasteList.size()-1));
            wasteList.remove(wasteList.get(wasteList.size() - 1));
        }
    }

    public void returnCards(ArrayList<Card> tempList){
        wasteList.add(tempList.get(0));
        tempList.get(0).setLocation(wasteArea.left, wasteArea.top);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tempList.get(0).img.setElevation(wasteList.size() + 1);
        }
    }

    public void reset(){

        if(wasteList.size()==0){
            Log.d("m:","empty wastelist");
        }

        if(stockList.size()==0) {
            for (int i = wasteList.size()-1; i >=0; i--) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    wasteList.get(i).img.setElevation(0);
                }
                stockList.add(wasteList.get(i));
                wasteList.get(i).setLocation(stockArea.left, stockArea.top);
                wasteList.get(i).closeCard();
            }
            wasteList.clear();
        }
    }

    public void flip(){
        if(stockList.size()!=0){
            wasteList.add(stockList.get(stockList.size() - 1));

            stockList.get(stockList.size()-1).setLocation(wasteArea.left, wasteArea.top);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                stockList.get(stockList.size()-1).img.setElevation(wasteList.size()+1);
            }
            stockList.get(stockList.size()-1).openCard();
            stockList.get(stockList.size()-1).img.startAnimation(getAlphaAnimation());
            stockList.remove(stockList.get(stockList.size()-1));
        }
        else {
            Log.d("m:", "empty stocklist");
        }
    }

    public AlphaAnimation getAlphaAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        return alphaAnimation;
    }

    public String getStockCardListInfo(){
        String info = "";

        if(stockList.size()==0){
            info += "empty";
        }
        else {
            for (int i = 0; i < stockList.size(); i++) {
                info += stockList.get(i).getCardName() + "&";
            }
        }

        return info;
    }

    public String getWasteCardListInfo(){
        String info = "";

        if(wasteList.size()==0){
            info += "empty";
        }
        else {
            for (int i = 0; i < wasteList.size(); i++) {
                info += wasteList.get(i).getCardName() + "&";
            }
        }

        return info;
    }

}
