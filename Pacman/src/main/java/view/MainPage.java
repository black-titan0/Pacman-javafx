package view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Player;

import java.io.IOException;

public class MainPage {
    public Button logoutButton;
    public StackPane stackPane;
    public Label userText;

    public void logoutClicked(MouseEvent mouseEvent) throws IOException {
        Player.setLoggedInPlayer(null);
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/LoginPage.fxml"));
        Scene scene = logoutButton.getScene();
        stackPane = (StackPane) scene.getRoot();
        stackPane.getChildren().add(root);
        root.translateYProperty().set(-480);
        Timeline animationTimeLine = new Timeline();
        Timeline currentPageAnimationTimeLine = new Timeline();
        KeyValue nextPageKeyValue = new KeyValue(root.translateYProperty(), 0 , Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame =new KeyFrame(Duration.seconds(1) , nextPageKeyValue);
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateYProperty(), +480 , Interpolator.EASE_IN);
        KeyFrame currentPageKeyFrame =new KeyFrame(Duration.seconds(1) , currentPageKeyValue);
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        animationTimeLine.setOnFinished(actionEvent -> {
            stackPane.getChildren().remove(0);
            stackPane.getChildren().remove(0);
            ((StackPane) scene.getRoot()).getChildren().add(root);
        });
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();


    }

    public void openNewGame(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/game.fxml"));
        Scene newScene = new Scene(root);
        newScene.getStylesheets().add(getClass().getResource("css/game.css").toExternalForm());
        ((Stage) userText.getScene().getWindow()).setScene(newScene);
        newScene.setOnKeyPressed(Game::playerControls);
    }

    public void openSettingsPage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/SettingPage.fxml"));
        Scene scene = logoutButton.getScene();
        stackPane = (StackPane) scene.getRoot();
        stackPane.getChildren().add(root);
        root.translateXProperty().set(+480);
        Timeline animationTimeLine = new Timeline();
        animationTimeLine.setOnFinished(actionEvent -> {
            stackPane.getChildren().remove(0);
            stackPane.getChildren().remove(0);
            ((StackPane) scene.getRoot()).getChildren().add(root);
        });
        scene.getStylesheets().remove(1);
        scene.getStylesheets().add(getClass().getResource("css/SettingPage.css").toExternalForm());
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), -480 , Interpolator.EASE_IN);
        KeyFrame currentPageKeyFrame =new KeyFrame(Duration.seconds(1) , currentPageKeyValue);
        KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0 , Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame =new KeyFrame(Duration.seconds(1) , nextPageKeyValue);
        Timeline currentPageAnimationTimeLine = new Timeline();
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();

    }
}
