package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


public class Game extends Application {
    private static int lifesLeft = 5;
    private static String mazeName;
    public ImageView pacmanImage,
            purpleGhostImage,
            orangeGhostImage,
            pinkGhostImage,
            greenGhostImage,
            blueGhostImage;
    public static Pacman pacman = new Pacman(false),
            blueGhost = new Pacman(true),
            greenGhost = new Pacman(true),
            orangeGhost = new Pacman(true),
            pinkGhost = new Pacman(true),
            purpleGhost = new Pacman(true);
    public static boolean isKillerMode = false;
    public boolean gameIsRunning = true;
    public GridPane graphicalMaze;
    public static Parent pane;
    public Label score;
    public Button muteButton;
    public Button pauseButton;
    public ImageView heart1fxml, heart2fxml, heart3fxml, heart4fxml, heart5fxml;
    public static ImageView heart1, heart2, heart3, heart4, heart5;

    public static void main(String[] args) {
        launch(args);
    }

    public static int getLifesLeft() {
        return lifesLeft;
    }

    public static void increaseLifes(int i) {
        lifesLeft += i;
    }

    @Override
    public void start(Stage stage) throws Exception {
        pane = FXMLLoader.load(getClass().getResource("fxmls/game.fxml"));
        Scene gameScene = new Scene(pane);
        gameScene.getStylesheets().add(getClass().getResource("css/game.css").toExternalForm());
        stage.setScene(gameScene);
        stage.show();
    }

