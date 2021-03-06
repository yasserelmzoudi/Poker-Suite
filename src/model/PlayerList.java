package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PlayerList {

  protected List<Player> activePlayers;
  protected List<Player> allPlayers;
  private List<Player> removedPlayers;
  private Player playerUp;
  private int lastBet;
  private boolean raiseMade;
  private Player raiseSeat;

  public PlayerList(List<Player> players) {
    this.allPlayers = new ArrayList<>(players);
    this.activePlayers = players;
    removedPlayers = new ArrayList<>();
    lastBet = 0;
    raiseMade = false;
  }

  protected void removeFoldedPlayers() {
    List<Player> filtered = activePlayers.stream()
        .filter(b -> !b.isActive())
        .collect(Collectors.toList());
    removedPlayers.addAll(filtered);
    activePlayers.removeAll(filtered);
  }

  public List<Player> getAllPlayers() {
    return allPlayers;
  }

  public boolean doesOneSolventPlayerRemain() {
    int SolventPlayerCount = 0;
    for (Player player : this.getActivePlayers()) {
      if (player.isSolvent()) {
        SolventPlayerCount++;
      }
    }
    return SolventPlayerCount == 1;
  }

  public void updateStartingRoundOrder() {
    Collections.rotate(allPlayers, -1);
  }

  public abstract void updateActivePlayers();

  public abstract void initializeActivePlayers();

  public void resetActivePlayers() {
    activePlayers = new ArrayList<>(allPlayers);
    for (Player player : getActivePlayers()) {
      player.clearBetAmount();
    }
  }

  public boolean raiseMade(Player player) {
    playerUp = player;
    if (playerUp.getTotalBetAmount() > lastBet) {
      raiseMade = true;
      lastBet = playerUp.getTotalBetAmount();
      raiseSeat = player;
    }
    return raiseMade;
  }

  public void raiseShift() {
    if (raiseMade) {
      int shiftIndex = activePlayers.indexOf(playerUp) + 1;
      Collections.rotate(activePlayers, -shiftIndex);
    }
    raiseMade = false;
  }

  public int getLastBet() {
    return lastBet;
  }

  public Player getRaiseSeat() {
    return raiseSeat;
  }

  public void resetRaiseStats() {
    lastBet = 0;
    raiseSeat = null;
  }

  public List<Player> getActivePlayers() {
    return activePlayers;
  }
}
