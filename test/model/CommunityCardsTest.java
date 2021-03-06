package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import controller.JSONReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommunityCardsTest {

  private Deck deck;
  private JSONReader reader;

  @BeforeEach
  void setUp() {
    reader = new JSONReader();
    reader.parse("/cardSettings.json");
    List<String> suitNames = new ArrayList<>();
    List<Integer> rankValues = new ArrayList<>();
    rankValues.addAll(reader.getRanks().keySet());
    suitNames.addAll(reader.getSuits().keySet());
    deck = new Deck(suitNames, rankValues);
  }

  @Test
  public void testAddToCommunity() {
    CommunityCards communityCards = new CommunityCards();
    Card card1 = deck.getTopCard();
    communityCards.receiveCard(card1);
    assertEquals(card1, communityCards.getCommunityCardsList().get(0));
  }
}
