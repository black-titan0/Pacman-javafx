package model;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import view.Game;

import java.util.*;

public class MazeGenerator {

    public static int numberOfStars = 0;

    public enum Direction {right, down, left, up, start}
    public static ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(Direction.right, Direction.down, Direction.left, Direction.up));
    public static HashMap<Integer, StringBuilder> mazeMap;
    public static int numberOfRows, numberOfColumns;
    public static StringBuilder rowTemplate, firstRowTemplate, lastRowTemplate;
    public static Random random = new Random();
    private static GridPane graphicalMaze;

    public static void createTemplates() {
        int numberOfMapColumns = 2 * numberOfColumns + 1;
        rowTemplate = new StringBuilder("1");
        rowTemplate.append("1".repeat(Math.max(0, numberOfMapColumns - 1)));
        firstRowTemplate = new StringBuilder("11");
        firstRowTemplate.append("1".repeat(Math.max(0, numberOfMapColumns - 2)));
        lastRowTemplate = new StringBuilder("1");
        lastRowTemplate.append("1".repeat(Math.max(0, numberOfMapColumns - 3)));
        lastRowTemplate.append("11");
    }

    public static void createUndoneMaze() {
        numberOfStars = 0;
        mazeMap = new HashMap<>();
        for (int row = 0; row < 2 * numberOfRows; row++)
            mazeMap.put(row, (row == 0) ? new StringBuilder(firstRowTemplate) : new StringBuilder(rowTemplate));
        mazeMap.put(2 * numberOfRows, new StringBuilder(lastRowTemplate));
    }

    public static String printMaze() {
        StringBuilder maze = new StringBuilder();
        for (int row = 0; row < 2 * numberOfRows + 1; row++)
            maze.append(mazeMap.get(row)).append("\n");
        maze.append("\n");
        return maze.toString();
    }

    public static void walkThroughMazeRandomly(int row, int column, Direction direction) {
        column = (direction == Direction.right) ? column + 2 : (direction == Direction.left) ? column - 2 : column;
        row = (direction == Direction.down) ? row + 2 : (direction == Direction.up) ? row - 2 : row;
        if (row < 1
                || row > 2 * numberOfRows
                || column < 1
                || column > 2 * numberOfColumns
                || mazeMap.get(row).charAt(column) == '*') return;
        if (direction == Direction.down) mazeMap.get(row - 1).setCharAt(column, '0');
        if (direction == Direction.up) mazeMap.get(row + 1).setCharAt(column, '0');
        if (direction == Direction.right) mazeMap.get(row).setCharAt(column - 1, '0');
        if (direction == Direction.left) mazeMap.get(row).setCharAt(column + 1, '0');
        mazeMap.get(row).setCharAt(column, '*');
        numberOfStars++;
        Collections.shuffle(directions);
        walkThroughMazeRandomly(row, column, directions.get(0));
        walkThroughMazeRandomly(row, column, directions.get(1));
        walkThroughMazeRandomly(row, column, directions.get(2));
        walkThroughMazeRandomly(row, column, directions.get(3));
    }

    public static String getRandomMaze() {
        numberOfRows = 20;
        numberOfColumns = 20;
        createTemplates();
            createUndoneMaze();
            int rand = random.nextInt(2);
            if (rand == 0)
                walkThroughMazeRandomly(1, 1, Direction.start);
            else
                walkThroughMazeRandomly(2 * numberOfRows - 1, 2 * numberOfColumns - 1, Direction.start);
            omitRandomWalls();
            return printMaze();


    }

    private static void omitRandomWalls() {
        for (Integer row : mazeMap.keySet()) {
            for (int column = 0; column < mazeMap.size(); column++) {
                if (mazeMap.get(row).charAt(column) == '1') {
                    if (column == 0 || column == 40 || row == 0 || row == 40) {
                        mazeMap.get(row).setCharAt(column, '1');
                        continue;
                    }
                    double rand = random.nextDouble();
                    if (rand > 0.75)
                        mazeMap.get(row).setCharAt(column, '0');
                }
            }
        }
    }
    public static boolean isTransitionPossible(int firstX , int firstY , int secondX , int secondY) {
        firstX = 1 + (firstX * 2);
        secondX = 1 + (secondX * 2);
        firstY = 1 + (firstY * 2);
        secondY = 1 + (secondY * 2);
        int y = (firstY + secondY)/2;
        int x = (firstX + secondX)/2;
        char wallOrNot =  mazeMap.get(y).charAt(x);
        return wallOrNot != '1';
    }
    public static void visit(int x , int y) {
        x = 1 + (x * 2);
        y = 1 + (y * 2);
        char item = mazeMap.get(y).charAt(x);
        switch (item) {
            case '*' -> {
                if (SoundPlayer.getSoundByName("chomp") != null) {
                    SoundPlayer.getSoundByName("chomp").getClip().loop(0);
                }
            }
            case 'g' -> {
                if (SoundPlayer.getSoundByName("chomp") != null) {
                    SoundPlayer.getSoundByName("chomp").getClip().loop(1);
                }
                Player.getLoggedInUser().score += 10;
            }
            case 'c' -> {
                Player.getLoggedInUser().score += 20;
                if (SoundPlayer.getSoundByName("fruit-eating") != null) {
                    SoundPlayer.getSoundByName("fruit-eating").getClip().loop(1);
                }
            }
            case 's' -> {
                Player.getLoggedInUser().score += 30;
                if (SoundPlayer.getSoundByName("fruit-eating") != null) {
                    SoundPlayer.getSoundByName("fruit-eating").getClip().loop(1);
                }
            }
            case 'b' -> Game.enableKillerPacman();
        }
        Node node =getNodeByRowColumnIndex(y, x, graphicalMaze);
        if (node instanceof  Label)
            ((Label)node).setText("");
        else if (node instanceof ImageView)
            ((ImageView) node).setImage(null);
        mazeMap.get(y).setCharAt(x , '*');
    }
    public static void buildGraphicalRandomMaze(Parent pane,GridPane graphicalMaze , Image cherryImage , Image strawberryImage){
        StringBuilder maze = new StringBuilder(MazeGenerator.getRandomMaze());
        MazeGenerator.graphicalMaze = graphicalMaze;
        int numberOfLittleGums = 0;
        int limit;
        Random random = new Random();
        for (int i = 0, j = 0; i < maze.length(); i++) {
            limit = random.nextInt(15) + 45;
            if (maze.charAt(i) == '\n') {
                j++;
                continue;
            }
            Rectangle rectangle = new Rectangle();
            if (maze.charAt(i) != '*') {
                if (j % 2 == 0) {
                    if (i % 2 == 0) {
                        rectangle.setHeight(0);
                        rectangle.setWidth(0);
                    } else {
                        rectangle.setHeight(2);
                        rectangle.setWidth(15);
                    }
                } else {
                    rectangle.setHeight(15);
                    rectangle.setWidth(2);
                }
                if (maze.charAt(i) == '1')
                    rectangle.setFill(Paint.valueOf("cyan"));
                else
                    rectangle.setFill(Paint.valueOf("black"));
                graphicalMaze.add(rectangle, i - (j * 42), j);
            } else {
                Label label;
                if (numberOfLittleGums > limit) {
                    label = new Label("☼");
                    maze.setCharAt(i , 'b');
                    double rand = 0;
                    if ((rand = random.nextDouble()) > 0.5) {
                        ImageView imageView = new ImageView();
                        imageView.setFitWidth(15);
                        imageView.setFitHeight(15);
                        if (rand < 0.7) {
                            imageView.setImage(cherryImage);
                            maze.setCharAt(i , 'c');
                        }
                        else {
                            imageView.setImage(strawberryImage);
                            maze.setCharAt(i , 's');
                        }

                        graphicalMaze.add(imageView, i - (j * 42), j);
                        continue;
                    }

                    numberOfLittleGums = 0;
                } else {
                    label = new Label("•");
                    maze.setCharAt(i , 'g');
                    numberOfLittleGums++;
                }
                label.setTextFill(Paint.valueOf("white"));
                label.setPrefHeight(15);
                label.setPrefWidth(15);
                label.setAlignment(Pos.CENTER);
                graphicalMaze.add(label, i - (j * 42), j);
                resetMazeHashmap(maze.toString());
            }
        }
    }

    private static void resetMazeHashmap(String mazeString) {
        String[] mazeRows = mazeString.split("\n");
        for (int i = 0; i < mazeRows.length; i++) {
            mazeMap.put(i , new StringBuilder(mazeRows[i]));
        }
    }

    public static Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
}