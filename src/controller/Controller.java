package controller;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import view.*;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {

    private static final String ENGLISH = "English";

    private static final String RESOURCES = "Resources/";
    public static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES.replace("/", ".");
    private ResourceBundle projectTextResources;

    private Model model;
    private RoundManager roundManager;
    private PlayerList playerList;
    private final List<GameDisplayRecipient> frontEndPlayers;
    private final CommunityCards communityCards;
    private GameDisplayRecipient displayCommunity;
    private final Pot pot;
    private final Dealer dealer;
    private final GameView view;
    private int roundNumber;
    private int totalRounds;
    private final Map<Player, FrontEndPlayer> playerMappings;
    private final Map<String, FrontEndCard> frontEndCardMappings;
    private final FileReader reader;


    public Controller() {
        reader = new FileReader();
        view = new GameView();
        roundNumber = 1;
        Game game = new Game();
        initializeGameSelect();

        communityCards = game.getCommunityCards();
        pot = game.getPot();
        dealer = game.getDealer();
        roundManager = game.getTurnManager();

        playerMappings = new HashMap<>();
        frontEndCardMappings = new HashMap<>();
        frontEndPlayers = new ArrayList<>();
    }

    public Scene setupScene() {
        return view.setupScene();
    }

    public void initializeGameSelect(){
        EventHandler<ActionEvent> holdemEvent = e -> initializeProperties("Holdem.properties");
        EventHandler<ActionEvent> drawEvent = e -> initializeProperties("FiveCardDraw.properties");
        EventHandler<ActionEvent> studEvent = e -> initializeProperties("SevenCardStud.properties");
        EventHandler<ActionEvent> customEvent = e -> chooseNewFile();
        view.makeGameSelectScreen(holdemEvent, drawEvent, studEvent, customEvent);
    }


    private void chooseNewFile() {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Game Type (*.properties)", "*.properties");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialDirectory(new File("properties/"));
        File file = fileChooser.showOpenDialog(new Stage());
        if(file!=null) {
            initializeProperties((file).getName());
        }
        //else?
    }

    public void initializeProperties(String fileName){
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        System.out.println(fileName);
        Properties modelProperties = reader.getPropertyFile(fileName);
        totalRounds = Integer.parseInt(modelProperties.getProperty("maxRounds"));
        initializePlayerList(fileName);
        initializeFrontEndPlayers();
        initializeCommunity();
        model = new Model(totalRounds, playerList, communityCards, dealer, modelProperties);
        startRound();
    }

    public void startRound(){
        model.dealFlow(roundNumber);
        nextAction(model.getAction(roundNumber));
    }

    private void initializePlayerList(String fileName){
        //TODO: use factory design pattern here to choose what kind of playerList to instantiate
        //TODO: use configuration files to instantiate the players
        try {
            Properties modelProperties = reader.getPropertyFile(fileName);
            String playerListType = modelProperties.getProperty("playerListType");
            Class<?> cl = Class.forName("model." + playerListType + "PlayerList");
            Player player1 = new InteractivePlayer("Arjun", 100, communityCards, pot);
            Player player2 = new InteractivePlayer("Christian", 100, communityCards, pot);
            Player player3 = new InteractivePlayer("Yasser", 100, communityCards, pot);
            playerList = (PlayerList) cl.getConstructor(List.class)
                    .newInstance(new ArrayList<>(List.of(player1,player2, player3)));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initializeFrontEndPlayers(){
        int playerOffset = 30;
        for (Player currentPlayer: playerList.getActivePlayers()){
            FrontEndPlayer newPlayer = new FrontEndPlayer(10, playerOffset, currentPlayer.toString(), currentPlayer.getBankroll());
            playerMappings.put(currentPlayer, newPlayer);
            frontEndPlayers.add(newPlayer);
            playerOffset+=50;
        }
    }

    private void initializeCommunity(){
        displayCommunity = new FrontEndCommunity(200,400);
    }

    //TODO: maintain player that raised last
    public void initializeActionMenu() throws InterruptedException {
        playerList.updateActivePlayers();
        List<Player> players = playerList.getActivePlayers();
        List<Player> playersCopy = new ArrayList<>(players);
        for (Player player : playersCopy) {
            if (!player.isInteractive()) {
                AutoPlayer autoPlayer = (AutoPlayer) player;
                autoPlayer.decideAction();
            }
            else {
                EventHandler<ActionEvent> foldEvent = e -> indicateFold(player);
                EventHandler<ActionEvent> checkEvent = e -> indicateCheck(player);
                EventHandler<ActionEvent> betEvent = e -> displayBetMenu(player);


                ChoiceDialog dialog = view.makeActionScreen(foldEvent, checkEvent, betEvent);
                Optional<Button> result = dialog.showAndWait();
                if (result.isPresent()){
                    if (result.get().getId().equals("Bet")){
                        displayBetMenu(player);
                    }
                    else {
                        try {
                            Class<?> c = Class.forName("controller.Controller");
                            Method method = c.getDeclaredMethod("indicate" + result.get().getId(), Player.class);
                            method.invoke(this, player);
                            //TODO: fix exceptions
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }


            }
            roundManager.checkOnePlayerRemains(playerList);
        }
        playerList.updateActivePlayers();
        roundManager.checkShowDown(playerList, roundNumber, totalRounds + 1);
        if (roundNumber < totalRounds + 1) {
            model.dealFlow(roundNumber);
            System.out.println(roundNumber);
            nextAction(model.getAction(roundNumber));
        }
    }

    private void nextAction(String action){
        try{
            Class<?> c = Class.forName("controller.Controller");
            Method method = c.getDeclaredMethod(action);
            method.invoke(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void exchangeRound() throws InterruptedException {
        playerList.updateActivePlayers();
        for (Player player : playerList.getActivePlayers()) {

            //TODO: have a way to create the number of text field inputs based on the number of exchange cards allowed as specified by user
            TextField exchangeCardInput1 = new TextField();
            TextField exchangeCardInput2 = new TextField();
            TextField exchangeCardInput3 = new TextField();
            Dialog exchangeBox = view.makeExchangeScreen(exchangeCardInput1,exchangeCardInput2, exchangeCardInput3);

            Optional<ButtonType> exchangeBoxResult = exchangeBox.showAndWait();
            if (exchangeBoxResult.isPresent()) {
                List<String> exchangeCards  = new ArrayList<>(List.of(exchangeCardInput1.getText(),exchangeCardInput2.getText(),exchangeCardInput3.getText()));
                List<String> filtered = exchangeCards.stream()
                        .filter(b -> b.equals(""))
                        .collect(Collectors.toList());
                exchangeCards.removeAll(filtered);
                dealer.exchangeCards(player, exchangeCards);
                exchangeFrontEndCards(player, playerMappings.get(player));
            }
        }
        roundNumber++;
        playerList.updateActivePlayers();
        initializeActionMenu();
    }

    //don't like this conditional
    private void dealingRound() throws InterruptedException {
        String recipient = model.getRecipient();
        if (recipient.equals("Community")){
            dealFrontEndCardsInRound(communityCards, displayCommunity);
        }
        else {
            for (Player player : playerList.getActivePlayers()){
                dealFrontEndCardsInRound(player,playerMappings.get(player));
            }
        }
        roundNumber++;
        playerList.updateActivePlayers();
        initializeActionMenu();
    }

    public void dealFrontEndCardsInRound(CardRecipient recipient, GameDisplayRecipient displayRecipient){
        for (Card newCard: recipient.getNewCards()){
            FrontEndCard displayCard = getFrontEndCard(newCard);
            int numberOfFrontEndCards = displayRecipient.getFrontEndCardLocations().size();

            if (numberOfFrontEndCards!= 0){
                FrontEndCard lastCard = displayRecipient.getLastCard();
                int lastCardLocation = displayRecipient.getFrontEndCardLocations().get(lastCard);
                view.deal(displayCard, displayRecipient, lastCardLocation + 80);
            }
            else{
                view.deal(displayCard, displayRecipient, 20);
            }
        }
    }

    public void exchangeFrontEndCards(Player player, GameDisplayRecipient displayRecipient){
//        int dealLocation = 0;
        int cardIndex = 0;
        for (Card discardedCard: player.getDiscardedCards()){
            FrontEndCard discardedFrontEndCard = frontEndCardMappings.get(discardedCard.toString());
            view.remove(discardedFrontEndCard);

            int dealLocation = displayRecipient.getFrontEndCardLocations().get(discardedFrontEndCard);
            Card newCard = player.getNewCards().get(cardIndex);
            FrontEndCard displayCard = getFrontEndCard(newCard);
            view.deal(displayCard, displayRecipient, dealLocation);
            cardIndex ++;
        }
    }


    //should this be in View or Controller?
    private FrontEndCard getFrontEndCard(Card card){
        FrontEndCard frontEndCard = new FrontEndCard(card.getCardSymbol(), card.getCardSuit(), card.isVisible());
        frontEndCardMappings.put(card.toString(), frontEndCard);
        return frontEndCard;
    }

    private void indicateFold(Player player){
        player.exitHand();
        FrontEndPlayer displayPlayer = playerMappings.get(player);
        displayPlayer.foldDisplay();
    }

    private void indicateBet(Player player, String betInput){
        int betAmount = Integer.parseInt(betInput);
        pot.addToPot(betAmount);
        player.updateBankroll(betAmount * -1);

        FrontEndPlayer displayPlayer = playerMappings.get(player);
        displayPlayer.betDisplay(betAmount * -1);
    }
    private void indicateCheck(Player player){
        FrontEndPlayer displayPlayer = playerMappings.get(player);
        displayPlayer.checkDisplay();

    }
    public void displayBetMenu(Player player){

        TextField betInput = new TextField();
        Dialog betBox = view.makeOptionScreen(betInput);
        Optional betBoxResult = betBox.showAndWait();
        if (betBoxResult.isPresent()) {

                indicateBet(player, betInput.getText());}


    }

}

