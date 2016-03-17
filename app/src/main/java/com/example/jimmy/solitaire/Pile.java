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

    int distanceBetweenOpenCards = 40;
    int distanceBetweenCloseCards = 20;
    int width = 93;
    int height = 143;


    public Pile(Point location){
        this.location = location;
        area = new Rect(location.x,location.y,location.x+width,location.y+height);
    }

    private void updateArea(){
        if(cardList.size()!=0) {
            area.bottom = location.y + height + (cardList.size() - 1) * distanceBetweenOpenCards;
        }
        else {
            area.bottom = location.y+height;
        }
    }

    public void addCard(Card card){
        card.pile = this;

        if(cardList.size()!=0) {

            if (cardList.get(cardList.size() - 1).isOpen()) {
                card.setLocation(this.area.left, area.top + getNumberOfCloseCard()*distanceBetweenCloseCards+(cardList.size()-getNumberOfCloseCard())*distanceBetweenOpenCards);
            } else {
                card.setLocation(this.area.left, area.top + getNumberOfCloseCard()*distanceBetweenCloseCards+(cardList.size()-getNumberOfCloseCard())*distanceBetweenOpenCards);
            }
        }
        else {
            card.setLocation(this.area.left, this.area.top);
        }
        cardList.add(card);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            card.img.setElevation(cardList.size());
        }
        updateArea();

    }


    // rule check method
    private boolean isCardsCanBeAdded(ArrayList<Card> tempList){
        Card firstCardInTemp = tempList.get(0);

        if(cardList.size()!=0) {
            Card lastCardInList = cardList.get(cardList.size() - 1);

            return firstCardInTemp.isAlternatingCard(lastCardInList) && firstCardInTemp.isOneLower(lastCardInList);
        }
        else {
            return true;
        }
    }

    public boolean addCardFromList(ArrayList<Card> tempList){

        if(isCardsCanBeAdded(tempList)) {
            for (int i = 0; i < tempList.size(); i++) {
                tempList.get(i).pile = this;

                tempList.get(i).setLocation(area.left,
                                            area.top + getNumberOfCloseCard()*distanceBetweenCloseCards+(cardList.size()-getNumberOfCloseCard())*distanceBetweenOpenCards);
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

    public void addOriginalCardFromList(ArrayList<Card> tempList){

        for (int i = 0; i < tempList.size(); i++) {
            tempList.get(i).pile = this;

            tempList.get(i).setLocation(area.left, area.top + getNumberOfCloseCard()*distanceBetweenCloseCards+(cardList.size()-getNumberOfCloseCard())*distanceBetweenOpenCards);
            cardList.add(tempList.get(i));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardList.get(cardList.size() - 1).img.setElevation(cardList.size());
            }
        }
        updateArea();

    }

    public void openLastCard(){

        if(cardList.size()!=0) {
            Card lastCard = cardList.get(cardList.size() - 1);
            if (!lastCard.isOpen()) {
                lastCard.openCard();
            }
        }
    }

    public void removeCard(Card card){
        cardList.remove(card);
        updateArea();
    }

    public void removeCardsFromCardToLast(Card card){
        int index=cardList.indexOf(card);
        Card[] tempList = new Card[index+1];
        for(int i = 0;i<index;i++){
            tempList[i]=cardList.get(i);
        }
        cardList.clear();
        for(int i = 0;i<index;i++){
            cardList.add(tempList[i]);
        }
    }

    public void addCardsSelectedToList(ArrayList<Card> tempList,int x,int y){
        for(int i = 0;i<cardList.size();i++){
            if(i!=cardList.size()-1){
                Card tempCard = cardList.get(i);
                ImageView tempImg = tempCard.img;

                Rect tempArea = new Rect((int)tempImg.getX(),(int)tempImg.getY(),(int)tempImg.getX()+width,(int)tempImg.getY()+getDistance(tempCard));
                if(tempArea.contains(x, y)){
                    if(tempCard.isOpen()) {
                        getCardsFromIndexToLast(tempList, i);
                        break;
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                getCardsFromIndexToLast(tempList,i);
                break;
            }
        }
    }

    private int getNumberOfCloseCard(){
        int c = 0;
        for(int i = 0;i<cardList.size();i++){
            if(!cardList.get(i).isOpen()){
                c+=1;
            }
        }

        return c;
    }

    private int getDistance(Card card){
        if(card.isOpen()){
            return distanceBetweenOpenCards;
        }
        else {
            return distanceBetweenCloseCards;
        }
    }

    private void getCardsFromIndexToLast(ArrayList<Card> tempList, int index){
        for(int i = index;i<cardList.size();i++){
            tempList.add(cardList.get(i));
        }
    }

}
