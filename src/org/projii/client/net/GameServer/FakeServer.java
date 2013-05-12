package org.projii.client.net.GameServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import org.jai.BSON.*;
import org.projii.client.net.InteractionMessage;

public class FakeServer {
	
	private int port;
	private DatagramSocket sk;
	private int connectionState;
	
	public FakeServer (int port) {
		this.port = port;
		this.connectionState = ConnectionState.JOIN;
		try {
			sk = new DatagramSocket(port);
		} 
		catch (Exception e) {   
			e.printStackTrace();
		}
		this.runReceiveData(this);
	}
	
	public void runReceiveData(final FakeServer server) {
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            server.receiveData();
	        }
	    }).start();	
	}
	
	public void receiveData() {
		
		DatagramPacket dp;
		byte[] packetData = null;
		byte[] data = null;
		while (connectionState != ConnectionState.OFFLINE) {
			packetData = new byte[65536];
			dp = new DatagramPacket(packetData, packetData.length);
			try{
				sk.receive(dp);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			BSONDocument message = BSONDecoder.decode(ByteBuffer.wrap(packetData));
			System.out.println(message.get("type"));
			System.out.println(message.size());
			JoinMessage request = (JoinMessage) BSONSerializer.deserialize(JoinMessage.class, message);
			System.out.println(request.getUserId());
			switch(this.connectionState) {
			case ConnectionState.JOIN:
				try {
					Thread.sleep(500);
				} 
				catch (Exception e) {   
					e.printStackTrace();
				}
				break;
			}
		}
	}
}
	
