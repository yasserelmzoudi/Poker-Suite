package model;

import java.util.List;

public class CommunityCards extends CardRecipient {

  public CommunityCards() {
    super();
  }

  void receiveCard(Card card) {
    cardsList.add(card);
    addNewCards(card);
  }

  public List<Card> getCommunityCardsList() {
    return cardsList;
  }

}
