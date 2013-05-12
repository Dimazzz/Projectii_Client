package org.projii.client.net.GameServer;

import org.projii.commons.GameState;

import java.util.List;

public interface GameServerConnection {

    List<Object> getPlayers();

    boolean join(long userId);

    GameState getGameState();

    Object sendMyState();

    boolean logOut();
}
