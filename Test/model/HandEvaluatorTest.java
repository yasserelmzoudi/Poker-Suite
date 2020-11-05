package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HandEvaluatorTest {

    @Test
    void testIsFlush(){
        HandEvaluator evaluator = new HandEvaluator();
        Card card1 = new Card(8,Suit.CLUBS);
        Card card2 = new Card(9,Suit.CLUBS);
        Card card3 = new Card(2,Suit.CLUBS);
        Card card4 = new Card(4,Suit.CLUBS);
        Card card5 = new Card(10,Suit.CLUBS);
        Hand hand1 = new Hand();
        hand1.add(card1);
        hand1.add(card2);
        hand1.add(card3);
        hand1.add(card4);
        hand1.add(card5);
        hand1 = hand1.sortHand();
        assertTrue(true==evaluator.isFlush(hand1));

        Card card6 = new Card(10,Suit.DIAMONDS);
        Hand hand2 = new Hand();
        hand2.add(card1);
        hand2.add(card2);
        hand2.add(card3);
        hand2.add(card4);
        hand2.add(card6);
        hand2 = hand2.sortHand();
        assertTrue(false==evaluator.isFlush(hand2));
    }


    @Test
    void testIsStraight(){
        HandEvaluator evaluator = new HandEvaluator();
        Card card1 = new Card(8,Suit.CLUBS);
        Card card2 = new Card(9,Suit.CLUBS);
        Card card3 = new Card(11,Suit.CLUBS);
        Card card4 = new Card(7,Suit.CLUBS);
        Card card5 = new Card(10,Suit.CLUBS);
        Hand hand1 = new Hand();
        hand1.add(card1);
        hand1.add(card2);
        hand1.add(card3);
        hand1.add(card4);
        hand1.add(card5);
        hand1 = hand1.sortHand();
        assertTrue(true==evaluator.isStraight(hand1));

        Card card6 = new Card(1,Suit.DIAMONDS);
        Hand hand2 = new Hand();
        hand2.add(card1);
        hand2.add(card2);
        hand2.add(card3);
        hand2.add(card4);
        hand2.add(card6);
        hand2 = hand2.sortHand();
        assertTrue(false==evaluator.isStraight(hand2));

        Card card7 = new Card(14,Suit.CLUBS);
        Card card8 = new Card(13,Suit.CLUBS);
        Card card9 = new Card(12,Suit.CLUBS);
        Card card10 = new Card(11,Suit.CLUBS);
        Card card11 = new Card(10,Suit.CLUBS);
        Hand hand3 = new Hand();
        hand3.add(card7);
        hand3.add(card8);
        hand3.add(card9);
        hand3.add(card10);
        hand3.add(card11);
        hand3 = hand3.sortHand();
        assertTrue(true==evaluator.isStraight(hand3));


        Card card12 = new Card(5,Suit.CLUBS);
        Card card13 = new Card(4,Suit.CLUBS);
        Card card14 = new Card(3,Suit.CLUBS);
        Card card15 = new Card(2,Suit.CLUBS);
        Hand hand4 = new Hand();
        hand4.add(card7);
        hand4.add(card12);
        hand4.add(card13);
        hand4.add(card14);
        hand4.add(card15);
        hand4 = hand4.sortHand();
        assertTrue(true==evaluator.isStraight(hand4));
    }

    @Test
    void testIsStraightFlush(){
        HandEvaluator evaluator = new HandEvaluator();
        Card card7 = new Card(14,Suit.CLUBS);
        Card card8 = new Card(13,Suit.CLUBS);
        Card card9 = new Card(12,Suit.CLUBS);
        Card card10 = new Card(11,Suit.CLUBS);
        Card card11 = new Card(10,Suit.CLUBS);
        Hand hand3 = new Hand();
        hand3.add(card7);
        hand3.add(card8);
        hand3.add(card9);
        hand3.add(card10);
        hand3.add(card11);
        hand3 = hand3.sortHand();
        assertTrue(true==evaluator.isStraightFlush(hand3));
    }

}
