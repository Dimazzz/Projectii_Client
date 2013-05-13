package org.projii.client.net.GameServer.Messages;

import org.jai.BSON.BSONSerializable;
import org.projii.client.commons.GameState;
import org.projii.client.net.InteractionMessage;
import org.projii.client.net.GameServer.GameServerResponses;

public class GameStateResponseMessage implements InteractionMessage{
	@BSONSerializable
	private final int type;
	
	@BSONSerializable
	private final GameState state;
	
	public GameStateResponseMessage(GameState state) {
		this.type = GameServerResponses.GAMESTATE;
		this.state = state;
	}
	
	public int getType() {
		return this.type;
	}
	
	public GameState getGameState() {
		return this.state;
	}

}
