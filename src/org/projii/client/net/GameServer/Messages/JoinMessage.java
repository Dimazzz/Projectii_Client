package org.projii.client.net.GameServer.Messages;

import org.jai.BSON.BSONSerializable;
import org.projii.client.net.InteractionMessage;
import org.projii.client.net.GameServer.GameServerRequests;

public class JoinMessage implements InteractionMessage{
	
	@BSONSerializable
	private int type;
	
	@BSONSerializable
	private long userId;
	
	public JoinMessage(long userId) {
		this.type = GameServerRequests.JOIN;
		this.userId = userId;
	}
	
	public int getType() {
		return type;
	}
}
