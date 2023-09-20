package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.Notification;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TransitionAnimation;
import nz.ac.auckland.se206.gpt.ChatMessage;

public class CauldronRoomController extends RoomController {
  @FXML
  private Pane pane;
  @FXML
  private ImageView cauldronImg;
  @FXML
  private Polygon rightArrow;
  @FXML
  private Polygon leftArrow;
  @FXML
  private ImageView bookFireRectangle, bookWaterRectangle, bookAirRectangle;
  @FXML
  private ImageView bookFireImage, bookWaterImage, bookAirImage;
  @FXML
  private Rectangle textRect;
  @FXML
  private ScrollPane calItemScroll;
  @FXML
  private Label riddleSelectLabel;
  @FXML
  private Label chooseLabel;

  @FXML
  private ImageView itemElevenImg;
  @FXML
  private ImageView itemTwelveImg;
  @FXML
  private ImageView itemThirteenImg;
  @FXML
  private ImageView itemFourteenImg;
  @FXML
  private ImageView itemFifteenImg;

  private boolean showRecipe = true;
  private CountdownTimer countdownTimer;

  public void initialize() {
    // genericInitialise("Cauldron", itemElevenImg, itemTwelveImg, itemThirteenImg, itemFourteenImg, itemFifteenImg, leftArrow);
    // Setting up the countdown and appropriate booleans before
    // anything happens to change them within the game
    bagOpened = false;
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setCauldronTimerLabel(timerLabel);

    GameState.isBookRiddleGiven = false;
    GameState.isBookRiddleResolved = false;
    mouseTrackRegion.setDisable(true);
    textRect.setDisable(true);
    toggleChat(true, 0);
    toggleBooks(true, 0);
    mouseTrackRegion.setOpacity(0);

    // Setting up the appropriate interactions for the cauldron, wizard, arrows, book button, bag button, book fire, book water, book air
    btnMouseActions(cauldronImg);
    btnMouseActions(wizardImg);
    arrowMouseActions(leftArrow);
    arrowMouseActions(rightArrow);
    btnMouseActions(bookBtn);
    btnMouseActions(bagBtn);
    btnMouseActions(bookFireRectangle);
    btnMouseActions(bookWaterRectangle);
    btnMouseActions(bookAirRectangle);
  }

  private void toggleBooks(boolean disable, int opacity) {
    bookFireRectangle.setDisable(disable);
    bookWaterRectangle.setDisable(disable);
    bookAirRectangle.setDisable(disable);
    bookFireRectangle.setOpacity(opacity);
    bookWaterRectangle.setOpacity(opacity);
    bookAirRectangle.setOpacity(opacity);
  }

  /**
   * Taking user to the cauldron scene from the room scene to be able to 
   * brew their potions.
   * 
   * @param event
   */
  @FXML
  public void clickCauldron(MouseEvent event) {
    CauldronController cauldronController = SceneManager.getCauldronControllerInstance();
    if (cauldronController != null) {
      cauldronController.updateImageStates();
    }

    // If the cauldronController exists, then switching scenes
    System.out.println("cauldron clicked");
    calItemScroll.setOpacity(0);
    bagOpened = false;
    System.out.println("CAULDRON_ROOM -> CAULDRON");
    TransitionAnimation.changeScene(pane, AppUi.CAULDRON, false);
    SceneManager.setTimerScene(AppUi.CAULDRON);
    System.out.println(Items.necessary);
  }
  
  /**
   * Talking the user to the wizard's chat functionality.
   * Also, if the user hasn't been prompted with the riddle yet,
   * showing it to them too.
   * 
   * @param event
   * @throws InterruptedException
   */
  @FXML
  public void clickWizard(MouseEvent event) {
    System.out.println("wizard clicked");
    if (!GameState.isBookRiddleGiven) {
      toggleChat(false, 1);
      disableChat(true, 0.5);
      MainMenuController.getChatHandler().appendChatMessage(MainMenuController.getRiddle(), chatTextArea, inputText, sendButton);

      // After the riddle scrolling text animation has finished, then allowing
      // the user to select the book and respond to the wizard
      MainMenuController.getChatHandler().getAppendTask().setOnSucceeded(e -> {
        chooseLabel.setOpacity(1);
        disableChat(false, 1);
        toggleBooks(false, 1);
      });
      GameState.isBookRiddleGiven = true;
    } else {
      toggleChat(false, 1);
    }
  }

  @FXML
  public void clickBookFire(MouseEvent event) {
    handleClickBooks("fire", bookFireImage, bookFireRectangle);
  }

  @FXML
  public void clickBookWater(MouseEvent event) {
    handleClickBooks("water", bookWaterImage, bookWaterRectangle);
  }

  @FXML
  public void clickBookAir(MouseEvent event) {
    handleClickBooks("air", bookAirImage, bookAirRectangle);
  }

  private void handleClickBooks(String element, ImageView bookImage, ImageView bookRectangle) {
    System.out.println("book " + element +  " clicked");
    if (MainMenuController.getBook() == element) {
      // remove the book from the scene
      FadeTransition ft = new FadeTransition(Duration.seconds(1), bookRectangle);
      ft.setFromValue(1);
      ft.setToValue(0);
      ft.play();

      bookImage.setOpacity(0);
      bookImage.setDisable(true);
      bookRectangle.setDisable(true);

      GameState.isBookRiddleResolved = true;
      chooseLabel.setOpacity(0);

      disableChat(true, 0.5);
      // Code to send appropriate riddle resolved message to GPT
      Task<Void> resolvedTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          ChatMessage msg = new ChatMessage(
            "Wizard", MainMenuController.getChatHandler().runGpt(
              MainMenuController.getResolvedMessage()));
          MainMenuController.getChatHandler().appendChatMessage(msg, chatTextArea, inputText, sendButton);
          return null;
        }
      };
      new Thread(resolvedTask).start();
    }
  }

  @FXML
  public void goLeft(MouseEvent event) {
    System.out.println("CAULDRON_ROOM -> LIBRARY_ROOM");
    calItemScroll.setOpacity(0);
    RoomController.goDirection(pane, AppUi.LIBRARY_ROOM);
  }

  @FXML
  public void goRight(MouseEvent event) {
    System.out.println("CAULDRON_ROOM -> TREASURE_ROOM");
    calItemScroll.setOpacity(0);
    RoomController.goDirection(pane, AppUi.TREASURE_ROOM);
  }

  /**
   * Handling events where menus or views need to be exited by clicking 
   * anywhere else on the screen
   *
   * @param event
   */
  @FXML
  public void clickOff(MouseEvent event) {
    if (GameState.isBookRiddleResolved) {
      System.out.println("click off");
      chooseLabel.setOpacity(0);
      enableRecipe();
      toggleChat(true, 0);
      toggleBooks(true, 0);

      // Handling closing the "bag" when clicking off inventory
      if (bagOpened) {
        calItemScroll.setOpacity(0);
        bagOpened = false;
        System.out.println("Bag closed");
      }
    }
  }

  @FXML
  private void enableRecipe() {
    bookBtn.setDisable(false);
    bookBtn.setOpacity(1);

    if (showRecipe) {
      notificationText.setText("Check bottom right for the recipe book!");
      Notification.notifyPopup(notificationBack, notificationText);
    }

    showRecipe = false;
  }

  /**
   * Taking the user to the book scene from the room scene.
   */
  @FXML
  public void openBook() {
    System.out.println("CAULDRON_ROOM -> BOOK");
    RoomController.openBook(AppUi.CAULDRON_ROOM, pane);
  }
}
