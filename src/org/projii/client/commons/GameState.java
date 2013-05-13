package org.projii.client.commons;

import java.util.List;

import org.jai.BSON.BSONSerializable;


public class GameState {
	@BSONSerializable
	public final List<PlayerInfo> players;
	
	public GameState(List<PlayerInfo> info) {
		this.players = info;
	}
}
