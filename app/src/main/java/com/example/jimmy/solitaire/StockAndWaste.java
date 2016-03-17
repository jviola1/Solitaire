package com.example.jimmy.solitaire;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by jay on 16/3/15.
 */
public class StockAndWaste {
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

    public void addCardToStock(Card card){
        stockList.add(card);
    }

    public void reset(){
        if(stockList.size()==0) {
            for (int i = 0; i < wasteList.size(); i++) {
                stockList.add(wasteList.get(i));
                wasteList.get(i).setLocation(stockArea.left, stockArea.top);
                wasteList.get(i).closeCard();
            }
            wasteList.clear();
        }
    }

    public void flip(){
        if(stockList.size()!=0){
            wasteList.add(stockList.get(stockList.size()-1));
            stockList.get(stockList.size()-1).setLocation(wasteArea.left, wasteArea.top);
            stockList.get(stockList.size()-1).openCard();
            stockList.remove(stockList.get(stockList.size()-1));
        }
    }
}
