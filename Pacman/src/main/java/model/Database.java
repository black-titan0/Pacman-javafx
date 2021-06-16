package model;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

public class Database {
    private static final Gson gson = new Gson();
    private static final String PLAYERS_PATH = "./src/main/resources/database/players/";
    public static void saveAllPlayers() throws IOException {
        ArrayList<Player> allPlayers = Player.getAllPlayers();
        File file = new File(PLAYERS_PATH);
        if (!file.exists())
            file.mkdir();

        for (Player player : allPlayers){
            String playerJSon = gson.toJson(player);
            FileWriter fileWriter = new FileWriter(PLAYERS_PATH + player.getUsername() + ".json");
            fileWriter.write(playerJSon);
            fileWriter.close();
        }
    }
    public static void loadAllPlayers() throws FileNotFoundException {
        ArrayList<Player> allPlayers = Player.getAllPlayers();
        File file = new File(PLAYERS_PATH);
        if (!file.exists())
            file.mkdir();
        for (int i = 0; i < file.listFiles().length; i++) {
            FileReader fileReader = new FileReader(PLAYERS_PATH + file.listFiles()[i].getName());
            Player player = gson.fromJson(fileReader , Player.class);
            allPlayers.add(player);
        }
        Player.setAllPlayers(allPlayers);
    }

    public static void deletePlayer(String username) {
        File file = new File(PLAYERS_PATH + "/" + username + ".json");
        file.delete();
    }
}
