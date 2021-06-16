package view;

import controller.ErrorChecker;
import controller.LoginRegisterController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class RegisterPage {
    public Button backButton;
    public StackPane stackPane;
    public TextField registerUsernameInput;
    public TextField registerPasswordInput;

    public void openFirstPage(MouseEvent mouseEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("fxmls/FirstPage.fxml"));
        Scene scene = backButton.getScene();
        stackPane = (StackPane) scene.getRoot();
        root.translateXProperty().set(+480);
        stackPane.getChildren().add(root);
        Timeline animationTimeLine = new Timeline();
        Timeline currentPageAnimationTimeLine = new Timeline();
        KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0 , Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame =new KeyFrame(Duration.seconds(1) , nextPageKeyValue);
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), -480 , Interpolator.EASE_IN);
        KeyFrame currentPageKeyFrame =new KeyFrame(Duration.seconds(1) , currentPageKeyValue);
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();
        animationTimeLine.setOnFinished(actionEvent -> {
            stackPane.getChildren().remove(0);
            stackPane.getChildren().remove(0);
            scene.getStylesheets().remove(1);
            scene.setRoot(root);
        });

    }

    public void registerUser(MouseEvent mouseEvent) {
        String username = registerUsernameInput.getText();
        String  password = registerPasswordInput.getText();
        if (password.equals("") || username.equals(""))
            return;
        LoginRegisterController.getInstance().registerNewPlayer(username , password);
    }
}
