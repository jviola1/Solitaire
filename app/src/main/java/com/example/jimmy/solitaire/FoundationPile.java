package com.example.jimmy.solitaire;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.AbstractCollection;
import java.util.ArrayList;


public class FoundationPile extends Pile{
    Card card;
    Rect area;
    int width = 93;
    int height = 143;
    Card.Suit suit;

    public FoundationPile(Point point, Card.Suit suit){
        area = new Rect(point.x, point.y, point.x + width, point.y + height);
        this.suit = suit;
    }

    public void addCardToTemplist(ArrayList<Card> tempList){
        tempList.add(card);
        this.card = null;
    }

    public void returnCards(ArrayList<Card> tempList){
        this.card = tempList.get(0);
        this.card.setLocation(area.left, area.top);
    }

    public boolean addCardFromTempList(ArrayList<Card> tempList){
        if(tempList.size()==1&&tempList.get(0).getRank()==1&&tempList.get(0).getSuit()==suit){
            this.card = tempList.get(0);
            this.card.setLocation(area.left, area.top);
            return true;
        }
        else {
            return false;
        }
    }
}
