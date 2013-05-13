package org.projii.client.net.GameServer.Messages;

import org.jai.BSON.BSONSerializable;
import org.projii.client.net.InteractionMessage;
import org.projii.client.net.GameServer.GameServerRequests;

public class JoinRequestMessage implements InteractionMessage{
	
	@BSONSerializable
	private final int type;
	
	@BSONSerializable
	private final long userId;
	
	public JoinRequestMessage(long userId) {
		this.type = GameServerRequests.JOIN_REQUEST;
		this.userId = userId;
	}
	
	public int getType() {
		return this.type;
	}
	
	public long getUserId() {
		return this.userId;
	}
}
