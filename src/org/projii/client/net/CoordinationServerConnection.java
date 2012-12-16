package org.projii.client.net;

import org.projii.commons.GameInfo;
import org.projii.commons.spaceship.Spaceship;

import java.util.List;

public interface CoordinationServerConnection {

    boolean logIn(String login, String password);

    List<Spaceship> getMyShips();

    List<GameInfo> getGamesList();

    void logOut();

    GameServerConnection joinGame(GameInfo g);
}
