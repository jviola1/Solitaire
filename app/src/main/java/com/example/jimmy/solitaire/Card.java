package com.example.jimmy.solitaire;

import android.widget.ImageView;

public class Card {
    Pile pile;
    ImageView img;
    private int rank;  //Value between 1 and 13 where 1 is ace, 11 is Jack, 12 is Queen and 13 is King
    private int suit;  //0 = Spades, 1 = Clubs, 2 = Hearts, 3 = Diamonds
    private int cardColor; //0 = black, 1 = Red
    private boolean reserve; //True if the card is face-down

    //For arbitrary card object
    public Card(Pile pile, ImageView img){
        this.img = img;
        pile.addCard(this);
    }

    //For full-fledged card object
    public Card(Pile pile, ImageView img, int tempRank, int tempSuit, boolean tempReserve){
        this.img = img;
        pile.addCard(this);
        rank = tempRank;
        suit = tempSuit;
        reserve = tempReserve;
        if(suit <= 1)
            cardColor = 0;
        else
            cardColor = 1;
    }

    //For cloning another card object
    public Card(Card tempCard){
        this.img = tempCard.getImg();
        pile.addCard(this);
        rank = tempCard.getRank();
        suit = tempCard.getSuit();
        cardColor = tempCard.getColor();
        reserve = tempCard.getReserve();
    }

    //Returns true if the card passed in is of the opposite color
    public boolean isAlternating(Card compareCard){
        return compareCard.cardColor != cardColor;
    }

    //Returns true if the card passed in is of the same suit
    public boolean isSameSuit(Card compareCard){
        return compareCard.getSuit() == suit;
    }

    //Returns true if this card is one rank lower than the card passed in
    public boolean isOneLower(Card compareCard){
        return rank == (compareCard.getRank() - 1);
    }

    //Returns true if this card is one rank higher than the card passed in
    public boolean isOneHigher(Card compareCard){
        return rank == (compareCard.getRank() - 1);
    }

    //Mutators
    public void setLocation(float x, float y){
        this.img.setX(x);
        this.img.setY(y);
    }

    public void setRank(int tempRank){
        rank = tempRank;
    }

    public void setSuit(int tempSuit){
        suit = tempSuit;
    }

    public void setReserve(boolean tempReserve)
    {
        reserve = tempReserve;
    }

    //Accessors
    public int getRank(){
        return rank;
    }

    public int getSuit(){
        return suit;
    }

    public ImageView getImg(){
        return img;
    }

    public int getColor(){
        return cardColor;
    }

    public boolean getReserve(){
        return reserve;
    }
}

