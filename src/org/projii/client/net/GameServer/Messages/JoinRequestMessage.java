package org.projii.client.net.GameServer.Messages;

import org.jai.BSON.BSONSerializable;
import org.projii.client.net.InteractionMessage;
import org.projii.client.net.GameServer.GameServerRequests;

public class JoinRequestMessage implements InteractionMessage{
	
	@BSONSerializable
	private int type;
	
	@BSONSerializable
	private long userId;
	
	public JoinRequestMessage(long userId) {
		this.type = GameServerRequests.JOIN_REQUEST;
		this.userId = userId;
	}
	
	public int getType() {
		return type;
	}
	
	public long getUserId() {
		return this.userId;
	}
}
