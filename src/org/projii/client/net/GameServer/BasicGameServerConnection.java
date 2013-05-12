package org.projii.client.net.GameServer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.jai.BSON.*;
import org.projii.client.net.GameServer.Messages.JoinMessage;

public class BasicGameServerConnection {
	
	private int connectionState;
	private boolean isJoined;
	private long userId;
	private DatagramSocket sk;
	private String address;
	private int port;
	
	public BasicGameServerConnection(String address, int port, long userId) {
		this.connectionState = ConnectionState.JOIN;
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
		while (connectionState != ConnectionState.OFFLINE) 
			switch(this.connectionState) {
			case ConnectionState.JOIN:
				BSONDocument joinMessage = BSONSerializer.serialize(new JoinMessage(this.userId));
				byte[] data = BSONEncoder.encode(joinMessage).array();
				DatagramPacket dp = new DatagramPacket(data, data.length);
				while(this.connectionState == ConnectionState.JOIN) {
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
			}
	}
			
	public void receiveData() {
		DatagramPacket dp;
		byte[] data;
		while (connectionState != ConnectionState.OFFLINE)
			
			switch(this.connectionState) {
			case ConnectionState.JOIN:
				
				break;
			}
	}

}
