package model;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> hand;

    //for temporary hand evaluation logic -> won't exist later
    private int handTotal;
    private int handSize;

    public Hand() {
        hand = new ArrayList<>();
        handTotal = 0;
        handSize = 0;
    }

    public void add(Card card) {
        hand.add(card);
        handSize++;
    }
    public void clear(){
        hand.clear();
        handSize = 0;
    }

    public void remove(Card card) {
        hand.remove(card);
        handSize = handSize - 1;
    }

    public List<Card> getCards() {
        return hand;
    }


    public int getHandTotal() {
        for (Card card : hand) {
            handTotal += card.getRank();
        }
        return handTotal;
    }

    public int getHandSize() {
        return handSize;
    }

    public Card get(int index){
        if(index<this.getHandSize()){
            int i =0;
            for (Card card:this.getCards()){
                if(i==index){
                    return card;
                }
                i++;
            }
        }
    return null;
    }

    public Hand sortHand() {
        Hand sortedHand = new Hand();
        while (this.getHandSize() > 0) {
            int max = 0;
            Card maxCard = null;
            for (Card card : this.getCards()) {
                if (card.getRank() > max) {
                    max = card.getRank();
                }
            }
            for (Card card : this.getCards()) {
                if (card.getRank() == max) {
                    maxCard = card;
                }
            }
            sortedHand.add(maxCard);
            this.remove(maxCard);
        }
        return sortedHand;
    }

    public Hand copyHand() {
        Hand copyHand = new Hand();
        for(Card card: this.getCards()){
            copyHand.add(card);
        }
        copyHand = copyHand.sortHand();
        return copyHand;
    }



}


