package org.projii.client.net.GameServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.jai.BSON.*;
import org.projii.client.net.GameServer.Messages.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FakeServer {
	
	private int port;
	private DatagramSocket sk;
	private AtomicInteger connectionState;
	private InetAddress address;
	
	public FakeServer (int port) {
		this.port = port;
		this.address = null;
		this.connectionState = new AtomicInteger(0);
		try {
			sk = new DatagramSocket(port);
		} 
		catch (Exception e) {   
			e.printStackTrace();
		}
		this.runSendData(this);
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
	
	public void runSendData(final FakeServer server) {
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            server.sendData();
	        }
	    }).start();	
	}
	
	public void sendData() {
		while (this.connectionState.get() != -1) 
			switch(this.connectionState.get()) {
			case 0:
				try {
					System.out.println("Waiting for join request");
					Thread.sleep(500);
				} 
				catch (Exception e) {   
					e.printStackTrace();
				}
				break;
			case 1:
				BSONDocument joinMessage = BSONSerializer.serialize(new JoinResponseMessage(1));
				byte[] data = BSONEncoder.encode(joinMessage).array();
				DatagramPacket dp = new DatagramPacket(data, data.length);
				int time = 0;
				while(this.connectionState.get() == 1) {
					try {
						sk.send(dp);
						System.out.println("Package Sent");
						Thread.sleep(500);
						time += 500;
						if (time == 3000)
							this.connectionState.set(2);
					} 
					catch (Exception e) {   
						e.printStackTrace();
					}
				}
				break;
			case 2:
				try {
					System.out.println("Waiting for MoveTo and SendTo");
					Thread.sleep(500);
				} 
				catch (Exception e) {   
					e.printStackTrace();
				}
			}
	}
	
	public void receiveData() {		
		DatagramPacket dp;
		byte[] packetData = new byte[65536];
		byte[] data = null;
		while (this.connectionState.get() != -1) {
			dp = new DatagramPacket(packetData, packetData.length);
			try{
				sk.receive(dp);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			BSONDocument message = BSONDecoder.decode(ByteBuffer.wrap(packetData));
			switch(this.connectionState.get()) {
			case 0:
				this.sk.connect(dp.getAddress(), dp.getPort());
				JoinRequestMessage request = (JoinRequestMessage) BSONSerializer.deserialize(JoinRequestMessage.class, message);
				if (request.getUserId() == 20)
					this.connectionState.set(1);
				break;
			}
		}
	}
}
