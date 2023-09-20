package nz.ac.auckland.se206.controllers;

import java.util.Iterator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.ShapeInteractionHandler;
import nz.ac.auckland.se206.TransitionAnimation;
import nz.ac.auckland.se206.Items.Item;
import nz.ac.auckland.se206.Notification;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public abstract class RoomController {
  protected static boolean bagOpened;
  protected static boolean readyToAdd;
  protected Items.Item item;

  @FXML
  protected ImageView wizardChatImage;
  @FXML
  protected ImageView wizardImg;
  @FXML
  protected Rectangle mouseTrackRegion;
  @FXML
  protected Label textLbl;
  @FXML
  protected Rectangle textRect;
  @FXML
  protected Label yesLbl;
  @FXML
  protected Label noLbl;
  @FXML
  protected Label dashLbl;
  @FXML
  protected ScrollPane itemScroll;
  @FXML
  protected ImageView bookBtn;
  @FXML
  protected ImageView bagBtn;
  @FXML
  protected Label timerLabel;
  @FXML
  protected static ShapeInteractionHandler interactionHandler;

  @FXML 
  protected TextArea chatTextArea;
  @FXML 
  protected TextField inputText;
  @FXML 
  protected Button sendButton;
  @FXML 
  protected ImageView ttsBtn2;

  @FXML
  protected ImageView notificationBack;
  @FXML
  protected Label notificationText;

  protected CountdownTimer countdownTimer;

  protected ImageView itemOneImg;
  protected ImageView itemTwoImg;
  protected ImageView itemThreeImg;
  protected ImageView itemFourImg;
  protected ImageView itemFiveImg;

  // Booleans to keep track of whether an item has been added to the inventory
  private boolean oneAdded;
  private boolean twoAdded;
  private boolean threeAdded;
  private boolean fourAdded;
  private boolean fiveAdded;
  // Booleans to keep track of if an item is clicked or selected
  private boolean oneClicked;
  private boolean twoClicked;
  private boolean threeClicked;
  private boolean fourClicked;
  private boolean fiveClicked;

  private Item itemOne;
  private Item itemTwo;
  private Item itemThree;
  private Item itemFour;
  private Item itemFive;

  private Image one;
  private Image two;
  private Image three;
  private Image four;
  private Image five;

  private ImageView image;
  private double ratio;

  /**
   * Initialising the fields that are common in both of the item
   * rooms to avoid code duplication.
   */
  @FXML
  protected void genericInitialise(String roomName, ImageView itemOneImg, ImageView itemTwoImg, ImageView itemThreeImg,
      ImageView itemFourImg, ImageView itemFiveImg, Polygon arrowShpe) {
    this.itemOneImg = itemOneImg;
    this.itemTwoImg = itemTwoImg;
    this.itemThreeImg = itemThreeImg;
    this.itemFourImg = itemFourImg;
    this.itemFiveImg = itemFiveImg;

    // Setting appropriate boolean fields
    oneAdded = false;
    twoAdded = false;
    threeAdded = false;
    fourAdded = false;
    fiveAdded = false;

    oneClicked = false;
    twoClicked = false;
    threeClicked = false;
    fourClicked = false;
    fiveClicked = false;

    if (roomName.equals("Library")) {
      itemOne = Items.Item.TAIL;
      itemTwo = Items.Item.INSECT_WINGS;
      itemThree = Items.Item.FLOWER;
      itemFour = Items.Item.SCALES;
      itemFive = Items.Item.POWDER;
    } else if (roomName.equals("Treasure")) {
      itemOne = Items.Item.TALON;
      itemTwo = Items.Item.CRYSTAL;
      itemThree = Items.Item.BAT_WINGS;
      itemFour = Items.Item.WREATH;
      itemFive = Items.Item.FEATHER;
    }

    readyToAdd = false;
    bagOpened = false;

    interactionHandler = new ShapeInteractionHandler();

    // Disabling the text box and mouse track region
    setText("", false, false);
    mouseTrackRegion.setDisable(true);
    mouseTrackRegion.setOpacity(0);

    // Setting appropriate interactable features for the buttons
    btnMouseActions(bookBtn);
    btnMouseActions(bagBtn);
    btnMouseActions(wizardImg);

    // Setting appropriate interactable features for the items
    itemMouseActions(itemOneImg, oneClicked, itemOne);
    itemMouseActions(itemTwoImg, twoClicked, itemTwo);
    itemMouseActions(itemThreeImg, threeClicked, itemThree);
    itemMouseActions(itemFourImg, fourClicked, itemFour);
    itemMouseActions(itemFiveImg, fiveClicked, itemFive);

    // Setting appropriate interactable features for the arrow
    arrowMouseActions(arrowShpe);
  }

  /**
   * Handling the event where a button is hovered over
   */
  protected static void btnMouseActions(ImageView btn) {
    btn.setOnMouseEntered(event -> interactionHandler.glowThis(btn));
    btn.setOnMouseExited(event -> interactionHandler.unglowThis(btn));
  }

  /**
   * Handling the event where an item is hovered over and clicked
   */
  protected void itemMouseActions(ImageView itemImg, boolean itemClicked, Items.Item item) {
    itemImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemImg));
    itemImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemImg, itemClicked));
    itemImg.setOnMouseClicked(event -> itemSelect(item));
  }

  protected static void arrowMouseActions(Polygon arrowShpe) {
    arrowShpe.setOnMouseEntered(event -> arrowShpe.setOpacity(0.9));
    arrowShpe.setOnMouseExited(event -> arrowShpe.setOpacity(0.6));
  }

  /**
   * Selecting the item and prompting user and prompting user to either add or not
   * add the item to their inventory. Does nothing if the item has already been 
   * added to the inventory.
   *
   * @param item the item clicked by user
   */
  @FXML
  public void itemSelect(Items.Item item) {
    // Items are are clicked, so their glow remains on until item is added or not
    // added
    switch (item) {
      case TAIL:
        if (oneAdded) {
          return;
        }
        interactionHandler.glowThis(itemOneImg);
        oneClicked = true;
        break;
      case INSECT_WINGS:
        if (twoAdded) {
          return;
        }
        interactionHandler.glowThis(itemTwoImg);
        twoClicked = true;
        break;
      case FLOWER:
        if (threeAdded) {
          return;
        }
        interactionHandler.glowThis(itemThreeImg);
        threeClicked = true;
        break;
      case SCALES:
        if (fourAdded) {
          return;
        }
        interactionHandler.glowThis(itemFourImg);
        fourClicked = true;
        break;
      case POWDER:
        if (fiveAdded) {
          return;
        }
        interactionHandler.glowThis(itemFiveImg);
        fiveClicked = true;
        break;
      case TALON:
        if (oneAdded) {
          return;
        }
        interactionHandler.glowThis(itemOneImg);
        oneClicked = true;
        break;
      case CRYSTAL:
        if (twoAdded) {
          return;
        }
        interactionHandler.glowThis(itemTwoImg);
        twoClicked = true;
        break;
      case BAT_WINGS:
        if (threeAdded) {
          return;
        }
        interactionHandler.glowThis(itemThreeImg);
        threeClicked = true;
        break;
      case WREATH:
        if (fourAdded) {
          return;
        }
        interactionHandler.glowThis(itemFourImg);
        fourClicked = true;
        break;
      case FEATHER:
        if (fiveAdded) {
          return;
        }
        interactionHandler.glowThis(itemFiveImg);
        fiveClicked = true;
        break;
      default:
        break;
    }

    // Setting the appropriate text for the text box
    // and setting the item to be added and letting the
    // system know that an item is ready to be added
    this.item = item;
    setText("Add to inventory?", true, true);
    mouseTrackRegion.setDisable(false);
    readyToAdd = true;
    System.out.println(item + " clicked");
  }

  /** Adding item to inventory if an item is selected */
  @FXML
  public void addItem() {
    if (!readyToAdd) {
      return;
    }
    MainMenuController.getInventory().add(item);
    setText("", false, false);
    readyToAdd = false;

    // If no item is selected but still added, place holder image
    image = new ImageView(new Image("images/place_holder.png"));
    ratio = 1;

    // Different controls are executed depending on the item
    switch (item) {
      case TAIL:
        one = new Image("images/tail.png");
        handleAddImg(one, itemOneImg, oneAdded, oneClicked);
        break;
      case INSECT_WINGS:
        two = new Image("images/insect_wings.png");
        handleAddImg(two, itemTwoImg, twoAdded, twoClicked);
        break;
      case FLOWER:
        three = new Image("images/flower.png");
        handleAddImg(three, itemThreeImg, threeAdded, threeClicked);
        break;
      case SCALES:
        four = new Image("images/scales.png");
        handleAddImg(four, itemFourImg, fourAdded, fourClicked);
        break;
      case POWDER:
        five = new Image("images/powder.png");
        handleAddImg(five, itemFiveImg, fiveAdded, fiveClicked);
        break;
      case TALON:
        one = new Image("images/talon.png");
        handleAddImg(one, itemOneImg, oneAdded, oneClicked);
        break;
      case CRYSTAL:
        two = new Image("images/crystal.png");
        handleAddImg(two, itemTwoImg, twoAdded, twoClicked);
        break;
      case BAT_WINGS:
        three = new Image("images/bat_wings.png");
        handleAddImg(three, itemThreeImg, threeAdded, threeClicked);
        break;
      case WREATH:
        four = new Image("images/wreath.png");
        handleAddImg(four, itemFourImg, fourAdded, fourClicked);
        break;
      case FEATHER:
        five = new Image("images/feather.png");
        handleAddImg(five, itemFiveImg, fiveAdded, fiveClicked);
        break;
      default:
        break;
    }
    itemCollect(ratio, image);
  }

  /**
   * Handling the event where an item is added to bag and formatting image size ratio
   */
  protected void handleAddImg(Image img, ImageView itemImg, boolean itemAdded, boolean itemClicked) {
    // Setting up appropriate image and ratio and appropriate click fields to be added
    ratio = img.getHeight() / img.getWidth();
    image = new ImageView(img);
    itemImg.setOpacity(0);
    itemImg.setDisable(true);
    itemAdded = true;
    itemClicked = false;
  }

  /**
   * Not adding a selected item to the inventory
   */
  @FXML
  public void noAdd() {
    if (!readyToAdd) {
      return;
    }
    setText("", false, false);
    readyToAdd = false;

    // Turning off the glow effect for all items
    oneClicked = false;
    interactionHandler.unglowThis(itemOneImg);
    twoClicked = false;
    interactionHandler.unglowThis(itemTwoImg);
    threeClicked = false;
    interactionHandler.unglowThis(itemThreeImg);
    fourClicked = false;
    interactionHandler.unglowThis(itemFourImg);
    fiveClicked = false;
    interactionHandler.unglowThis(itemFiveImg);

    // Making sure the mouseTrackRegion is disabled
    mouseTrackRegion.setDisable(true);
    System.out.println("Item not added to inventory");
  }

  /**
   * Dealing with closing the inventory or text box by clicking
   * a region outside of the inventory or text box.
   */
  @FXML
  public void clickOff(MouseEvent event) {
    System.out.println("click off");
    setText("", false, false);

    mouseTrackRegion.setOpacity(0);
    mouseTrackRegion.setDisable(true);
    wizardChatImage.setOpacity(0);
    wizardChatImage.setDisable(true);
    textRect.setOpacity(0);
    textRect.setDisable(true);
    // chatTextArea.setDisable(true);
    // chatTextArea.setOpacity(0);

    // Turning off the glow effect for all items
    interactionHandler.unglowThis(itemOneImg);
    interactionHandler.unglowThis(itemTwoImg);
    interactionHandler.unglowThis(itemThreeImg);
    interactionHandler.unglowThis(itemFourImg);
    interactionHandler.unglowThis(itemFiveImg);

    // None of the items are clicked anymore
    oneClicked = false;
    twoClicked = false;
    threeClicked = false;
    fourClicked = false;
    fiveClicked = false;

    // Handling closing the "bag" when clicking off inventory
    if (bagOpened) {
      itemScroll.setOpacity(0);
      bagOpened = false;
      System.out.println("Bag closed");
    }
  }
  
  /**
   * Making text box appear or dissapear with given text.
   *
   * @param text the text to be displayed
   * @param on   whether the text box should be visible or not
   */
  @FXML
  protected void setText(String text, boolean on, boolean yesNo) {
    textLbl.setText(text);
    if (on) {
      textRect.setOpacity(1);
      textRect.setDisable(false);
      textLbl.setOpacity(1);
      textLbl.setDisable(false);

      // Decision labels need to be refactored to deal with
      // different room interactions, e.g. proceed.
      yesLbl.setDisable(!yesNo);
      noLbl.setDisable(!yesNo);
      if (yesNo) {
        yesLbl.setOpacity(1);
        yesLbl.setDisable(false);
        noLbl.setOpacity(1);
        noLbl.setDisable(false);
        dashLbl.setOpacity(1);
      } else {
        yesLbl.setOpacity(0);
        yesLbl.setDisable(true);
        noLbl.setOpacity(0);
        noLbl.setDisable(true);
        dashLbl.setOpacity(0);
      }
    } else {
      textRect.setOpacity(0);
      textRect.setDisable(true);
      textLbl.setOpacity(0);
      textLbl.setDisable(true);
      yesLbl.setOpacity(0);
      yesLbl.setDisable(true);
      noLbl.setOpacity(0);
      noLbl.setDisable(true);
      dashLbl.setOpacity(0);
    }
  }

  public static void goDirection(Pane pane, AppUi room) {
    // Resetting appropriate fields before changing scenes
    readyToAdd = false;
    bagOpened = false;
    SceneManager.setTimerScene(room);
    TransitionAnimation.changeScene(pane, room, false);
  }

  public static void openBook(AppUi currScene, Pane pane) {
    BookController bookController = SceneManager.getBookControllerInstance();
    if (bookController != null) {
      bookController.updateBackground();
    }
    SceneManager.currScene = currScene;
    // Transitioning to the book scene with the appropriate fade animation
    TransitionAnimation.changeScene(pane, AppUi.BOOK, false);
  }

  /**
   * Dealing with the event where the bag icon is clicked
   */
  @FXML
  public void clickBag() {
    // If there are no items in the inventory, can't open the bag
    //  if (MainMenuController.inventory.size() == 0) {
    //   notificationText.setText("You have no ingredients in your bag!");
    //   Notification.notifyPopup(notificationBack, notificationText);
    //   return;
    // }

    // If the bag isn't opened already, open it
    if (!bagOpened) {
      itemScroll.setVvalue(0);
      itemScroll.setContent(null);
      itemScroll.setContent(MainMenuController.getInventory().getBox());
      itemScroll.setOpacity(1);
      bagOpened = true;
      mouseTrackRegion.setDisable(false);
      System.out.println("Bag opened");
    }
  }

  /**
   * Dealing with the event where the wizard icon is clicked
   */
  @FXML
  public void clickWizard(MouseEvent event) {
    System.out.println("wizard clicked");
    if (!GameState.isBookRiddleResolved) {
      showWizardChat();
      GameState.isBookRiddleGiven = true;
    } else {
      showWizardChat();
    }
  }

  /**
   * Displaying wizard chat to user when prompted
   */
  protected void showWizardChat() {
    // Setting approrpiate fields to be visible and interactable
    wizardChatImage.setDisable(false);
    wizardChatImage.setOpacity(1);
    textRect.setDisable(false);
    mouseTrackRegion.setDisable(false);
    setText("I'm keeping an eye on you", true, false);
    mouseTrackRegion.setOpacity(0.5);
  }

  /**
   * Handling the identical parts of addItem in the treasure room and
   * library room in a single method.
   * 
   * @param ratio the ratio between the image's width and height
   * @param image an image of the item to be added to the inventory
   */
  public void itemCollect(double ratio, ImageView image) {
    image.setFitHeight(133 * ratio);
    image.setFitWidth(133);
    // Using the inventory instance from the MainMenuController so that images
    // added from other scenes are not lost
    MainMenuController.getInventory().getBox().getChildren().add(image);

    mouseTrackRegion.setDisable(true);
    // To see what is in the inventory in the terminal
    // Can be removed later
    System.out.println("Item added to inventory");
    System.out.println("Current Inventory:");
    new MainMenuController();
    Iterator<Item> itr = MainMenuController.getInventory().getInventory().iterator();
    while (itr.hasNext()) {
      System.out.println("  " + itr.next());
    }
  }
}