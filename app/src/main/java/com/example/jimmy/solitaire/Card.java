package com.example.jimmy.solitaire;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import java.util.Collection;

public class Card {

    public enum Suit{Spade, Club, Heart, Diamond}

    Pile pile;
    ImageView img;
    private int rank;
    private Suit suit;
    private int color;
    private boolean isOpen = false;
    @DrawableRes int imgId;
    @DrawableRes int backImgId = R.drawable.back;

    public Card(Pile pile, ImageView img, int rank, Suit suit, @DrawableRes int imgId){
        this.img = img;
        pile.addCard(this);
        this.rank = rank;
        this.suit = suit;
        this.imgId = imgId;

        switch (suit){
            case Spade:
            case Club:
                color = 0;
                break;
            case Heart:
            case Diamond:
                color = 1;
                break;

        }

        if(isOpen) {
            img.setImageResource(imgId);
        }
        else {
            img.setImageResource(backImgId);
        }

    }

    public int getRank(){
        return rank;
    }

    public Suit getSuit(){
        return suit;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public void openCard(){
        isOpen = true;
        img.setImageResource(imgId);
    }

    public void closeCard(){
        isOpen = false;
        img.setImageResource(backImgId);
    }

    public void setLocation(float x, float y){
        this.img.setX(x);
        this.img.setY(y);
    }

    public boolean isAlternatingCard(Card compareCard){
        if(this.color+compareCard.color == 1){
            return true;
        }
        else {
            return false;
        }
    }


    public boolean isSameSuit(Card compareCard){
        if(this.suit == compareCard.suit){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isOneLower(Card compareCard){
        if(compareCard.rank - this.rank == 1){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isOneHigher(Card compareCard){
        if(this.rank - compareCard.rank == 1){
            return true;
        }
        else {
            return false;
        }
    }
}

