package com.example.jimmy.solitaire;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;

import java.util.AbstractCollection;
import java.util.ArrayList;

/**
 * Created by jay on 16/3/15.
 */
public class FoundationPile extends Pile{
    ArrayList<Card> foundationList = new ArrayList<Card>();
    Rect area;
    int width = 93;
    int height = 143;
    Card.Suit suit;

    public FoundationPile(Point point, Card.Suit suit){
        area = new Rect(point.x, point.y, point.x + width, point.y + height);
        this.suit = suit;
    }

    public void addCardToTemplist(ArrayList<Card> tempList){
        if(foundationList.size()!=0) {
            tempList.add(foundationList.get(foundationList.size() - 1));
            foundationList.remove(foundationList.get(foundationList.size() - 1));
        }
        else {
            Log.d("game:","empty foundationPile!");
        }
    }

    public void returnCards(ArrayList<Card> tempList){
        foundationList.add(tempList.get(0));
        foundationList.get(foundationList.size()-1).setLocation(area.left, area.top);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            foundationList.get(foundationList.size()-1).img.setElevation(foundationList.size());
        }
    }

    public boolean addCardFromTempList(ArrayList<Card> tempList){
        if(tempList.size()==1&&tempList.get(0).getSuit()==suit){
            if(foundationList.size()==0&&tempList.get(0).getRank()==1) {
                foundationList.add(tempList.get(0));
                foundationList.get(foundationList.size() - 1).setLocation(area.left, area.top);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    foundationList.get(foundationList.size() - 1).img.setElevation(foundationList.size());
                }
                return true;
            }
            else if(foundationList.size()!=0&&tempList.get(0).getRank()-1==foundationList.get(foundationList.size()-1).getRank()){
                foundationList.add(tempList.get(0));
                foundationList.get(foundationList.size() - 1).setLocation(area.left, area.top);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    foundationList.get(foundationList.size() - 1).img.setElevation(foundationList.size());
                }
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public void addCard(Card card){
        foundationList.add(card);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            foundationList.get(foundationList.size()-1).img.setElevation(foundationList.size());
        }
        card.openCard();
        card.setLocation(area.left, area.top);
    }

    public String getCardListInfo(){
        String info = "";

        if(foundationList.size()==0){
            info += "empty";
        }
        else {
            for (int i = 0; i < foundationList.size(); i++) {
                info += foundationList.get(i).getCardName() + "&";
            }
        }
        return info;
    }
}
