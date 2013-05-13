package org.projii.client.net.GameServer.Messages;

import org.jai.BSON.BSONSerializable;
import org.projii.client.net.InteractionMessage;
import org.projii.client.net.GameServer.GameServerResponses;

public class JoinResponseMessage implements InteractionMessage{
	
	@BSONSerializable
	private final int type;
	
	@BSONSerializable
	private final boolean joinResult;

	public JoinResponseMessage(boolean joinResult) {
		this.type = GameServerResponses.JOIN_RESULT;
		this.joinResult = joinResult;
	}
	
	public int getType() {
		return this.type;
	}
	
	public boolean getJoinResult() {
		return this.joinResult;
	}
}
