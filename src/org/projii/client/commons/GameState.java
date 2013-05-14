package org.projii.client.commons;

import java.util.LinkedList;

import org.jai.BSON.BSONSerializable;


public class GameState {
	@BSONSerializable
	public final LinkedList<PlayerInfo> players;
	
	public GameState(LinkedList<PlayerInfo> info) {
		this.players  = info;
	}
}
