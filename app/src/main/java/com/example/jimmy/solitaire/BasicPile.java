package com.example.jimmy.solitaire;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by jay on 16/3/8.
 */
public class BasicPile extends Pile{
    ArrayList<Card> cardList = new ArrayList<Card>();
    Point location;
    Rect area;

    int distanceBetweenOpenCards = 30;
    int distanceBetweenCloseCards = 10;
    int width = 93;
    int height = 143;


    public BasicPile(Point location){
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
                card.setLocation(location.x, location.y + getNumberOfCloseCard()*distanceBetweenCloseCards+(cardList.size()-getNumberOfCloseCard())*distanceBetweenOpenCards);
            } else {
                card.setLocation(location.x, location.y + getNumberOfCloseCard()*distanceBetweenCloseCards+(cardList.size()-getNumberOfCloseCard())*distanceBetweenOpenCards);
            }
        }
        else {
            card.setLocation(location.x, location.y);
        }
        cardList.add(card);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            card.img.setElevation(cardList.size());
        }
        updateArea();

    }

    public void setCardsToCardList(Card[] cards){
        for(int i = 0; i<cards.length; i++) {
            addCard(cards[i]);

        }

        if(!cardList.get(cardList.size()-1).isOpen()){
            cardList.get(cardList.size()-1).openCard();
        }
    }


    // rule check method
    private boolean isCardsCanBeAdded(ArrayList<Card> tempList){
        Card firstCardInTemp = tempList.get(0);

        if(cardList.size()!=0) {
            Card lastCardInList = cardList.get(cardList.size() - 1);

            return firstCardInTemp.isAlternatingCard(lastCardInList) && firstCardInTemp.isOneLower(lastCardInList);
        }
        else if(tempList.get(0).getRank()!=13){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addCardFromList(ArrayList<Card> tempList){

        if(isCardsCanBeAdded(tempList)) {
            for (int i = 0; i < tempList.size(); i++) {
                tempList.get(i).pile = this;

                tempList.get(i).setLocation(location.x,
                        location.y + getNumberOfCloseCard()*distanceBetweenCloseCards+(cardList.size()-getNumberOfCloseCard())*distanceBetweenOpenCards);
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

    public void returnCards(ArrayList<Card> tempList){

        for (int i = 0; i < tempList.size(); i++) {
            tempList.get(i).pile = this;

            tempList.get(i).setLocation(location.x, location.y + getNumberOfCloseCard()*distanceBetweenCloseCards+(cardList.size()-getNumberOfCloseCard())*distanceBetweenOpenCards);
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

    public void addCardsSelectedToList(final ArrayList<Card> tempList,int x,int y){
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

