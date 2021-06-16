package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import view.Game;


import javax.swing.*;
import java.util.*;

public class Pacman {
    private MovingDirection direction = MovingDirection.NONE,
            pastDirection = MovingDirection.NONE;
    private ImageView imageView;
    private final boolean isGhost;
    private final HashMap<MovingDirection, Image> pacmanImages = new HashMap<>();
    private final HashMap<Boolean, Image> ghostImages = new HashMap<>();
    private int moveCounter = 0;

    public Pacman(boolean isGhost) {
        this.isGhost = isGhost;
    }

    public void setPacmanImage(ImageView pacmanImage, Image pacmanUp, Image pacmanDown, Image pacmanLeft, Image pacmanRight) {
        this.imageView = pacmanImage;
        this.pacmanImages.put(MovingDirection.UP, pacmanUp);
        this.pacmanImages.put(MovingDirection.DOWN, pacmanDown);
        this.pacmanImages.put(MovingDirection.LEFT, pacmanLeft);
        this.pacmanImages.put(MovingDirection.RIGHT, pacmanRight);
    }

    public void setGhostImage(ImageView imageView, Image ghostImage, Image hauntedGhost) {
        this.imageView = imageView;
        this.ghostImages.put(false, ghostImage);
        this.ghostImages.put(true, hauntedGhost);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public MovingDirection getDirection() {
        return direction;
    }

    public void setDirection(MovingDirection direction) {
        goToNearestPlace();
        this.direction = direction;
        if (direction == MovingDirection.NONE)
            return;
        if (!isGhost)
            imageView.setImage(pacmanImages.get(direction));
    }

    private void goToNearestPlace() {
        int x = getRelativeX((int) ((imageView.getX() + 4) / 17));
        int y = getRelativeY((int) ((imageView.getY() + 4) / 18));
        imageView.setY(y);
        imageView.setX(x);
    }

    private int getRelativeX(int x) {
        return (17 * x) + 2;
    }

    private int getRelativeY(int y) {
        return (18 * y) + 2;
    }

    public final void setImage() {
        imageView.setImage(ghostImages.get(Game.isKillerMode));
    }
    public void translateY(double Y) {
        int x = (int) ((imageView.getX() + 4 + 3.25) / 17);
        int y1 = (int) ((imageView.getY() + 4 + (Math.signum(Y) * 3.25)) / 18);
        int y2 = (int) ((imageView.getY() + 4 + Y + (Math.signum(Y) * 3.25)) / 18);
        if (moveCounter == 10) {
            moveCounter = 0;
            setDirectionRandomly(x , y1);
            return;
        }
        if (isGhost) {
            checkCollisionWithPacman(imageView.getX(), imageView.getY());
            moveCounter++;
        }
        if ((imageView.getY() + 4 + Y + (Math.signum(Y) * 3.25)) < 0) {
            if (!isGhost)
                setDirection(MovingDirection.NONE);
            if (isGhost)
                setDirectionRandomly(x, y1);
            goToNearestPlace();
            return;
        }
        if (y1 != y2) {
            if (MazeGenerator.isTransitionPossible(x, y1, x, y2)) {
                imageView.setY(imageView.getY() + Y);
                if (!isGhost)
                    MazeGenerator.visit(x, y2);
            } else {
                if (!isGhost)
                    setDirection(MovingDirection.NONE);
                goToNearestPlace();
                if (isGhost)
                    setDirectionRandomly(x, y1);
            }
        } else {
            imageView.setY(imageView.getY() + Y);
        }

    }

    private void checkCollisionWithPacman(double x , double y) {
        double pacmanX = Game.pacman.getImageView().getX();
        double pacmanY = Game.pacman.getImageView().getY();

        if ((Math.abs(pacmanX - x)) < 5 && (Math.abs(pacmanY - y) < 5) ) {
            if (Game.isKillerMode) {
                Player.getLoggedInUser().score += 50;
                if (SoundPlayer.getSoundByName("ghost-eating") != null) {
                    SoundPlayer.getSoundByName("ghost-eating").getClip().loop(1);
                }
                imageView.setY(2 + 19 * 18);
                imageView.setX(2 + 19 * 17);
            }
            else {
                if (SoundPlayer.getSoundByName("die") != null) {
                    SoundPlayer.getSoundByName("die").getClip().loop(1);
                }
                Game.pacman.getImageView().setX(2);
                Game.pacman.getImageView().setY(2);
                Game.consumeLife();
            }
        }

    }

    public void translateX(double X) {

        int y = (int) ((imageView.getY() + 4 + 3.25) / 18);
        int x1 = (int) ((imageView.getX() + 4 + (Math.signum(X) * 3.25)) / 17);
        int x2 = (int) ((imageView.getX() + 4 + X + (Math.signum(X) * 3.25)) / 17);
        if (moveCounter == 10) {
            moveCounter = 0;
            setDirectionRandomly(x1 , y);
            return;
        }
        if (isGhost) {
            checkCollisionWithPacman(imageView.getX(), imageView.getY());
            moveCounter++;
        }
        if ((imageView.getX() + 4 + X + (Math.signum(X) * 3.25)) < 2) {
            if (!isGhost)
                setDirection(MovingDirection.NONE);
            if (isGhost)
                setDirectionRandomly(x1, y);
            goToNearestPlace();
            return;
        }
        if (x1 != x2) {
            if (MazeGenerator.isTransitionPossible(x1, y, x2, y)) {
                imageView.setX(imageView.getX() + X);
                if (!isGhost)
                    MazeGenerator.visit(x2, y);
            } else {
                goToNearestPlace();
                if (!isGhost)
                    setDirection(MovingDirection.NONE);
                if (isGhost)
                    setDirectionRandomly(x1, y);
            }
        } else {
            imageView.setX(imageView.getX() + X);
        }
    }

    private void setDirectionRandomly(int currentX, int currentY) {
        ArrayList<MovingDirection> possibleDirections = new ArrayList<>();
        if (MazeGenerator.isTransitionPossible(currentX, currentY, currentX + 1, currentY))
            possibleDirections.add(MovingDirection.RIGHT);
        if (MazeGenerator.isTransitionPossible(currentX, currentY, currentX, currentY + 1))
            possibleDirections.add(MovingDirection.DOWN);
        if (MazeGenerator.isTransitionPossible(currentX, currentY, currentX - 1, currentY))
            possibleDirections.add(MovingDirection.LEFT);
        if (MazeGenerator.isTransitionPossible(currentX, currentY, currentX, currentY - 1))
            possibleDirections.add(MovingDirection.UP);
        Collections.shuffle(possibleDirections);
        if (!(possibleDirections.size() == 1))
            while (possibleDirections.get(0) == getAntiDirection(direction))
                Collections.shuffle(possibleDirections);

        setDirection(possibleDirections.get(0));
    }
    private MovingDirection getAntiDirection(MovingDirection direction) {
        switch (direction) {
            case UP: return MovingDirection.DOWN;
            case DOWN: return MovingDirection.UP;
            case RIGHT: return MovingDirection.LEFT;
            case LEFT: return MovingDirection.RIGHT;
        }
        return MovingDirection.NONE;
    }

    public boolean isGhost() {
        return isGhost;
    }
}
