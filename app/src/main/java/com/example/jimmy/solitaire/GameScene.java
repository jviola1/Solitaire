package com.example.jimmy.solitaire;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class GameScene extends View{

    Context context;

    //width, height are attributes of ImageView of cards, marginLeft, marginTop, marginX, marginY are margins of ImageView of basciPiles
    int width = 93;
    int height = 143;
    int marginLeft = 100;
    int marginTop = 250;
    int marginX = 50;
    int marginY = 300;

    int touchCorrection;

    Rect screen = new Rect(10, 10, 1200, 1200);

    BasicPile[] basicPileList = new BasicPile[7];

    StockAndWaste stockAndWaste;

    FoundationPile[] foundationPiles = new FoundationPile[4];

    ArrayList<Card> tempList = new ArrayList<Card>();

    int lastIndexOfPile = -1;

    boolean flip = false;
    boolean reset = false;
    boolean isFromWaste = false;
    int fromIndexOfFoundation = -1;

    public GameScene(Context context, RelativeLayout relativeLayout, int touchCorrection, int width, int height) {
        super(context);

        this.context = context;
        this.touchCorrection = touchCorrection;
        this.width = width;
        this.height = height;

        //initialize basicPiles and add them to basicPileList
        for(int i = 0;i<7;i++) {
            ImageView pileImg = new ImageView(context);
            pileImg.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                pileImg.setX(marginLeft+i*(marginX+width));
                pileImg.setY(marginTop);
            }
            else {
                if(i<4){
                    pileImg.setX(marginLeft+i*(marginX+width));
                    pileImg.setY(marginTop);
                }
                else {
                    pileImg.setX(marginLeft+(i-4)*(marginX+width));
                    pileImg.setY(marginTop+height+marginY);
                }
            }
            pileImg.setBackgroundColor(Color.GRAY);
            relativeLayout.addView(pileImg);

            basicPileList[i] = new BasicPile(new Point((int)pileImg.getX(), (int)pileImg.getY()));
            basicPileList[i].width = width;
            basicPileList[i].height = height;
        }

        //initialize stockAndWaste
        ImageView stockImg = new ImageView(context);
        stockImg.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        stockImg.setBackgroundColor(Color.GRAY);
        stockImg.setX(100);
        stockImg.setY(70);
        relativeLayout.addView(stockImg);

        ImageView wasteImg = new ImageView(context);
        wasteImg.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        wasteImg.setBackgroundColor(Color.GRAY);
        wasteImg.setX(100 + marginX + width);
        wasteImg.setY(70);
        relativeLayout.addView(wasteImg);

        stockAndWaste = new StockAndWaste(new Point((int)stockImg.getX(), (int)stockImg.getY()), new Point((int)wasteImg.getX(), (int)wasteImg.getY()));
        stockAndWaste.width = width;
        stockAndWaste.height = height;

        //initialize foundationPiles
        setFoundationPile(relativeLayout, (int)wasteImg.getX()+marginX+width, (int)wasteImg.getY(), 0, Card.Suit.Club);
        setFoundationPile(relativeLayout, (int)wasteImg.getX()+(marginX+width)*2, (int)wasteImg.getY(), 1, Card.Suit.Spade);
        setFoundationPile(relativeLayout, (int)wasteImg.getX()+(marginX+width)*3, (int)wasteImg.getY(), 2, Card.Suit.Diamond);
        setFoundationPile(relativeLayout, (int)wasteImg.getX()+(marginX+width)*4, (int)wasteImg.getY(), 3, Card.Suit.Heart);

        //initialize all the 52 cards and add them to stockList of stockAndWaste
        Card club1 = new Card(stockAndWaste, getCardImageView(relativeLayout), 1, Card.Suit.Club, R.drawable.club1);
        Card club2 = new Card(stockAndWaste, getCardImageView(relativeLayout), 2, Card.Suit.Club, R.drawable.club2);
        Card club3 = new Card(stockAndWaste, getCardImageView(relativeLayout), 3, Card.Suit.Club, R.drawable.club3);
        Card club4 = new Card(stockAndWaste, getCardImageView(relativeLayout), 4, Card.Suit.Club, R.drawable.club4);
        Card club5 = new Card(stockAndWaste, getCardImageView(relativeLayout), 5, Card.Suit.Club, R.drawable.club5);
        Card club6 = new Card(stockAndWaste, getCardImageView(relativeLayout), 6, Card.Suit.Club, R.drawable.club6);
        Card club7 = new Card(stockAndWaste, getCardImageView(relativeLayout), 7, Card.Suit.Club, R.drawable.club7);
        Card club8 = new Card(stockAndWaste, getCardImageView(relativeLayout), 8, Card.Suit.Club, R.drawable.club8);
        Card club9 = new Card(stockAndWaste, getCardImageView(relativeLayout), 9, Card.Suit.Club, R.drawable.club9);
        Card club10 = new Card(stockAndWaste, getCardImageView(relativeLayout), 10, Card.Suit.Club, R.drawable.club10);
        Card club11 = new Card(stockAndWaste, getCardImageView(relativeLayout), 11, Card.Suit.Club, R.drawable.club11);
        Card club12 = new Card(stockAndWaste, getCardImageView(relativeLayout), 12, Card.Suit.Club, R.drawable.club12);
        Card club13 = new Card(stockAndWaste, getCardImageView(relativeLayout), 13, Card.Suit.Club, R.drawable.club13);

        Card diamond1 = new Card(stockAndWaste, getCardImageView(relativeLayout), 1, Card.Suit.Diamond, R.drawable.diamond1);
        Card diamond2 = new Card(stockAndWaste, getCardImageView(relativeLayout), 2, Card.Suit.Diamond, R.drawable.diamond2);
        Card diamond3 = new Card(stockAndWaste, getCardImageView(relativeLayout), 3, Card.Suit.Diamond, R.drawable.diamond3);
        Card diamond4 = new Card(stockAndWaste, getCardImageView(relativeLayout), 4, Card.Suit.Diamond, R.drawable.diamond4);
        Card diamond5 = new Card(stockAndWaste, getCardImageView(relativeLayout), 5, Card.Suit.Diamond, R.drawable.diamond5);
        Card diamond6 = new Card(stockAndWaste, getCardImageView(relativeLayout), 6, Card.Suit.Diamond, R.drawable.diamond6);
        Card diamond7 = new Card(stockAndWaste, getCardImageView(relativeLayout), 7, Card.Suit.Diamond, R.drawable.diamond7);
        Card diamond8 = new Card(stockAndWaste, getCardImageView(relativeLayout), 8, Card.Suit.Diamond, R.drawable.diamond8);
        Card diamond9 = new Card(stockAndWaste, getCardImageView(relativeLayout), 9, Card.Suit.Diamond, R.drawable.diamond9);
        Card diamond10 = new Card(stockAndWaste, getCardImageView(relativeLayout), 10, Card.Suit.Diamond, R.drawable.diamond10);
        Card diamond11 = new Card(stockAndWaste, getCardImageView(relativeLayout), 11, Card.Suit.Diamond, R.drawable.diamond11);
        Card diamond12 = new Card(stockAndWaste, getCardImageView(relativeLayout), 12, Card.Suit.Diamond, R.drawable.diamond12);
        Card diamond13 = new Card(stockAndWaste, getCardImageView(relativeLayout), 13, Card.Suit.Diamond, R.drawable.diamond13);

        Card spade1 = new Card(stockAndWaste, getCardImageView(relativeLayout), 1, Card.Suit.Spade, R.drawable.spade1);
        Card spade2 = new Card(stockAndWaste, getCardImageView(relativeLayout), 2, Card.Suit.Spade, R.drawable.spade2);
        Card spade3 = new Card(stockAndWaste, getCardImageView(relativeLayout), 3, Card.Suit.Spade, R.drawable.spade3);
        Card spade4 = new Card(stockAndWaste, getCardImageView(relativeLayout), 4, Card.Suit.Spade, R.drawable.spade4);
        Card spade5 = new Card(stockAndWaste, getCardImageView(relativeLayout), 5, Card.Suit.Spade, R.drawable.spade5);
        Card spade6 = new Card(stockAndWaste, getCardImageView(relativeLayout), 6, Card.Suit.Spade, R.drawable.spade6);
        Card spade7 = new Card(stockAndWaste, getCardImageView(relativeLayout), 7, Card.Suit.Spade, R.drawable.spade7);
        Card spade8 = new Card(stockAndWaste, getCardImageView(relativeLayout), 8, Card.Suit.Spade, R.drawable.spade8);
        Card spade9 = new Card(stockAndWaste, getCardImageView(relativeLayout), 9, Card.Suit.Spade, R.drawable.spade9);
        Card spade10 = new Card(stockAndWaste, getCardImageView(relativeLayout), 10, Card.Suit.Spade, R.drawable.spade10);
        Card spade11 = new Card(stockAndWaste, getCardImageView(relativeLayout), 11, Card.Suit.Spade, R.drawable.spade11);
        Card spade12 = new Card(stockAndWaste, getCardImageView(relativeLayout), 12, Card.Suit.Spade, R.drawable.spade12);
        Card spade13 = new Card(stockAndWaste, getCardImageView(relativeLayout), 13, Card.Suit.Spade, R.drawable.spade13);

        Card heart1 = new Card(stockAndWaste, getCardImageView(relativeLayout), 1, Card.Suit.Heart, R.drawable.heart1);
        Card heart2 = new Card(stockAndWaste, getCardImageView(relativeLayout), 2, Card.Suit.Heart, R.drawable.heart2);
        Card heart3 = new Card(stockAndWaste, getCardImageView(relativeLayout), 3, Card.Suit.Heart, R.drawable.heart3);
        Card heart4 = new Card(stockAndWaste, getCardImageView(relativeLayout), 4, Card.Suit.Heart, R.drawable.heart4);
        Card heart5 = new Card(stockAndWaste, getCardImageView(relativeLayout), 5, Card.Suit.Heart, R.drawable.heart5);
        Card heart6 = new Card(stockAndWaste, getCardImageView(relativeLayout), 6, Card.Suit.Heart, R.drawable.heart6);
        Card heart7 = new Card(stockAndWaste, getCardImageView(relativeLayout), 7, Card.Suit.Heart, R.drawable.heart7);
        Card heart8 = new Card(stockAndWaste, getCardImageView(relativeLayout), 8, Card.Suit.Heart, R.drawable.heart8);
        Card heart9 = new Card(stockAndWaste, getCardImageView(relativeLayout), 9, Card.Suit.Heart, R.drawable.heart9);
        Card heart10 = new Card(stockAndWaste, getCardImageView(relativeLayout), 10, Card.Suit.Heart, R.drawable.heart10);
        Card heart11 = new Card(stockAndWaste, getCardImageView(relativeLayout), 11, Card.Suit.Heart, R.drawable.heart11);
        Card heart12 = new Card(stockAndWaste, getCardImageView(relativeLayout), 12, Card.Suit.Heart, R.drawable.heart12);
        Card heart13 = new Card(stockAndWaste, getCardImageView(relativeLayout), 13, Card.Suit.Heart, R.drawable.heart13);

        //pick random cards from stockList and add them to the specific basicPile
        basicPileList[0].setCardsToCardList(getRandomCardsToPile(1, stockAndWaste.stockList));
        basicPileList[1].setCardsToCardList(getRandomCardsToPile(2, stockAndWaste.stockList));
        basicPileList[2].setCardsToCardList(getRandomCardsToPile(3, stockAndWaste.stockList));
        basicPileList[3].setCardsToCardList(getRandomCardsToPile(4, stockAndWaste.stockList));
        basicPileList[4].setCardsToCardList(getRandomCardsToPile(5, stockAndWaste.stockList));
        basicPileList[5].setCardsToCardList(getRandomCardsToPile(6, stockAndWaste.stockList));
        basicPileList[6].setCardsToCardList(getRandomCardsToPile(7, stockAndWaste.stockList));


    }

    //set touchevent
    public boolean onTouchEvent(MotionEvent e) {

        int action = MotionEventCompat.getActionMasked(e);

        switch (action) {

            //recognize the touch down point, if it is in the valid area, add cards to tempList or do reset and filp actions of stockAndWaste
            case MotionEvent.ACTION_DOWN:

                if(stockAndWaste.stockArea.contains((int) e.getRawX(), (int) e.getRawY() + touchCorrection)){
                    if(stockAndWaste.stockList.size()!=0){
                        flip = true;
                    }
                    else {
                        reset = true;
                    }
                }
                else if(stockAndWaste.wasteArea.contains((int) e.getRawX(), (int) e.getRawY() + touchCorrection)&&stockAndWaste.wasteList.size()!=0){

                    stockAndWaste.addCardToTempList(tempList);
                    isFromWaste = true;

                }



                for(int i = 0; i < foundationPiles.length; i++){
                    if(foundationPiles[i].area.contains((int) e.getRawX(), (int) e.getRawY() + touchCorrection)){
                        if(foundationPiles[i].card!=null){
                            foundationPiles[i].addCardToTemplist(tempList);
                            fromIndexOfFoundation = i;
                        }
                    }
                }

                for (int i = 0; i < basicPileList.length; i++) {

                    if (basicPileList[i].area.contains((int) e.getRawX(), (int) e.getRawY() + touchCorrection)) {

                        lastIndexOfPile = i;
                        basicPileList[i].addCardsSelectedToList(tempList, (int) e.getRawX(), (int) e.getRawY() + touchCorrection);
                        if (tempList.size() != 0) {
                            basicPileList[i].removeCardsFromCardToLast(tempList.get(0));
                        }

                        break;
                    }

                }

                if (tempList.size() != 0) {
                    for (int i = 0; i < tempList.size(); i++) {
                        tempList.get(i).setLocation((int) e.getRawX() - 50, (int) e.getRawY() + touchCorrection - 50 + 40 * i);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tempList.get(i).img.setElevation(200+i);
                        }
                    }
                }

                return true;

            //make the selected cards(cards in tempList) move with the touch point
            case MotionEvent.ACTION_MOVE:

                if (screen.contains((int) e.getRawX(), (int) e.getRawY() + touchCorrection)) {
                    if (tempList.size() != 0) {
                        for (int i = 0; i < tempList.size(); i++) {
                            tempList.get(i).setLocation((int) e.getRawX() - 50, (int) e.getRawY() + touchCorrection - 50 + 40 * i);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                tempList.get(i).img.setElevation(100 + i);
                            }
                        }
                    }

                } else {
                    if (tempList.size() != 0) {
                        basicPileList[lastIndexOfPile].addCardFromList(tempList);
                        tempList.clear();
                    }
                }

                return true;

            //recognize touch up point, if it is in the valid area, add cards in tempList to valid Pile and reset the location of cards in tempList
            //if it is not in valid area, return cards to the original Pile and reset the location of cards in tempList
            case MotionEvent.ACTION_UP:

                if(stockAndWaste.stockArea.contains((int) e.getRawX(), (int) e.getRawY() + touchCorrection)){
                    if(reset==true){
                        stockAndWaste.reset();
                        reset = false;
                    }
                    else if(flip==true){
                        stockAndWaste.flip();
                        flip = false;
                    }
                }


                if (screen.contains((int) e.getRawY(), (int) e.getRawY() + touchCorrection)) {

                    if (tempList.size() != 0) {

                        for(int i = 0; i<foundationPiles.length;i++){
                            if(foundationPiles[i].area.contains((int) e.getRawX(), (int) e.getRawY() + touchCorrection)&&foundationPiles[i].addCardFromTempList(tempList)){
                                tempList.clear();
                                isFromWaste=false;
                                fromIndexOfFoundation = -1;

                                break;
                            }
                        }

                        for (int i = 0; i < basicPileList.length; i++) {
                            if (basicPileList[i].area.contains((int) e.getRawX(), (int) e.getRawY() + touchCorrection)) {

                                if (basicPileList[i].addCardFromList(tempList)) {
                                    tempList.clear();
                                    if(lastIndexOfPile!=-1) {
                                        basicPileList[lastIndexOfPile].openLastCard();
                                        lastIndexOfPile = -1;
                                    }
                                    else if(isFromWaste==true){
                                        isFromWaste = false;
                                    }
                                    else if(fromIndexOfFoundation != -1){
                                        fromIndexOfFoundation = -1;
                                    }


                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                        returnCardsBack();
                    }

                } else {
                    returnCardsBack();
                }

                for(int i = 0;i<7;i++) {
                    BasicPile bp = basicPileList[i];
                    if(bp.cardList.size()!=0) {
                        if (!bp.cardList.get(bp.cardList.size() - 1).isOpen()) {
                            bp.cardList.get(bp.cardList.size() - 1).openCard();
                        }
                    }
                }
                return true;


            default:
                return super.onTouchEvent(e);
        }
    }

    public void returnCardsBack(){
        if (tempList.size() != 0) {
            if(lastIndexOfPile!=-1) {
                basicPileList[lastIndexOfPile].returnCards(tempList);
                tempList.clear();
                lastIndexOfPile=-1;
            }
            else if(isFromWaste==true){
                stockAndWaste.returnCards(tempList);
                tempList.clear();
                isFromWaste = false;
            }
            else if(fromIndexOfFoundation!=-1){
                foundationPiles[fromIndexOfFoundation].returnCards(tempList);
                tempList.clear();
                fromIndexOfFoundation = -1;
            }
        }
    }

    public ImageView getCardImageView(RelativeLayout relativeLayout){
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        relativeLayout.addView(imageView);

        return imageView;
    }

    public void setFoundationPile(RelativeLayout relativeLayout, int x, int y, int index, Card.Suit suit){
        ImageView FoundationPileImg = new ImageView(context);
        FoundationPileImg.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        FoundationPileImg.setBackgroundColor(Color.GRAY);
        FoundationPileImg.setX(x);
        FoundationPileImg.setY(y);
        relativeLayout.addView(FoundationPileImg);
        foundationPiles[index] = new FoundationPile(new Point((int)FoundationPileImg.getX(), (int)FoundationPileImg.getY()), suit);
        foundationPiles[index].width = width;
        foundationPiles[index].height = height;

        switch (suit) {
            case Heart:
                setFoundationHintLabel((int) FoundationPileImg.getX(), (int) FoundationPileImg.getY() - 40, "Heart", relativeLayout);
                break;
            case Club:
                setFoundationHintLabel((int) FoundationPileImg.getX(), (int) FoundationPileImg.getY() - 40, "Club", relativeLayout);
                break;
            case Diamond:
                setFoundationHintLabel((int) FoundationPileImg.getX(), (int) FoundationPileImg.getY() - 40, "Diamond", relativeLayout);
                break;
            case Spade:
                setFoundationHintLabel((int) FoundationPileImg.getX(), (int) FoundationPileImg.getY() - 40, "Spade", relativeLayout);
                break;

        }
    }

    public void setFoundationHintLabel(int x, int y, String hint, RelativeLayout relativeLayout){
        TextView labelClubAce = new TextView(context);
        labelClubAce.setText(hint);
        labelClubAce.setTextSize(12);
        labelClubAce.setTextColor(Color.GREEN);
        labelClubAce.setX(x);
        labelClubAce.setY(y);
        relativeLayout.addView(labelClubAce);
    }

    //method to pick specific number of random cards from an arrayList
    public Card[] getRandomCardsToPile(int numberOfCards, ArrayList<Card> cardList){
        Card[] cards = new Card[numberOfCards];

        for(int i = 0;i<numberOfCards;i++){
            int n = new Random().nextInt(cardList.size());
            cards[i] = cardList.get(n);
            cardList.remove(cardList.get(n));
        }

        return cards;
    }

}
