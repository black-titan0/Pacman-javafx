package view;

import controller.LoginRegisterController;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Database;
import model.Player;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SettingPage {
    public StackPane stackPane;
    public TextField newPasswordInput;
    public Label heartsText;

    @FXML
    public void initialize() {
        heartsText.setText(Game.getLifesLeft() + "");
    }
    public void openMainMenu(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/MainPage.fxml"));
        Scene scene = newPasswordInput.getScene();
        Timeline animationTimeLine = new Timeline();
        animationTimeLine.setOnFinished(actionEvent -> {
            stackPane.getChildren().remove(0);
            stackPane.getChildren().remove(0);
            ((StackPane) scene.getRoot()).getChildren().add(root);
            scene.getStylesheets().remove(0);
        });
        stackPane = (StackPane) scene.getRoot();
        stackPane.getChildren().add(root);
        root.translateXProperty().set(-480);
        scene.getStylesheets().remove(1);
        KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0 , Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame =new KeyFrame(Duration.seconds(1) , nextPageKeyValue);
        Timeline currentPageAnimationTimeLine = new Timeline();
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), +480 , Interpolator.EASE_IN);
        KeyFrame currentPageKeyFrame =new KeyFrame(Duration.seconds(1) , currentPageKeyValue);
        scene.getStylesheets().add(getClass().getResource("css/FirstPage.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/RegisterPage.css").toExternalForm());
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        HBox hBox = ((HBox)(((VBox)root.getChildrenUnmodifiable().get(0)).getChildren().get(1)));
        ((Label)hBox.getChildren().get(0)).setText(Player.getLoggedInUser().getUsername());
        ((Label)hBox.getChildren().get(1)).setText(Player.getLoggedInUser().getHighScore() + "");
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();
    }

    public void changePassword(MouseEvent mouseEvent) {
        LoginRegisterController.getInstance().changePassword(newPasswordInput.getText());
    }

    public void deleteAcount(MouseEvent mouseEvent) throws IOException {
        Database.deletePlayer(Player.getLoggedInUser().getUsername());
        Player.setLoggedInPlayer(null);
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.seconds(1));
        fadeTransition.setNode(newPasswordInput.getScene().getRoot());
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> {
            try {
                loadFirstPage();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

    }

    private void loadFirstPage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/FirstPage.fxml"));
        root.setOpacity(0);
        Scene newScene = new Scene(root);
        newScene.getStylesheets().add(getClass().getResource("css/FirstPage.css").toExternalForm());
        Stage currentStage = (Stage) newPasswordInput.getScene().getWindow();
        currentStage.setWidth(480);
        currentStage.setHeight(480);
        currentStage.setScene(newScene);
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.seconds(1));
        fadeTransition.setNode(newScene.getRoot());
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

    }

    public void decreaseLifes(MouseEvent mouseEvent) {
        if (Game.getLifesLeft() >= 3)
            Game.increaseLifes(-1);
        heartsText.setText(Game.getLifesLeft() + "");
    }

    public void increaseLifes(MouseEvent mouseEvent) {
        if (Game.getLifesLeft() <= 4)
            Game.increaseLifes(1);
        heartsText.setText(Game.getLifesLeft() + "");
    }
}
