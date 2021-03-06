package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public abstract class PlayerHUDGameObject extends VBox {

  private final String displayedGameStat;
  private String displayedGameImage;

  private Text gameStat;
  private ImageView gameImage;

  public static final int IMAGE_WIDTH = 35;
  public static final int IMAGE_HEIGHT = 50;

  public PlayerHUDGameObject(String displayedGameStat, String displayedGameImage) {
    super();
    this.displayedGameStat = displayedGameStat;
    this.displayedGameImage = displayedGameImage;

    setUp();
  }

  private void setUp() {
    gameStat = new Text(displayedGameStat);
    gameStat.getStyleClass().add("playerHUDObject");

    gameImage = new ImageView();
    gameImage.setFitHeight(IMAGE_HEIGHT);
    gameImage.setFitWidth(IMAGE_WIDTH);
    StackPane pane = new StackPane();
    pane.getChildren().addAll(gameImage, gameStat);

    this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    this.getChildren().add(pane);

    setImage(displayedGameImage);
  }

  public Text getGameStat() {
    return gameStat;
  }

  public void setImage(String newImage) {
    try {
      gameImage.setImage(new Image(getClass().getResource(newImage).toExternalForm()));
      displayedGameImage = newImage;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ImageView getGameImage() {
    return gameImage;
  }
}
