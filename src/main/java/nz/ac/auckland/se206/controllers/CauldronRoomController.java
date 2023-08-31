package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;

public class CauldronRoomController {
  @FXML private Rectangle cauldronRectangle;
  @FXML private Rectangle wizardRectangle;
  @FXML private Polygon rightArrow;
  @FXML private Polygon leftArrow;
  @FXML private ImageView bookBtn;

  @FXML private ShapeInteractionHandler interactionHandler;

  @FXML
  public void initialize() {
    interactionHandler = new ShapeInteractionHandler();

    if (cauldronRectangle != null) {
      cauldronRectangle.setOnMouseEntered(event -> interactionHandler.handle(event));
      cauldronRectangle.setOnMouseExited(event -> interactionHandler.handle(event));
    }

    if (wizardRectangle != null) {
      wizardRectangle.setOnMouseEntered(event -> interactionHandler.handle(event));
      wizardRectangle.setOnMouseExited(event -> interactionHandler.handle(event));
    }

    if (rightArrow != null) {
      rightArrow.setOnMouseEntered(event -> interactionHandler.handle(event));
      rightArrow.setOnMouseExited(event -> interactionHandler.handle(event));
    }

    if (leftArrow != null) {
      leftArrow.setOnMouseEntered(event -> interactionHandler.handle(event));
      leftArrow.setOnMouseExited(event -> interactionHandler.handle(event));
    }
  }

  @FXML
  public void clickCauldron(MouseEvent event) {
    System.out.println("cauldron clicked");
  }

  @FXML
  public void clickWizard(MouseEvent event) {
    System.out.println("wizard clicked");
  }

  @FXML
  public void goLeft(MouseEvent event) {
    System.out.println("CAULDRON_ROOM > SHELF_LEFT");
    Scene currentScene = cauldronRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.SHELF_LEFT));
  }

  @FXML
  public void goRight(MouseEvent event) {
    System.out.println("CAULDRON_ROOM > SHELF_RIGHT");
    Scene currentScene = cauldronRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.SHELF_RIGHT));
  }

  @FXML
  public void glowThis(Shape shape) {
    shape.setStrokeWidth(5);
  }

  @FXML
  private void unglowThis(Shape shape) {
    shape.setStroke(null); // Remove the stroke to "unglow"
  }

  @FXML
  void openBook() {
    System.out.println("CAULDRON_ROOM > BOOK");
    SceneManager.currScene = AppUi.CAULDRON_ROOM;
    bookBtn.getScene().setRoot(SceneManager.getUiRoot(AppUi.BOOK));
  }
}
