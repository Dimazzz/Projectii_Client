package org.projii.client.commons;

public class GameInfo {
    public final int gameId;
    public final String serverIP;
    public final String mapName;
    public final int currentPlayersAmount;
    public final int maxPlayersAmount;

    public GameInfo(int gameId, String serverIP, String mapName, int currentPlayersAmount, int maxPlayersAmount) {
        this.gameId = gameId;
        this.serverIP = serverIP;
        this.mapName = mapName;
        this.currentPlayersAmount = currentPlayersAmount;
        this.maxPlayersAmount = maxPlayersAmount;
    }
}
