package model;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;

public class Player {

    private String username , password;
    private boolean shouldBeDeleted = false;
    private int highScore = 0;
    public transient int score = 0;
    private static ArrayList<Player> ALL_PLAYERS = new ArrayList<>();
    private static Player loggedInPlayer;

    public Player (String username ,String password) {
        this.username = username;
        this.password = password;
        ALL_PLAYERS.add(this);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setShouldBeDeleted(boolean shouldBeDeleted) {
        this.shouldBeDeleted = shouldBeDeleted;
    }

    public static ArrayList<Player> getAllPlayers() {
        return ALL_PLAYERS;
    }

    public static Player getLoggedInUser() {
        return loggedInPlayer;
    }

    public static void  setAllPlayers(ArrayList<Player> allPlayers) {
        ALL_PLAYERS = allPlayers;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void setLoggedInPlayer(Player loggedInPlayer) {
        Player.loggedInPlayer = loggedInPlayer;
    }

    public static Player getPlayerByUsername(String username) {
        for (Player player : ALL_PLAYERS) {
            if (player.getUsername().equals(username))
                return player;
        }
        return null;
    }

    public int getHighScore() {
        return highScore;
    }
}
