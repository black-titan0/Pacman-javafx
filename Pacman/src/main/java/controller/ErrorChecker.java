package controller;

import model.Player;
import view.Prompt;

public class ErrorChecker {
    public static boolean isUsernameUnique(String username) {
        Player player = Player.getPlayerByUsername(username);
        if (player == null)
            return true;
        Prompt.showMessage("Username Already Exists!" , "Error");
        return false;
    }
    public static boolean doesUsernameExist(String username) {
        Player player = Player.getPlayerByUsername(username);
        if (player != null)
            return true;
        Prompt.showMessage("Username Doesn't Exist!" , "Error");
        return false;
    }

    public static boolean isPasswordCorrect(String username, String password) {
        Player player = Player.getPlayerByUsername(username);
        if (player.getPassword().equals(password))
            return true;
        Prompt.showMessage("Incorrect Password" , "Error");
        return false;
    }
}
