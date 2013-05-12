package org.projii.client.net.GameServer;

public class ConnectionTest {
	public static void main(String[] args) {
		BasicGameServerConnection gameConnection = new BasicGameServerConnection("localhost", 10234, 20);
		while(true){
			System.out.println("This is main thread");
			try {
				Thread.sleep(1000);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
