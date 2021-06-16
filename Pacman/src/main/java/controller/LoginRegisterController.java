package controller;

import model.Player;
import view.Prompt;

import java.util.Objects;

public class LoginRegisterController {

    private static LoginRegisterController loginRegisterController;


    private LoginRegisterController() {

    }

    public static LoginRegisterController getInstance() {
        return Objects.requireNonNullElseGet(loginRegisterController, () -> (loginRegisterController = new LoginRegisterController()));
    }

    public void registerNewPlayer(String username, String password) {
        if (ErrorChecker.isUsernameUnique(username))
        {
            new Player(username , password);
            Prompt.showMessage("User Registered!" , "Successful");
        }
    }
    public void login(String username , String password) {
        boolean isPermitted = ErrorChecker.doesUsernameExist(username)
                && ErrorChecker.isPasswordCorrect(username , password);
        if (isPermitted) {
            Player player = Player.getPlayerByUsername(username);
            Player.setLoggedInPlayer(player);
        }
    }

    public void changePassword(String newPass) {
        if (newPass.equals(""))
            return;
        Player.getLoggedInUser().setPassword(newPass);
        Prompt.showMessage("Password Changed Successfully" , "Successful");
    }
}