    @FXML
    public void initialize() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        SoundPlayer.initializeGameSounds();
        heart1 = heart1fxml;
        heart2 = heart2fxml;
        heart3 = heart3fxml;
        heart4 = heart4fxml;
        heart5 = heart5fxml;
        initializeHearts();
        Image ghost1 = new Image(getClass().getResource("images/ghost-blue.gif").toExternalForm()),
                ghost2 = new Image(getClass().getResource("images/ghost-green.gif").toExternalForm()),
                ghost3 = new Image(getClass().getResource("images/ghost-orange.gif").toExternalForm()),
                ghost4 = new Image(getClass().getResource("images/ghost-pink.gif").toExternalForm()),
                ghost5 = new Image(getClass().getResource("images/ghost-purple.gif").toExternalForm()),
                ghostHaunted = new Image(getClass().getResource("images/ghost-haunted.gif").toExternalForm()),
                pacmanUp = new Image(getClass().getResource("images/pacman-up.gif").toExternalForm()),
                pacmanDown = new Image(getClass().getResource("images/pacman-down.gif").toExternalForm()),
                pacmanLeft = new Image(getClass().getResource("images/pacman-left.gif").toExternalForm()),
                pacmanRight = new Image(getClass().getResource("images/pacman-right.gif").toExternalForm()),
                cherryImage = new Image(getClass().getResource("images/cherry.jpg").toExternalForm()),
                strawberryImage = new Image(getClass().getResource("images/strawberry.jpg").toExternalForm());
        MazeGenerator.buildGraphicalRandomMaze(pane, graphicalMaze, cherryImage, strawberryImage);
        pacman.setPacmanImage(pacmanImage, pacmanUp, pacmanDown, pacmanLeft, pacmanRight);
        purpleGhost.setGhostImage(purpleGhostImage, ghost5, ghostHaunted);
        greenGhost.setGhostImage(greenGhostImage, ghost2, ghostHaunted);
        orangeGhost.setGhostImage(orangeGhostImage, ghost3, ghostHaunted);
        pinkGhost.setGhostImage(pinkGhostImage, ghost4, ghostHaunted);
        blueGhost.setGhostImage(blueGhostImage, ghost1, ghostHaunted);
        pacman.setDirection(MovingDirection.NONE);
        blueGhost.setDirection(MovingDirection.LEFT);
        greenGhost.setDirection(MovingDirection.RIGHT);
        orangeGhost.setDirection(MovingDirection.UP);
        pinkGhost.setDirection(MovingDirection.DOWN);
        purpleGhost.setDirection(MovingDirection.LEFT);
        Timeline pacmanFrameByFrameTimeline = new Timeline(new KeyFrame(Duration.millis(16), event -> changePacmanPosition()));
        pacman.translateX(2);
        pacman.translateY(2);
        greenGhost.getImageView().setX(2 + 19 * (17));
        greenGhost.getImageView().setY(2 + 19 * (18));
        blueGhost.getImageView().setX(2 + 19 * (17));
        blueGhost.getImageView().setY(2 + 19 * (18));
        orangeGhost.getImageView().setX(2);
        orangeGhost.getImageView().setY(2 + 19 * (18));
        pinkGhost.getImageView().setX(2);
        pinkGhost.getImageView().setY(2 + 19 * (18));
        purpleGhost.getImageView().setX(2 + 19 * (17));
        purpleGhost.getImageView().setY(2);
        pacmanFrameByFrameTimeline.setCycleCount(Timeline.INDEFINITE);
        pacmanFrameByFrameTimeline.play();
    }

    private void initializeHearts() {
        switch (lifesLeft) {
            case 2: heart3fxml.setImage(null);
            case 3: heart4fxml.setImage(null);
            case 4: heart5fxml.setImage(null);
        }
    }

    public static void playerControls(KeyEvent keyEvent) {
                    switch (keyEvent.getCode()) {
                        case W -> pacman.setDirection(MovingDirection.UP);
                        case S -> pacman.setDirection(MovingDirection.DOWN);
                        case A -> pacman.setDirection(MovingDirection.LEFT);
                        case D -> pacman.setDirection(MovingDirection.RIGHT);
                    }

    }

    @FXML
    private void changePacmanPosition() {
        if (!gameIsRunning)
            return;
        if (Player.getLoggedInUser() == null)
            Player.setLoggedInPlayer(new Player("sasaf", "asfas"));
        score.setText("  SCORE  " + "\n  " + Player.getLoggedInUser().score + "  ");
        changePositionBasedOnDirection(pacman);
        changePositionBasedOnDirection(greenGhost);
        changePositionBasedOnDirection(blueGhost);
        changePositionBasedOnDirection(purpleGhost);
        changePositionBasedOnDirection(pinkGhost);
        changePositionBasedOnDirection(orangeGhost);
    }

    private void changePositionBasedOnDirection(Pacman movingObject) {

        switch (movingObject.getDirection()) {
            case UP -> movingObject.translateY(-((!movingObject.isGhost())?1.8:1.5));
            case DOWN -> movingObject.translateY(((!movingObject.isGhost())?1.8:1.5));
            case RIGHT -> movingObject.translateX(((!movingObject.isGhost())?1.7:1.416));
            case LEFT -> movingObject.translateX(-((!movingObject.isGhost())?1.7:1.416));
        }
    }


    public static void enableKillerPacman() {
        isKillerMode = true;
        Timeline killerMode = new Timeline(new KeyFrame(Duration.seconds(9), event -> isKillerMode = false));
        killerMode.play();
        greenGhost.setImage();
        blueGhost.setImage();
        pinkGhost.setImage();
        purpleGhost.setImage();
        orangeGhost.setImage();
        killerMode.setOnFinished(actionEvent -> {
            killerMode.stop();
            isKillerMode = false;
            greenGhost.setImage();
            blueGhost.setImage();
            pinkGhost.setImage();
            purpleGhost.setImage();
            orangeGhost.setImage();
        });
    }

    public static void consumeLife() {
        if (lifesLeft == 5)
            heart5.setImage(null);
        else if (lifesLeft == 4)
            heart4.setImage(null);
        else if (lifesLeft == 3)
            heart3.setImage(null);
        else if (lifesLeft == 2)
            heart2.setImage(null);
        else if (lifesLeft == 1)
            heart1.setImage(null);
        else if (lifesLeft == 0)
            gameOver();
        lifesLeft--;

    }

    private static void gameOver() {
        System.out.println("game over");
    }

    public void mute(MouseEvent mouseEvent) {
        SoundPlayer.toggleMute();
        String text = muteButton.getText();
        if (text.equals("mute")) {
            muteButton.setText("unmute");
        } else {
            muteButton.setText("mute");
        }
    }

    public void pause(MouseEvent mouseEvent) {
        String text = pauseButton.getText();
        if (text.equals("pause")) {
            pauseButton.setText("unpause");
            gameIsRunning = false;
        } else {
            pauseButton.setText("pause");
            gameIsRunning = true;
        }
    }
}
