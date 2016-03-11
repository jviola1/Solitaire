package com.example.jimmy.solitaire;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.widget.ImageView;

import java.util.ArrayList;


public class Pile {
    ArrayList<Card> cardList = new ArrayList<Card>();
    Point location;
    Rect area;
    int distanceBetweenCards = 40;


    public Pile(Point location){
        this.location = location;
        area = new Rect(location.x,location.y,location.x+100,location.y+100);
    }

    private void updateArea(){
        if(cardList.size()!=0) {
            area.bottom = location.y + 100 + (cardList.size() - 1) * distanceBetweenCards;
        }
        else {
            area.bottom = location.y+100;
        }
    }

    public void addCard(Card card){
        card.pile = this;
        card.img.setX(this.area.left);
        card.img.setY(this.area.top + (this.cardList.size()) * distanceBetweenCards);
        cardList.add(card);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            card.img.setElevation(cardList.size());
        }
        updateArea();

    }


    // rule check method
    private boolean isCardsCanBeAdded(ArrayList<Card> tempList){
        if(cardList.size()!=0) {
            Card lastCardInList = cardList.get(cardList.size() - 1);
            Card firstCardInTemp = tempList.get(0);

            return true;
        }
        else {
            return true;
        }
    }

    public boolean addCardFromList(ArrayList<Card> tempList){

        if(isCardsCanBeAdded(tempList)) {
            for (int i = 0; i < tempList.size(); i++) {
                tempList.get(i).pile = this;
                tempList.get(i).img.setX(area.left);
                tempList.get(i).img.setY(area.top + cardList.size() * distanceBetweenCards);
                cardList.add(tempList.get(i));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cardList.get(cardList.size() - 1).img.setElevation(cardList.size());
                }
            }
            updateArea();
            return true;
        }
        else {
            return false;
        }


    }

    public void removeCard(Card card){
        cardList.remove(card);
        updateArea();
    }

    public void removeCardFromCardToLast(Card card){
        int index=cardList.indexOf(card);
        Card[] tempList = new Card[index+1];
        for(int i = 0;i<index;i++){
            tempList[i]=cardList.get(i);
        }
        cardList.clear();
        for(int i = 0;i<index;i++){
            cardList.add(tempList[i]);
        }

        updateArea();
    }

    public void addCardsSelectedToList(ArrayList<Card> tempList,int x,int y){
        for(int i = 0;i<cardList.size();i++){
            if(i!=cardList.size()-1){
                Card tempCard = cardList.get(i);
                ImageView tempImg = tempCard.img;
                Rect tempArea = new Rect((int)tempImg.getX(),(int)tempImg.getY(),(int)tempImg.getX()+100,(int)tempImg.getY()+distanceBetweenCards);
                if(tempArea.contains(x, y)){
                    getCardsFromIndexToLast(tempList,i);
                    break;
                }
            }
            else {
                getCardsFromIndexToLast(tempList,i);
                break;
            }
        }
    }

    public void getCardsFromIndexToLast(ArrayList<Card> tempList, int index){
        for(int i = index;i<cardList.size();i++){
            tempList.add(cardList.get(i));
        }
    }

}
