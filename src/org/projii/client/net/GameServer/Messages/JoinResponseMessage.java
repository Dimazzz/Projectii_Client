package org.projii.client.net.GameServer.Messages;

import org.jai.BSON.BSONSerializable;
import org.projii.client.net.InteractionMessage;
import org.projii.client.net.GameServer.GameServerResponses;

public class JoinResponseMessage implements InteractionMessage{
	
	@BSONSerializable
	private int type;
	
	@BSONSerializable
	private Integer joinResult;

	public JoinResponseMessage(Integer joinResult) {
		this.type = GameServerResponses.JOIN_RESULT;
		this.joinResult = joinResult;
	}
	
	public int getType() {
		return type;
	}
	
	public Integer getJoinResult() {
		return this.joinResult;
	}
}
