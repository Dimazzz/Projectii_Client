package org.projii.client.net.GameServer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.jai.BSON.*;
import org.projii.client.net.GameServer.Messages.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicGameServerConnection {
	
	private AtomicInteger connectionState;
	private boolean isJoined;
	private long userId;
	private DatagramSocket sk;
	private String address;
	private int port;
	
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
	
	public void runSendData(final BasicGameServerConnection connection) {
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            connection.sendData();
	        }
	    }).start();	
	}
	
	public void runReceiveData(final BasicGameServerConnection connection) {
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            connection.receiveData();
	        }
	    }).start();	
	}
	
	public void sendData() { 
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
					Thread.sleep(500);
				} 
				catch (Exception e) {   
					e.printStackTrace();
				}
				break;
			}
	}
			
	public void receiveData() {
		DatagramPacket dp;
		byte[] packetData = new byte[65536];
		Integer type;
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
				JoinResponseMessage response = null;
				type = (Integer) message.get("type");
				if (type == GameServerResponses.JOIN_RESULT)
					response = (JoinResponseMessage) BSONSerializer.deserialize(JoinResponseMessage.class, message);
				if (response.getJoinResult() == 1) {
					this.isJoined = true;
					this.connectionState.set(ConnectionState.WAIT_FOR_GAMESTATE);
				}
				break;
			}
		}
	}

}