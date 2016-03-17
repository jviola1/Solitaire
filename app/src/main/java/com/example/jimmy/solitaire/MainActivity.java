package com.example.jimmy.solitaire;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int width = 93;
    int height = 143;
    int marginX = 150;
    int marginY = 300;

    Rect screen = new Rect(10,10,1200,800);

    Pile pile1, pile2, pile3, pile4, pile5, pile6, pile7;

    Pile[] pileList = new Pile[7];

//    int numberOfCards = 8;
//    Card[] cardList = new Card[numberOfCards];

    ArrayList<Card> tempList = new ArrayList<Card>();

    int lastIndexOfPile = -1;


    //view cards in Pile ***** not necessary *****

//    public String showCardsInPile(Pile pile){
//        String log="";
//        for(int i = 0;i<pile.cardList.size();i++){
//            switch(pile.cardList.get(i).suit) {
//                case Spade:
//                    log += ", " + "Spade" + String.valueOf(pile.cardList.get(i).rank);
//                    break;
//                case Diamond:
//                    log += ", " + "Diamond" + String.valueOf(pile.cardList.get(i).rank);
//                    break;
//                case Club:
//                    log += ", " + "Club" + String.valueOf(pile.cardList.get(i).rank);
//                    break;
//                case Heart:
//                    log += ", " + "Heart" + String.valueOf(pile.cardList.get(i).rank);
//                    break;
//            }
//        }
//
//        return log;
//    }


    // *****  not necessary,for test   *****




    public boolean onTouchEvent(MotionEvent e){

        int action = MotionEventCompat.getActionMasked(e);

        switch(action){
            case MotionEvent.ACTION_DOWN:

//                Log.d("Message:","templist:"+String.valueOf(tempList.size()));
//                Log.d("Message:","Pile1:"+showCardsInPile(pile1));
//                Log.d("Message:","Pile2:"+showCardsInPile(pile2));
//                Log.d("Message:","Pile3:"+showCardsInPile(pile3));
//                Log.d("Message:","Pile4:"+showCardsInPile(pile4));
//                Log.d("Message:","Pile5:"+showCardsInPile(pile5));
//                Log.d("Message:","Pile6:"+showCardsInPile(pile6));
//                Log.d("Message:","Pile7:"+showCardsInPile(pile7));


                for(int i = 0;i<pileList.length;i++){

                    if(pileList[i].area.contains((int)e.getRawX(),(int)e.getRawY()-160)){

                        lastIndexOfPile = i;
                        pileList[i].addCardsSelectedToList(tempList, (int) e.getRawX(), (int) e.getRawY() - 160);
                        if(tempList.size()!=0) {
                            pileList[i].removeCardsFromCardToLast(tempList.get(0));
                        }

                        break;
                    }

                }

                if(tempList.size()!=0) {
                    for (int i = 0; i < tempList.size();i++) {
                        tempList.get(i).setLocation((int) e.getRawX()-50,(int) e.getRawY() - 160-50+40*i);
                    }
                }

                return true;

            case MotionEvent.ACTION_MOVE:

                if(screen.contains((int)e.getRawX(),(int)e.getRawY()-160)) {
                    if (tempList.size() != 0) {
                        for (int i = 0; i < tempList.size(); i++) {
                            tempList.get(i).setLocation((int) e.getRawX() - 50, (int) e.getRawY() - 160 - 50 + 40 * i);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                tempList.get(i).img.setElevation(100 + i);
                            }
                        }
                    }

                }
                else{
                    if(tempList.size()!=0){
                        pileList[lastIndexOfPile].addCardFromList(tempList);
                        tempList.clear();
                    }
                }

                return true;

            case  MotionEvent.ACTION_UP:

                if(screen.contains((int)e.getRawY(),(int)e.getRawY())) {

                    if (tempList.size() != 0) {
                        for (int i = 0; i < pileList.length; i++) {
                            if (pileList[i].area.contains((int) e.getRawX(), (int) e.getRawY() - 160)) {

                                if (pileList[i].addCardFromList(tempList)) {
                                    tempList.clear();
                                    pileList[lastIndexOfPile].openLastCard();
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                        if (tempList.size() != 0) {
                            pileList[lastIndexOfPile].addOriginalCardFromList(tempList);
                            tempList.clear();
                        }
                    }

                }
                else {
                    if(tempList.size()!=0){
                        pileList[lastIndexOfPile].addOriginalCardFromList(tempList);
                        tempList.clear();
                    }
                }

                return true;


            default:
                return super.onTouchEvent(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final ImageView pileImg1 = (ImageView)findViewById(R.id.pile1);
        pileImg1.getLayoutParams().width = width;
        pileImg1.getLayoutParams().height = height;
        final ImageView pileImg2 = (ImageView)findViewById(R.id.pile2);
        pileImg2.getLayoutParams().width = width;
        pileImg2.getLayoutParams().height = height;
        final ImageView pileImg3 = (ImageView)findViewById(R.id.pile3);
        pileImg3.getLayoutParams().width = width;
        pileImg3.getLayoutParams().height = height;
        final ImageView pileImg4 = (ImageView)findViewById(R.id.pile4);
        pileImg4.getLayoutParams().width = width;
        pileImg4.getLayoutParams().height = height;
        final ImageView pileImg5 = (ImageView)findViewById(R.id.pile5);
        pileImg5.getLayoutParams().width = width;
        pileImg5.getLayoutParams().height = height;
        final ImageView pileImg6 = (ImageView)findViewById(R.id.pile6);
        pileImg6.getLayoutParams().width = width;
        pileImg6.getLayoutParams().height = height;
        final ImageView pileImg7 = (ImageView)findViewById(R.id.pile7);
        pileImg7.getLayoutParams().width = width;
        pileImg7.getLayoutParams().height = height;

//        final ImageView stockImg = (ImageView)findViewById(R.id.stock);
//        final ImageView wasteImg = (ImageView)findViewById(R.id.waste);

        final ImageView cardImg1 = (ImageView)findViewById(R.id.card1);
        cardImg1.getLayoutParams().width = width;
        cardImg1.getLayoutParams().height = height;
        final ImageView cardImg2 = (ImageView)findViewById(R.id.card2);
        cardImg2.getLayoutParams().width = width;
        cardImg2.getLayoutParams().height = height;
        final ImageView cardImg3 = (ImageView)findViewById(R.id.card3);
        cardImg3.getLayoutParams().width = width;
        cardImg3.getLayoutParams().height = height;
        final ImageView cardImg4 = (ImageView)findViewById(R.id.card4);
        cardImg4.getLayoutParams().width = width;
        cardImg4.getLayoutParams().height = height;
        final ImageView cardImg5 = (ImageView)findViewById(R.id.card5);
        cardImg5.getLayoutParams().width = width;
        cardImg5.getLayoutParams().height = height;
        final ImageView cardImg6 = (ImageView)findViewById(R.id.card6);
        cardImg6.getLayoutParams().width = width;
        cardImg6.getLayoutParams().height = height;
        final ImageView cardImg7 = (ImageView)findViewById(R.id.card7);
        cardImg7.getLayoutParams().width = width;
        cardImg7.getLayoutParams().height = height;
        final ImageView cardImg8 = (ImageView)findViewById(R.id.card8);
        cardImg8.getLayoutParams().width = width;
        cardImg8.getLayoutParams().height = height;
        final ImageView cardImg9 = (ImageView)findViewById(R.id.card9);
        cardImg9.getLayoutParams().width = width;
        cardImg9.getLayoutParams().height = height;
        final ImageView cardImg10 = (ImageView)findViewById(R.id.card10);
        cardImg10.getLayoutParams().width = width;
        cardImg10.getLayoutParams().height = height;
        final ImageView cardImg11 = (ImageView)findViewById(R.id.card11);
        cardImg11.getLayoutParams().width = width;
        cardImg11.getLayoutParams().height = height;
        final ImageView cardImg12 = (ImageView)findViewById(R.id.card12);
        cardImg12.getLayoutParams().width = width;
        cardImg12.getLayoutParams().height = height;
        final ImageView cardImg13 = (ImageView)findViewById(R.id.card13);
        cardImg13.getLayoutParams().width = width;
        cardImg13.getLayoutParams().height = height;
        final ImageView cardImg14 = (ImageView)findViewById(R.id.card14);
        cardImg14.getLayoutParams().width = width;
        cardImg14.getLayoutParams().height = height;
        final ImageView cardImg15 = (ImageView)findViewById(R.id.card15);
        cardImg15.getLayoutParams().width = width;
        cardImg15.getLayoutParams().height = height;
        final ImageView cardImg16 = (ImageView)findViewById(R.id.card16);
        cardImg16.getLayoutParams().width = width;
        cardImg16.getLayoutParams().height = height;
//        final ImageView cardImg17 = (ImageView)findViewById(R.id.card17);
//        cardImg17.getLayoutParams().width = width;
//        cardImg17.getLayoutParams().height = height;
        final ImageView cardImg18 = (ImageView)findViewById(R.id.card18);
        cardImg18.getLayoutParams().width = width;
        cardImg18.getLayoutParams().height = height;
        final ImageView cardImg19 = (ImageView)findViewById(R.id.card19);
        cardImg19.getLayoutParams().width = width;
        cardImg19.getLayoutParams().height = height;



        ViewTreeObserver vtoP1 = pileImg1.getViewTreeObserver();
        vtoP1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pileImg1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                pile1 = new Pile(new Point((int)pileImg1.getX(), (int)pileImg1.getY()));

                pileList[0] = pile1;
            }
        });

        ViewTreeObserver vtoP2 = pileImg2.getViewTreeObserver();
        vtoP2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pileImg2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                pileImg2.setX(pile1.location.x+marginX);
                pileImg2.setY(pile1.location.y);
                pile2 = new Pile(new Point((int)pileImg2.getX(), (int)pileImg2.getY()));

                pileList[1] = pile2;
            }
        });

        ViewTreeObserver vtoP3 = pileImg3.getViewTreeObserver();
        vtoP3.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pileImg3.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                pileImg3.setX(pile2.location.x+marginX);
                pileImg3.setY(pile2.location.y);
                pile3 = new Pile(new Point((int)pileImg3.getX(), (int)pileImg3.getY()));

                pileList[2] = pile3;
            }
        });

        ViewTreeObserver vtoP4 = pileImg4.getViewTreeObserver();
        vtoP4.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pileImg4.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                pileImg4.setX(pile3.location.x+marginX);
                pileImg4.setY(pile3.location.y);
                pile4 = new Pile(new Point((int)pileImg4.getX(), (int)pileImg4.getY()));

                pileList[3] = pile4;
            }
        });

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            ViewTreeObserver vtoP5 = pileImg5.getViewTreeObserver();
            vtoP5.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    pileImg5.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    pileImg5.setX(pile4.location.x + marginX);
                    pileImg5.setY(pile4.location.y);
                    pile5 = new Pile(new Point((int)pileImg5.getX(), (int)pileImg5.getY()));

                    pileList[4] = pile5;
                }
            });

            ViewTreeObserver vtoP6 = pileImg6.getViewTreeObserver();
            vtoP6.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    pileImg6.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    pileImg6.setX(pile5.location.x+marginX);
                    pileImg6.setY(pile5.location.y);
                    pile6 = new Pile(new Point((int)pileImg6.getX(), (int)pileImg6.getY()));

                    pileList[5] = pile6;
                }
            });

            ViewTreeObserver vtoP7 = pileImg7.getViewTreeObserver();
            vtoP7.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    pileImg7.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    pileImg7.setX(pile6.location.x+marginX);
                    pileImg7.setY(pile6.location.y);
                    pile7 = new Pile(new Point((int)pileImg7.getX(), (int)pileImg7.getY()));

                    pileList[6] = pile7;
                }
            });
        }
        else {
            ViewTreeObserver vtoP5 = pileImg5.getViewTreeObserver();
            vtoP5.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    pileImg5.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    pileImg5.setX(pile1.location.x);
                    pileImg5.setY(pile1.location.y+marginY);
                    pile5 = new Pile(new Point((int)pileImg5.getX(), (int)pileImg5.getY()));

                    pileList[4] = pile5;
                }
            });

            ViewTreeObserver vtoP6 = pileImg6.getViewTreeObserver();
            vtoP6.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    pileImg6.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    pileImg6.setX(pile2.location.x);
                    pileImg6.setY(pile2.location.y+marginY);
                    pile6 = new Pile(new Point((int)pileImg6.getX(), (int)pileImg6.getY()));

                    pileList[5] = pile6;
                }
            });

            ViewTreeObserver vtoP7 = pileImg7.getViewTreeObserver();
            vtoP7.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    pileImg7.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    pileImg7.setX(pile3.location.x);
                    pileImg7.setY(pile3.location.y+marginY);
                    pile7 = new Pile(new Point((int)pileImg7.getX(), (int)pileImg7.getY()));

                    pileList[6] = pile7;
                }
            });
        }


        ViewTreeObserver vtoC1 = cardImg1.getViewTreeObserver();
        vtoC1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card1 = new Card(pile1,cardImg1,1, Card.Suit.Club, R.drawable.club1);
            }
        });


        ViewTreeObserver vtoC2 = cardImg2.getViewTreeObserver();
        vtoC2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card2 = new Card(pile4,cardImg2,2, Card.Suit.Diamond, R.drawable.diamond2);
            }
        });

        ViewTreeObserver vtoC3 = cardImg3.getViewTreeObserver();
        vtoC3.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg3.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card3 = new Card(pile7,cardImg3,3, Card.Suit.Heart, R.drawable.heart3);
            }
        });

        ViewTreeObserver vtoC4 = cardImg4.getViewTreeObserver();
        vtoC4.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg4.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card4 = new Card(pile4,cardImg4,4, Card.Suit.Spade, R.drawable.spade4);
            }
        });

        ViewTreeObserver vtoC5 = cardImg5.getViewTreeObserver();
        vtoC5.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg5.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card5 = new Card(pile7,cardImg5,5, Card.Suit.Heart, R.drawable.heart5);
            }
        });

        ViewTreeObserver vtoC6 = cardImg6.getViewTreeObserver();
        vtoC6.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg6.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card6 = new Card(pile1,cardImg6,6, Card.Suit.Diamond, R.drawable.diamond6);
            }
        });

        ViewTreeObserver vtoC7 = cardImg7.getViewTreeObserver();
        vtoC7.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg7.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card7 = new Card(pile7,cardImg7,7, Card.Suit.Club, R.drawable.club7);
            }
        });

        ViewTreeObserver vtoC8 = cardImg8.getViewTreeObserver();
        vtoC8.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg8.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card8 = new Card(pile4,cardImg8,8, Card.Suit.Diamond, R.drawable.diamond8);
            }
        });

        ViewTreeObserver vtoC9 = cardImg9.getViewTreeObserver();
        vtoC9.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg9.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card9 = new Card(pile4,cardImg9,9, Card.Suit.Heart, R.drawable.heart9);
            }
        });

        ViewTreeObserver vtoC10 = cardImg10.getViewTreeObserver();
        vtoC10.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg10.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card10 = new Card(pile1,cardImg10,10, Card.Suit.Spade, R.drawable.spade10);
            }
        });

        ViewTreeObserver vtoC11 = cardImg11.getViewTreeObserver();
        vtoC11.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg11.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card11 = new Card(pile7,cardImg11,11, Card.Suit.Heart, R.drawable.heart11);
            }
        });

        ViewTreeObserver vtoC12 = cardImg12.getViewTreeObserver();
        vtoC12.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg12.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card12 = new Card(pile1,cardImg12,12, Card.Suit.Diamond, R.drawable.diamond12);
            }
        });

        ViewTreeObserver vtoC13 = cardImg13.getViewTreeObserver();
        vtoC13.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg13.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card13 = new Card(pile4,cardImg13,13, Card.Suit.Club, R.drawable.club13);
            }
        });


        ViewTreeObserver vtoC14 = cardImg14.getViewTreeObserver();
        vtoC14.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg14.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card14 = new Card(pile1,cardImg14,2, Card.Suit.Spade, R.drawable.spade2);
            }
        });

        ViewTreeObserver vtoC15 = cardImg15.getViewTreeObserver();
        vtoC15.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg15.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card15 = new Card(pile4,cardImg15,3, Card.Suit.Club, R.drawable.club3);
            }
        });

        ViewTreeObserver vtoC16 = cardImg16.getViewTreeObserver();
        vtoC16.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg16.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card16 = new Card(pile7,cardImg16,5, Card.Suit.Club, R.drawable.club5);
                card16.openCard();
            }
        });

//        ViewTreeObserver vtoC17 = cardImg17.getViewTreeObserver();
//        vtoC17.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                cardImg17.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                Card card17 = new Card(pile1,cardImg17,8, Card.Suit.Spade, R.drawable.spade8);
//            }
//        });

        ViewTreeObserver vtoC18 = cardImg18.getViewTreeObserver();
        vtoC18.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg18.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card18 = new Card(pile1,cardImg18,6, Card.Suit.Club, R.drawable.club6);
                card18.openCard();
            }
        });

        ViewTreeObserver vtoC19 = cardImg19.getViewTreeObserver();
        vtoC19.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardImg19.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Card card19 = new Card(pile4,cardImg19,12, Card.Suit.Spade, R.drawable.spade12);
                card19.openCard();
            }
        });


    }
}
