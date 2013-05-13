package org.projii.client.net.GameServer;

public final class ConnectionState {
	public static final int JOIN = 1;
	public static final int WAIT_FOR_GAMESTATE = 2;
	public static final int MOVETO_FIRETO = 3;
	public static final int ERROR = 0;
	public static final int OFFLINE = -1;
}
