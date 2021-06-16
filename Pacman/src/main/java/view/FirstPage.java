package view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Blend;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Database;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FirstPage extends Application {

    public Button registerButton;
    public Button loginButton;
    public StackPane stackPane;

    @Override
    public void start(Stage stage) throws Exception {
        Parent pane = FXMLLoader.load(getClass().getResource("fxmls/FirstPage.fxml"));
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("css/FirstPage.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Database.loadAllPlayers();
        launch(args);
    }

    public void exitClicked(MouseEvent mouseEvent) throws IOException {
        Database.saveAllPlayers();
        System.exit(0);
    }

    @FXML
    public void openRegisterPage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/RegisterPage.fxml"));
        Scene scene = registerButton.getScene();
        root.translateXProperty().set(-480);
        stackPane.getChildren().add(root);
        scene.getStylesheets().add(getClass().getResource("css/RegisterPage.css").toExternalForm());
        Timeline animationTimeLine = new Timeline();
        Timeline currentPageAnimationTimeLine = new Timeline();
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), 480 , Interpolator.EASE_IN);
        KeyFrame currentPageKeyFrame =new KeyFrame(Duration.seconds(1) , currentPageKeyValue);
        KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0 , Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame =new KeyFrame(Duration.seconds(1) , nextPageKeyValue);
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();
        animationTimeLine.setOnFinished(actionEvent -> stackPane.getChildren().remove(scene.getRoot().getChildrenUnmodifiable().get(0)));
    }

    public void openLoginPage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/LoginPage.fxml"));
        Scene scene = registerButton.getScene();
        root.translateXProperty().set(+480);
        stackPane.getChildren().add(root);
        scene.getStylesheets().add(getClass().getResource("css/RegisterPage.css").toExternalForm());
        Timeline animationTimeLine = new Timeline();
        Timeline currentPageAnimationTimeLine = new Timeline();
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), -480 , Interpolator.EASE_IN);
        KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0 , Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame =new KeyFrame(Duration.seconds(1) , nextPageKeyValue);
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        KeyFrame currentPageKeyFrame =new KeyFrame(Duration.seconds(1) , currentPageKeyValue);
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();
        animationTimeLine.setOnFinished(actionEvent -> stackPane.getChildren().remove(scene.getRoot().getChildrenUnmodifiable().get(0)));
    }
}
