package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeckTest {

    @Test
    void testNewDeck() {
        Deck deck = new Deck();
        Card card1 = new Card(11,Suit.CLUBS);
        Card card2 = new Card(4,Suit.DIAMONDS);
        Card card3 = new Card(2,Suit.SPADES);
        deck.replaceTopCard(card1);
        deck.replaceTopCard(card2);
        deck.replaceTopCard(card3);
        assertEquals(2, deck.getTopCard().getRank());
        assertEquals(Suit.DIAMONDS, deck.getTopCard().getCardSuit());
        assertEquals("J", deck.getTopCard().getCardSymbol());
    }

    @Test
    public void testGetTopCard(){
        Deck deck = new Deck();
        assertEquals(14, deck.getTopCard().getRank());
        assertEquals(Suit.DIAMONDS, deck.getTopCard().getCardSuit());
        assertEquals("Q", deck.getTopCard().getCardSymbol());
    }

    @Test
    public void testReplaceTopCard(){
        Deck deck = new Deck();
        assertEquals(14, deck.getTopCard().getRank());
        assertEquals(Suit.DIAMONDS, deck.getTopCard().getCardSuit());
        assertEquals("Q", deck.getTopCard().getCardSymbol());
        Card card1 = new Card(11,Suit.CLUBS);
        Card card2 = new Card(4,Suit.DIAMONDS);
        Card card3 = new Card(2,Suit.SPADES);
        deck.replaceTopCard(card1);
        deck.replaceTopCard(card2);
        deck.replaceTopCard(card3);
        assertEquals(2, deck.getTopCard().getRank());
        assertEquals(Suit.DIAMONDS, deck.getTopCard().getCardSuit());
        assertEquals("J", deck.getTopCard().getCardSymbol());
    }

    @Test
    public void testIsEmpty(){
        Deck deck = new Deck();
        assertFalse(deck.isEmpty());
    }
}