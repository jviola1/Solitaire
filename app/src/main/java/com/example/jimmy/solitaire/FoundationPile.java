package com.example.jimmy.solitaire;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.AbstractCollection;
import java.util.ArrayList;


public class FoundationPile {
    ArrayList<Card> list = new ArrayList<Card>();
    Rect area;
    int width = 93;
    int height = 143;
    Card.Suit suit;

    public FoundationPile(Point point, Card.Suit suit){
        area = new Rect(point.x, point.y, point.x + width, point.y + height);
        this.suit = suit;
    }

    public void addCard(ArrayList<Card> tempList){
        if(tempList.size()==1) {
            Card card = tempList.get(0);
            if (card.getRank() == 1 && card.getSuit() == suit) {
                list.add(card);
            }
        }
    }
}