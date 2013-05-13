package org.projii.client.net.GameServer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.jai.BSON.*;
import org.projii.client.commons.GameState;
import org.projii.client.net.GameServer.Messages.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicGameServerConnection {
	
	private AtomicInteger connectionState;
	private boolean isJoined;
	private long userId;
	private DatagramSocket sk;
	private String address;
	private int port;
	private GameState gameState;
	
	public BasicGameServerConnection(String address, int port, long userId) {
		this.connectionState = new AtomicInteger(ConnectionState.JOIN);
		this.isJoined = false;
		this.userId = userId;
		this.address = address;
		this.port = port;
		try {
			sk = new DatagramSocket();
			sk.connect(InetAddress.getByName(address), port);
		} 
		catch (Exception e) {   
			e.printStackTrace();
		}
		this.runSendData(this);	
		this.runReceiveData(this);
	}
	
	public Boolean join() {
		return isJoined;
	}
	
	private void runSendData(final BasicGameServerConnection connection) {
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            connection.sendData();
	        }
	    }).start();	
	}
	
	private void runReceiveData(final BasicGameServerConnection connection) {
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            connection.receiveData();
	        }
	    }).start();	
	}
	
	private void sendData() { 
		while (this.connectionState.get() != ConnectionState.OFFLINE) 
			switch(this.connectionState.get()) {
			
			case ConnectionState.JOIN:
				BSONDocument joinMessage = BSONSerializer.serialize(new JoinRequestMessage(this.userId));
				byte[] data = BSONEncoder.encode(joinMessage).array();
				DatagramPacket dp = new DatagramPacket(data, data.length);
				while(this.connectionState.get() == ConnectionState.JOIN) {
					try {
						sk.send(dp);
						System.out.println("Package Sent");
						Thread.sleep(500);
					} 
					catch (Exception e) {   
						e.printStackTrace();
					}
				}
				break;
				
			case ConnectionState.WAIT_FOR_GAMESTATE:
				System.out.println("Waiting for GameState");
				try {
					Thread.sleep(5000);
				} 
				catch (Exception e) {   
					e.printStackTrace();
				}
				break;
			case ConnectionState.MOVETO_FIRETO:
				System.out.println("Sending MoveTo FireTo");
				try {
					Thread.sleep(5000);
				} 
				catch (Exception e) {   
					e.printStackTrace();
				}
				break;
			}
		
		
	}
			
	private void receiveData() {
		DatagramPacket dp;
		byte[] packetData = new byte[65536];
		int type;
		while (this.connectionState.get() != ConnectionState.OFFLINE) {
			dp = new DatagramPacket(packetData, packetData.length);
			try{
				sk.receive(dp);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			BSONDocument message = BSONDecoder.decode(ByteBuffer.wrap(packetData));
			switch(this.connectionState.get()) {
			case ConnectionState.JOIN:
				JoinResponseMessage joinResponse = null;
				type = (Integer) message.get("type");
				if (type == GameServerResponses.JOIN_RESULT) {
					joinResponse = (JoinResponseMessage) BSONSerializer.deserialize(JoinResponseMessage.class, message);
					if (joinResponse.getJoinResult()) {
						this.isJoined = true;
						this.connectionState.set(ConnectionState.WAIT_FOR_GAMESTATE);
					}
				}
				break;
			case ConnectionState.WAIT_FOR_GAMESTATE:
				GameStateResponseMessage gameStateResponse = null;
				type = (Integer) message.get("type");
				if(type == GameServerResponses.GAMESTATE) {
					gameStateResponse = (GameStateResponseMessage) BSONSerializer.deserialize(GameStateResponseMessage.class, message);
					this.gameState = gameStateResponse.getGameState();
					System.out.println("Data received: " + dp.getLength());
					this.connectionState.set(ConnectionState.MOVETO_FIRETO);
				}
				break;
			}
		}
	}

}