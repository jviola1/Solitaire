package com.example.jimmy.solitaire;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jay on 16/3/25.
 */
public class GameScene extends View{

    Context context;

    //width, height are attributes of ImageView of cards, marginLeft, marginTop, marginX, marginY are margins of ImageView of basciPiles
    int width;
    int height;
    int marginLeft = 100;
    int marginTop = scalePixels(250, true);
    int marginX = 50;
    int marginY = 300;

    static DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

    int touchCorrection;

    Rect screen = new Rect(10, 10, scalePixels(1300, false), scalePixels(1300, false));

    BasicPile[] basicPileList = new BasicPile[7];

    StockAndWaste stockAndWaste;

    FoundationPile[] foundationPiles = new FoundationPile[4];

    ArrayList<Card> tempList = new ArrayList<Card>();

    ArrayList<Card> allCards = new ArrayList<Card>();

    int lastIndexOfPile = -1;

    boolean flip = false;
    boolean reset = false;
    boolean isFromWaste = false;
    int fromIndexOfFoundation = -1;

    public GameScene(Context context, final RelativeLayout relativeLayout, int touchCorrection, int width, int height) {
        super(context);

        this.context = context;
        this.touchCorrection = touchCorrection;
        this.width = width;
        this.height = height;


        Log.i("TAG", "MarginTop " + marginTop);
        Log.i("TAG", "MarginLeft " + marginLeft);


        //initialize basicPiles and add them to basicPileList
        for(int i = 0;i<7;i++) {
            ImageView pileImg = new ImageView(context);
            pileImg.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                pileImg.setX(marginLeft+i*scalePixels((marginX + width), false));
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
        setFoundationPile(relativeLayout, (int) (metrics.widthPixels - (/*wasteImg.getX() +*/ marginX+width)), (int)wasteImg.getY(), 0, Card.Suit.Club);
        setFoundationPile(relativeLayout, (int) (metrics.widthPixels - ((marginX + width) * 2)),  (int) wasteImg.getY(), 1, Card.Suit.Spade);
        setFoundationPile(relativeLayout, (int) (metrics.widthPixels - ((marginX + width) * 3)), (int) wasteImg.getY(), 2, Card.Suit.Diamond);
        setFoundationPile(relativeLayout, (int) (metrics.widthPixels - ((marginX + width) * 4)), (int) wasteImg.getY(), 3, Card.Suit.Heart);


        final Button random = new Button(context);
        random.setLayoutParams(new RelativeLayout.LayoutParams(80, 80));
        random.setX(10);
        random.setY(100);
        random.setBackgroundColor(Color.RED);
        random.setText("random");
        random.setTextSize(7);
        random.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCards(relativeLayout);
                randomGame(relativeLayout);
            }
        });
        relativeLayout.addView(random);

        Button save = new Button(context);
        save.setLayoutParams(new RelativeLayout.LayoutParams(80, 80));
        save.setX(10);
        save.setY(220);
        save.setBackgroundColor(Color.GREEN);
        save.setText("save");
        save.setTextSize(7);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allCards.size() != 0) {
                    saveHelper();
                }
            }
        });
        relativeLayout.addView(save);

        Button load = new Button(context);
        load.setLayoutParams(new RelativeLayout.LayoutParams(80,80));
        load.setX(10);
        load.setY(340);
        load.setBackgroundColor(Color.BLUE);
        load.setText("load");
        load.setTextSize(7);
        load.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCards(relativeLayout);
                String[] pileInfo = loadHelper();
                String[] stockInfo = getPileString(pileInfo[0]);
                String[] wasteInfo = getPileString(pileInfo[1]);
                String[] foundation1Info = getPileString(pileInfo[2]);
                String[] foundation2Info = getPileString(pileInfo[3]);
                String[] foundation3Info = getPileString(pileInfo[4]);
                String[] foundation4Info = getPileString(pileInfo[5]);
                String[] basicPile1Info = getPileString(pileInfo[6]);
                String[] basicPile2Info = getPileString(pileInfo[7]);
                String[] basicPile3Info = getPileString(pileInfo[8]);
                String[] basicPile4Info = getPileString(pileInfo[9]);
                String[] basicPile5Info = getPileString(pileInfo[10]);
                String[] basicPile6Info = getPileString(pileInfo[11]);
                String[] basicPile7Info = getPileString(pileInfo[12]);

                if(stockInfo.length!=1) {
                    for (int i = 0; i < stockInfo.length; i++) {

                            int rank = Integer.parseInt(stockInfo[i].split(",")[1]);
                            String suitStr = stockInfo[i].split(",")[0];
                            switch (suitStr) {
                                case "spade":
                                    Card card = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Spade);
                                    allCards.add(card);
                                    stockAndWaste.stockList.remove(card);
                                    stockAndWaste.loadCardToStock(card);
                                    break;
                                case "heart":
                                    Card card1 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Heart);
                                    allCards.add(card1);
                                    stockAndWaste.stockList.remove(card1);
                                    stockAndWaste.loadCardToStock(card1);
                                    break;
                                case "diamond":
                                    Card card2 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Diamond);
                                    allCards.add(card2);
                                    stockAndWaste.stockList.remove(card2);
                                    stockAndWaste.loadCardToStock(card2);
                                    break;
                                case "club":
                                    Card card3 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Club);
                                    allCards.add(card3);
                                    stockAndWaste.stockList.remove(card3);
                                    stockAndWaste.loadCardToStock(card3);
                                    break;
                            }

                    }
                }
                else {
                    if(!stockInfo[0].equals("empty")){
                        int rank = Integer.parseInt(stockInfo[0].split(",")[1]);
                        String suitStr = stockInfo[0].split(",")[0];
                        switch (suitStr) {
                            case "spade":
                                Card card = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Spade);
                                allCards.add(card);
                                stockAndWaste.stockList.remove(card);
                                stockAndWaste.loadCardToStock(card);
                                break;
                            case "heart":
                                Card card1 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Heart);
                                allCards.add(card1);
                                stockAndWaste.stockList.remove(card1);
                                stockAndWaste.loadCardToStock(card1);
                                break;
                            case "diamond":
                                Card card2 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Diamond);
                                allCards.add(card2);
                                stockAndWaste.stockList.remove(card2);
                                stockAndWaste.loadCardToStock(card2);
                                break;
                            case "club":
                                Card card3 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Club);
                                allCards.add(card3);
                                stockAndWaste.stockList.remove(card3);
                                stockAndWaste.loadCardToStock(card3);
                                break;
                        }
                    }
                }

                if(wasteInfo.length!=1) {
                    for (int i = 0; i < wasteInfo.length; i++) {

                            int rank = Integer.parseInt(wasteInfo[i].split(",")[1]);
                            String suitStr = wasteInfo[i].split(",")[0];

                            switch (suitStr) {
                                case "spade":
                                    Card card = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Spade);
                                    allCards.add(card);
                                    stockAndWaste.stockList.remove(card);
                                    stockAndWaste.loadCardToWaste(card);
                                    break;
                                case "heart":
                                    Card card1 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Heart);
                                    allCards.add(card1);
                                    stockAndWaste.stockList.remove(card1);
                                    stockAndWaste.loadCardToWaste(card1);
                                    break;
                                case "diamond":
                                    Card card2 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Diamond);
                                    allCards.add(card2);
                                    stockAndWaste.stockList.remove(card2);
                                    stockAndWaste.loadCardToWaste(card2);
                                    break;
                                case "club":
                                    Card card3 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Club);
                                    allCards.add(card3);
                                    stockAndWaste.stockList.remove(card3);
                                    stockAndWaste.loadCardToWaste(card3);
                                    break;
                            }

                    }
                }
                else {
                    if(!wasteInfo[0].equals("empty")){
                        int rank = Integer.parseInt(wasteInfo[0].split(",")[1]);
                        String suitStr = wasteInfo[0].split(",")[0];

                        switch (suitStr) {
                            case "spade":
                                Card card = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Spade);
                                allCards.add(card);
                                stockAndWaste.stockList.remove(card);
                                stockAndWaste.loadCardToWaste(card);
                                break;
                            case "heart":
                                Card card1 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Heart);
                                allCards.add(card1);
                                stockAndWaste.stockList.remove(card1);
                                stockAndWaste.loadCardToWaste(card1);
                                break;
                            case "diamond":
                                Card card2 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Diamond);
                                allCards.add(card2);
                                stockAndWaste.stockList.remove(card2);
                                stockAndWaste.loadCardToWaste(card2);
                                break;
                            case "club":
                                Card card3 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Club);
                                allCards.add(card3);
                                stockAndWaste.stockList.remove(card3);
                                stockAndWaste.loadCardToWaste(card3);
                                break;
                        }
                    }
                }

                loadCardsInFoundationPile(foundation1Info, foundationPiles[0], relativeLayout);
                loadCardsInFoundationPile(foundation2Info, foundationPiles[1], relativeLayout);
                loadCardsInFoundationPile(foundation3Info, foundationPiles[2], relativeLayout);
                loadCardsInFoundationPile(foundation4Info, foundationPiles[3], relativeLayout);

                loadCardsInBasicPile(basicPile1Info, basicPileList[0], relativeLayout);
                loadCardsInBasicPile(basicPile2Info, basicPileList[1], relativeLayout);
                loadCardsInBasicPile(basicPile3Info, basicPileList[2], relativeLayout);
                loadCardsInBasicPile(basicPile4Info, basicPileList[3], relativeLayout);
                loadCardsInBasicPile(basicPile5Info, basicPileList[4], relativeLayout);
                loadCardsInBasicPile(basicPile6Info, basicPileList[5], relativeLayout);
                loadCardsInBasicPile(basicPile7Info, basicPileList[6], relativeLayout);

            }
        });
        relativeLayout.addView(load);

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
                        if(foundationPiles[i].foundationList.size()!=0){
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

    public void randomGame(RelativeLayout relativeLayout){
        //initialize all the 52 cards and add them to stockList of stockAndWaste
        load52Cards(relativeLayout);

        //pick random cards from stockList and add them to the specific basicPile
        basicPileList[0].setCardsToCardList(getRandomCardsToPile(1, stockAndWaste.stockList));
        basicPileList[1].setCardsToCardList(getRandomCardsToPile(2, stockAndWaste.stockList));
        basicPileList[2].setCardsToCardList(getRandomCardsToPile(3, stockAndWaste.stockList));
        basicPileList[3].setCardsToCardList(getRandomCardsToPile(4, stockAndWaste.stockList));
        basicPileList[4].setCardsToCardList(getRandomCardsToPile(5, stockAndWaste.stockList));
        basicPileList[5].setCardsToCardList(getRandomCardsToPile(6, stockAndWaste.stockList));
        basicPileList[6].setCardsToCardList(getRandomCardsToPile(7, stockAndWaste.stockList));
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

    private void load52Cards(RelativeLayout relativeLayout){
        for(int i=0;i<4;i++){
            switch (i){
                case 0:
                    load13SameSuitCards(Card.Suit.Club, relativeLayout);
                    break;
                case 1:
                    load13SameSuitCards(Card.Suit.Spade, relativeLayout);
                    break;
                case 2:
                    load13SameSuitCards(Card.Suit.Diamond, relativeLayout);
                    break;
                case 3:
                    load13SameSuitCards(Card.Suit.Heart, relativeLayout);
                    break;
            }
        }
    }

    private void load13SameSuitCards(Card.Suit suit, RelativeLayout relativeLayout){
        for(int i=1;i<=13;i++){
            try {
                Card card = new Card(stockAndWaste, getCardImageView(relativeLayout), i, suit);
                allCards.add(card);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void saveHelper(){

        String info = stockAndWaste.getStockCardListInfo()+"##";
        info += stockAndWaste.getWasteCardListInfo()+"##";
        for(int i = 0;i<4;i++){
            info += foundationPiles[i].getCardListInfo()+"##";
        }
        for(int i = 0;i<7;i++){
            info += basicPileList[i].getCardListInfo()+"##";
        }

        try{
            FileOutputStream check = context.openFileOutput("game.sav", Context.MODE_APPEND);
            check.close();
            FileOutputStream outputStream = context.openFileOutput("game.sav", Context.MODE_PRIVATE);
            outputStream.write(info.getBytes());
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private String[] loadHelper(){

        String[] PileInfo = new String[13];

        try {
            FileInputStream inputStream = context.openFileInput("game.sav");
            byte[] temp = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            while ((len = inputStream.read(temp))>0){
                sb.append(new String(temp, 0, len));
            }

            inputStream.close();

            PileInfo = sb.toString().split("##");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return PileInfo;
    }

    private void clearCards(RelativeLayout relativeLayout){
        for(int i = 0;i<allCards.size();i++) {
            relativeLayout.removeView(allCards.get(i).img);
        }
        allCards.clear();
        for(int i=0;i<4;i++){
            foundationPiles[i].foundationList.clear();
        }
        for(int i=0;i<7;i++){
            basicPileList[i].cardList.clear();
        }
        stockAndWaste.wasteList.clear();
        stockAndWaste.stockList.clear();
    }

    private String[] getPileString(String info){
        String[] str = null;
        if(info.equals("empty")){
            str = new String[]{"empty"};
        }
        else {
            str = info.split("&");
        }
        return str;
    }

    private void loadCardsInFoundationPile(String[] foundationInfo, FoundationPile foundationPile, RelativeLayout relativeLayout){
        if(foundationInfo.length!=1) {
            for (int i = 0; i < foundationInfo.length; i++) {
                int rank = Integer.parseInt(foundationInfo[i].split(",")[1]);
                String suitStr = foundationInfo[i].split(",")[0];
                switch (suitStr){
                    case "spade":
                        Card card1 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Spade);
                        allCards.add(card1);
                        stockAndWaste.stockList.remove(card1);
                        foundationPile.addCard(card1);
                        break;
                    case "heart":
                        Card card2 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Heart);
                        allCards.add(card2);
                        stockAndWaste.stockList.remove(card2);
                        foundationPile.addCard(card2);
                        break;
                    case "diamond":
                        Card card3 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Diamond);
                        allCards.add(card3);
                        stockAndWaste.stockList.remove(card3);
                        foundationPile.addCard(card3);
                        break;
                    case "club":
                        Card card4 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Club);
                        allCards.add(card4);
                        stockAndWaste.stockList.remove(card4);
                        foundationPile.addCard(card4);
                        break;
                }
            }
        }
        else {
            if(!foundationInfo[0].equals("empty")){
                int rank = Integer.parseInt(foundationInfo[0].split(",")[1]);
                String suitStr = foundationInfo[0].split(",")[0];

                switch (suitStr){
                    case "spade":
                        Card card = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Spade);
                        allCards.add(card);
                        stockAndWaste.stockList.remove(card);
                        foundationPile.addCard(card);
                        break;
                    case "heart":
                        Card card1 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Heart);
                        allCards.add(card1);
                        stockAndWaste.stockList.remove(card1);
                        foundationPile.addCard(card1);
                        break;
                    case "diamond":
                        Card card2 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Diamond);
                        allCards.add(card2);
                        stockAndWaste.stockList.remove(card2);
                        foundationPile.addCard(card2);
                        break;
                    case "club":
                        Card card3 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Club);
                        allCards.add(card3);
                        stockAndWaste.stockList.remove(card3);
                        foundationPile.addCard(card3);
                        break;
                }


            }
        }
    }

    private void loadCardsInBasicPile(String[] basicPileInfo, BasicPile basicPile, RelativeLayout relativeLayout){

        if(basicPileInfo.length==1){
            if(!basicPileInfo[0].equals("empty")){
                int rank = Integer.parseInt(basicPileInfo[0].split(",")[1]);
                String suitStr = basicPileInfo[0].split(",")[0];
                boolean isOpen = basicPileInfo[0].split(",")[2].equals("open");

                switch (suitStr){
                    case "spade":
                        Card card = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Spade);
                        allCards.add(card);
                        stockAndWaste.stockList.remove(card);
                        if(isOpen){
                            card.openCard();
                        }
                        basicPile.addCard(card);
                        break;
                    case "heart":
                        Card card1 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Heart);
                        allCards.add(card1);
                        stockAndWaste.stockList.remove(card1);
                        if(isOpen){
                            card1.openCard();
                        }
                        basicPile.addCard(card1);
                        break;
                    case "diamond":
                        Card card2 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Diamond);
                        allCards.add(card2);
                        stockAndWaste.stockList.remove(card2);
                        if(isOpen){
                            card2.openCard();
                        }
                        basicPile.addCard(card2);
                        break;
                    case "club":
                        Card card3 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Club);
                        allCards.add(card3);
                        stockAndWaste.stockList.remove(card3);
                        if(isOpen){
                            card3.openCard();
                        }
                        basicPile.addCard(card3);
                        break;
                }


            }
        }
        else {
            for(int i = 0;i<basicPileInfo.length;i++){
                int rank = Integer.parseInt(basicPileInfo[i].split(",")[1]);
                String suitStr = basicPileInfo[i].split(",")[0];
                boolean isOpen = basicPileInfo[i].split(",")[2].equals("open");

                switch (suitStr){
                    case "club":
                        Card card1 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Club);
                        allCards.add(card1);
                        stockAndWaste.stockList.remove(card1);
                        if(isOpen){
                            card1.openCard();
                        }
                        basicPile.addCard(card1);
                        break;
                    case "diamond":
                        Card card2 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Diamond);
                        allCards.add(card2);
                        stockAndWaste.stockList.remove(card2);
                        if(isOpen){
                            card2.openCard();
                        }
                        basicPile.addCard(card2);
                        break;
                    case "spade":
                        Card card3 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Spade);
                        allCards.add(card3);
                        stockAndWaste.stockList.remove(card3);
                        if(isOpen){
                            card3.openCard();
                        }
                        basicPile.addCard(card3);
                        break;
                    case "heart":
                        Card card4 = new Card(stockAndWaste, getCardImageView(relativeLayout), rank, Card.Suit.Heart);
                        allCards.add(card4);
                        stockAndWaste.stockList.remove(card4);
                        if(isOpen){
                            card4.openCard();
                        }
                        basicPile.addCard(card4);
                        break;
                }
            }
        }
    }

    public static int scalePixels(int px, boolean isVertical)
    {
        if(isVertical)
            return (int) (px * ((metrics.heightPixels) / 768.0));
        return (int) (px * ((metrics.widthPixels) / 1280.0));

    }

}
