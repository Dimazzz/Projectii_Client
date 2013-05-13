package org.projii.client.commons;

import org.jai.BSON.BSONSerializable;
import org.projii.client.commons.spaceship.Spaceship;

public class PlayerInfo {
	@BSONSerializable
	public final long playerId;
	@BSONSerializable
	public final String playerName;
	@BSONSerializable
	public final Spaceship shipModel;
	
	public PlayerInfo(long id, String name, Spaceship model) {
		this.playerId = id;
		this.playerName = name;
		this.shipModel = model;
	}
}
