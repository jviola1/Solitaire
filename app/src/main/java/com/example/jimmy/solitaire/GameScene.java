package com.example.jimmy.solitaire;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.view.MotionEventCompat;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by jay on 16/3/25.
 */
public class GameScene extends View{

    Context context;

    //width, height are attributes of ImageView of cards, marginLeft, marginTop, marginX, marginY are margins of ImageView of basciPiles
    int width;
    int height;
    int marginLeft = scalePixels(100, false);
    int marginTop = scalePixels(250, true);
    int marginX = scalePixels(50, false);
    int marginY = 300;

    RelativeLayout relativeLayout;

    static DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

    int touchCorrection;

    Rect screen = new Rect(10, 10, scalePixels(1300, false), scalePixels(1300, false));

    BasicPile[] basicPileList = new BasicPile[7];

    StockAndWaste stockAndWaste;

    FoundationPile[] foundationPiles = new FoundationPile[4];

    ArrayList<Card> tempList = new ArrayList<Card>();

    ArrayList<Card> allCards = new ArrayList<Card>();

    TextView name, timeView;

    Thread t;

    int lastIndexOfPile = -1;

    boolean flip = false;
    boolean reset = false;
    boolean isFromWaste = false;
    int fromIndexOfFoundation = -1;

    public GameScene(final Context context, final RelativeLayout relativeLayout, int touchCorrection, int width, int height) {
        super(context);

        this.context = context;
        this.touchCorrection = touchCorrection;
        this.width = width;
        this.height = height;
        this.relativeLayout = relativeLayout;

        //initialize basicPiles and add them to basicPileList
        for(int i = 0;i<7;i++) {
            ImageView pileImg = new ImageView(context);
            pileImg.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                pileImg.setX(marginLeft+i*(marginX + width));
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
        setFoundationPile((int) (metrics.widthPixels - (/*wasteImg.getX() +*/ marginX + width)), (int) wasteImg.getY(), 0, Card.Suit.Club);
        setFoundationPile((int) (metrics.widthPixels - ((marginX + width) * 2)), (int) wasteImg.getY(), 1, Card.Suit.Spade);
        setFoundationPile((int) (metrics.widthPixels - ((marginX + width) * 3)), (int) wasteImg.getY(), 2, Card.Suit.Diamond);
        setFoundationPile((int) (metrics.widthPixels - ((marginX + width) * 4)), (int) wasteImg.getY(), 3, Card.Suit.Heart);

        Button menu = new Button(context);
        menu.setLayoutParams(new RelativeLayout.LayoutParams(80, 80));
        menu.setX(10);
        menu.setY(100);
        menu.setBackgroundColor(Color.RED);
        menu.setText("Menu");
        menu.setTextSize(7);
        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Menu")
                        .setMessage("Select function:")
                        .setNegativeButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(allCards.size()!=0){
                                    saveHelper();
                                }
                            }
                        })
                        .setPositiveButton("Road", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadGame();
                            }
                        })
                        .setNeutralButton("New Game", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                randomGame();
                            }
                        })
                        .show();
            }
        });
        relativeLayout.addView(menu);

        name = new TextView(context);
        name.setLayoutParams(new RelativeLayout.LayoutParams(100, 40));
        name.setX(20);
        name.setY(20);
        name.setTextColor(Color.BLUE);
        name.setTextSize(10);
        name.setText("unknown");
        relativeLayout.addView(name);

        timeView = new TextView(context);
        timeView.setLayoutParams(new RelativeLayout.LayoutParams(100, 40));
        timeView.setX(200);
        timeView.setY(20);
        timeView.setTextColor(Color.RED);
        timeView.setTextSize(10);
        timeView.setText("0");
        relativeLayout.addView(timeView);
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

                gameClear();

                return true;


            default:
                return super.onTouchEvent(e);
        }
    }



    private void gameClear(){
        if(isGameClear()){

            //GameFileHelper.RankSaver(context, );

            new AlertDialog.Builder(context)
                    .setTitle("Message:")
                    .setMessage("Game Clear, congratulation!!!")
                    .setPositiveButton("Start New Game", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            randomGame();
                        }
                    })
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            // field for back to main page
                        }
                    })
                    .show();
        }
    }

    private boolean isGameClear(){
        if(stockAndWaste.isEmpty()&&isFoundationPilesEmpty()&&isBasicPilesClear()&&isAllCardsOpen()){
            return true;
        }
        else
            return false;
    }

    private boolean isFoundationPilesEmpty(){
        int c = 0;
        for(int i = 0;i<4;i++){
            if(foundationPiles[i].foundationList.size()!=0){
                c += 1;
            }
        }
        if (c == 0){
            return true;
        }
        else
            return false;
    }

    private boolean isAllCardsOpen(){
        int numberOfOpenCars = 0;
        for (int i = 0;i<52;i++){
            if(allCards.get(i).isOpen()){
                numberOfOpenCars += 1;
            }
        }

        if(numberOfOpenCars == 52){
            return true;
        }
        else
            return false;
    }

    private boolean isBasicPilesClear(){
        int c = 0;
        for(int i=0;i<7;i++){
            if(basicPileList[i].cardList.size()!=0){
                c += 1;
            }
        }
        if(c==4){
            return true;
        }
        else
            return false;
    }

    //for test
    private void openAllCardsInBasicPiles(){
        for (int i = 0; i<7;i++){
            for(int j = 0;j<basicPileList[i].cardList.size();j++){
                basicPileList[i].cardList.get(j).openCard();
            }
        }
    }

    private void returnCardsBack(){
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

    private void nameInputDialog(){

        final EditText editText = new EditText(context);

        new AlertDialog.Builder(context)
                .setTitle("Hello")
                .setMessage("Please input a player name:")
                .setView(editText)
                .setPositiveButton("Play", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name.setText(editText.getText());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void randomGame(){

        nameInputDialog();

        clearCards();
        //initialize all the 52 cards and add them to stockList of stockAndWaste
        load52Cards();
        shuffle();

        timeView.setText("0");

        //pick random cards from stockList and add them to the specific basicPile
        basicPileList[0].setCardsToCardList(getRandomCardsToPile(1, stockAndWaste.stockList));
        basicPileList[1].setCardsToCardList(getRandomCardsToPile(2, stockAndWaste.stockList));
        basicPileList[2].setCardsToCardList(getRandomCardsToPile(3, stockAndWaste.stockList));
        basicPileList[3].setCardsToCardList(getRandomCardsToPile(4, stockAndWaste.stockList));
        basicPileList[4].setCardsToCardList(getRandomCardsToPile(5, stockAndWaste.stockList));
        basicPileList[5].setCardsToCardList(getRandomCardsToPile(6, stockAndWaste.stockList));
        basicPileList[6].setCardsToCardList(getRandomCardsToPile(7, stockAndWaste.stockList));
    }

    private ImageView getCardImageView(){
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        relativeLayout.addView(imageView);

        return imageView;
    }

    private void setFoundationPile(int x, int y, int index, Card.Suit suit){
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
                setFoundationHintLabel((int) FoundationPileImg.getX(), (int) FoundationPileImg.getY() - 40, "Heart");
                break;
            case Club:
                setFoundationHintLabel((int) FoundationPileImg.getX(), (int) FoundationPileImg.getY() - 40, "Club");
                break;
            case Diamond:
                setFoundationHintLabel((int) FoundationPileImg.getX(), (int) FoundationPileImg.getY() - 40, "Diamond");
                break;
            case Spade:
                setFoundationHintLabel((int) FoundationPileImg.getX(), (int) FoundationPileImg.getY() - 40, "Spade");
                break;

        }
    }

    private void setFoundationHintLabel(int x, int y, String hint){
        TextView labelClubAce = new TextView(context);
        labelClubAce.setText(hint);
        labelClubAce.setTextSize(12);
        labelClubAce.setTextColor(Color.GREEN);
        labelClubAce.setX(x);
        labelClubAce.setY(y);
        relativeLayout.addView(labelClubAce);
    }

    //method to pick specific number of random cards from an arrayList
    private Card[] getRandomCardsToPile(int numberOfCards, ArrayList<Card> cardList){
        Card[] cards = new Card[numberOfCards];

        for(int i = 0;i<numberOfCards;i++){
            int n = new Random().nextInt(cardList.size());
            cards[i] = cardList.get(n);
            cardList.remove(cardList.get(n));
        }

        return cards;
    }

    private void shuffle(){
        ArrayList<Card> tempCards = new ArrayList<Card>();
        for(int i = 0;i < 52;i++){
            tempCards.add(stockAndWaste.stockList.get(i));
        }
        stockAndWaste.stockList.clear();
        for(int i = 0;i < 52;i++){
            int n = new Random().nextInt(tempCards.size());
            stockAndWaste.stockList.add(tempCards.get(n));
            tempCards.remove(n);
        }

        Log.d("test:",String.valueOf(stockAndWaste.stockList.size()));
    }

    private void load52Cards(){
        for(int i=0;i<4;i++){
            switch (i){
                case 0:
                    load13SameSuitCards(Card.Suit.Club);
                    break;
                case 1:
                    load13SameSuitCards(Card.Suit.Spade);
                    break;
                case 2:
                    load13SameSuitCards(Card.Suit.Diamond);
                    break;
                case 3:
                    load13SameSuitCards(Card.Suit.Heart);
                    break;
            }
        }
    }

    private void load13SameSuitCards(Card.Suit suit){
        for(int i=1;i<=13;i++){
            try {
                Card card = new Card(stockAndWaste, getCardImageView(), i, suit);
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

        info += name.getText()+"##";
        info += timeView.getText()+"##";

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

    private void clearCards(){
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

    private void loadGame(){
        clearCards();
        String[] pileInfo = loadHelper();

        String[] stockInfo = getPileString(pileInfo[0]);
        String[] wasteInfo = getPileString(pileInfo[1]);
        loadCardsInStockAndWaste(stockInfo, wasteInfo);

        for(int i = 2;i<13;i++){
            if(i<6){
                loadCardsInFoundationPile(getPileString(pileInfo[i]), foundationPiles[i-2]);
            }
            else {
                loadCardsInBasicPile(getPileString(pileInfo[i]), basicPileList[i-6]);
            }
        }


        name.setText(pileInfo[13]);
        timeView.setText(String.valueOf(pileInfo[14]));

        gameClear();
    }

    private void loadCardsInFoundationPile(String[] foundationInfo, FoundationPile foundationPile){
        if(foundationInfo.length!=1) {
            for (int i = 0; i < foundationInfo.length; i++) {
                int rank = Integer.parseInt(foundationInfo[i].split(",")[1]);
                String suitStr = foundationInfo[i].split(",")[0];

                foundationPile.addCard(createFreeCard(rank, suitStr));

            }
        }
        else {
            if(!foundationInfo[0].equals("empty")){
                int rank = Integer.parseInt(foundationInfo[0].split(",")[1]);
                String suitStr = foundationInfo[0].split(",")[0];

                foundationPile.addCard(createFreeCard(rank, suitStr));



            }
        }
    }

    private Card createFreeCard(int rank, String suitStr){
        Card card = null;
        switch (suitStr) {
            case "spade":
                card = createCardWithSuit(rank, Card.Suit.Spade);
                break;
            case "heart":
                card = createCardWithSuit(rank, Card.Suit.Heart);
                break;
            case "diamond":
                card = createCardWithSuit(rank, Card.Suit.Diamond);
                break;
            case "club":
                card = createCardWithSuit(rank, Card.Suit.Club);
                break;
        }

        return card;
    }

    private Card createCardWithSuit(int rank, Card.Suit suit){
        Card card = new Card(stockAndWaste, getCardImageView(), rank, suit);
        allCards.add(card);
        stockAndWaste.stockList.remove(card);

        return card;
    }

    private void loadCardsInStockAndWaste(String[] stockInfo, String[] wasteInfo){
        if(stockInfo.length!=1) {
            for (int i = 0; i < stockInfo.length; i++) {

                int rank = Integer.parseInt(stockInfo[i].split(",")[1]);
                String suitStr = stockInfo[i].split(",")[0];

                stockAndWaste.loadCardToStock(createFreeCard(rank, suitStr));

            }
        }
        else {
            if(!stockInfo[0].equals("empty")){
            int rank = Integer.parseInt(stockInfo[0].split(",")[1]);
            String suitStr = stockInfo[0].split(",")[0];

            stockAndWaste.loadCardToStock(createFreeCard(rank, suitStr));
            }
        }
        if(wasteInfo.length!=1) {
            for (int i = 0; i < wasteInfo.length; i++) {

                int rank = Integer.parseInt(wasteInfo[i].split(",")[1]);
                String suitStr = wasteInfo[i].split(",")[0];

                stockAndWaste.loadCardToWaste(createFreeCard(rank, suitStr));


            }
        }
        else {
            if(!wasteInfo[0].equals("empty")){
                int rank = Integer.parseInt(wasteInfo[0].split(",")[1]);
                String suitStr = wasteInfo[0].split(",")[0];

                stockAndWaste.loadCardToWaste(createFreeCard(rank, suitStr));

            }
        }
    }

    private void loadCardsInBasicPile(String[] basicPileInfo, BasicPile basicPile){

        if(basicPileInfo.length==1){
            if(!basicPileInfo[0].equals("empty")){
                int rank = Integer.parseInt(basicPileInfo[0].split(",")[1]);
                String suitStr = basicPileInfo[0].split(",")[0];
                boolean isOpen = basicPileInfo[0].split(",")[2].equals("open");

                Card card = createFreeCard(rank, suitStr);
                if(isOpen) {
                    card.openCard();
                }
                basicPile.addCard(card);
            }
        }
        else {
            for(int i = 0;i<basicPileInfo.length;i++){
                int rank = Integer.parseInt(basicPileInfo[i].split(",")[1]);
                String suitStr = basicPileInfo[i].split(",")[0];
                boolean isOpen = basicPileInfo[i].split(",")[2].equals("open");

                Card card = createFreeCard(rank, suitStr);
                if(isOpen){
                    card.openCard();
                }
                basicPile.addCard(card);

            }
        }
    }

    private static int scalePixels(int px, boolean isVertical)
    {
        if(isVertical)
            return (int) (px * ((metrics.heightPixels) / 768.0));
        return (int) (px * ((metrics.widthPixels) / 1280.0));

    }

}





