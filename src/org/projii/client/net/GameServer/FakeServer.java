package org.projii.client.net.GameServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.jai.BSON.*;
import org.projii.client.commons.GameState;
import org.projii.client.commons.PlayerInfo;
import org.projii.client.net.GameServer.Messages.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.projii.client.commons.spaceship.*;
import org.projii.client.commons.spaceship.equipment.*;
import org.projii.client.commons.spaceship.weapon.*;


public class FakeServer {
	
	private int port;
	private DatagramSocket sk;
	private AtomicInteger connectionState;
	private InetAddress address;
	private GameState state;
	
	//State
	//0 - Waiting for Player
	//1 - Sending Join and GameState
	//2 - move and fire

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
	
	private void runReceiveData(final FakeServer server) {
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            server.receiveData();
	        }
	    }).start();	
	}
	
	private void runSendData(final FakeServer server) {
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            server.sendData();
	        }
	    }).start();	
	}
	
	private void sendData() {
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
				BSONDocument joinMessage = BSONSerializer.serialize(new JoinResponseMessage(true));
				byte[] data = BSONEncoder.encode(joinMessage).array();
				DatagramPacket dp = new DatagramPacket(data, data.length);
				int time = 0;
				while(time != 3000) {
					try {
						sk.send(dp);
						System.out.println("Join package Sent");
						Thread.sleep(500);
						time += 500;
					} 
					catch (Exception e) {   
						e.printStackTrace();
					}
				}
				
				BSONDocument gameStateMessage = BSONSerializer.serialize(new GameStateResponseMessage(this.setGameState()));
				data = BSONEncoder.encode(gameStateMessage).array();
				dp = new DatagramPacket(data, data.length);
				time = 0;			
				while(time != 3000) {
					try {
						sk.send(dp);
						System.out.println("GameState package Sent");
						Thread.sleep(500);
						time += 500;
					} 
					catch (Exception e) {   
						e.printStackTrace();
					}
				}
				this.connectionState.set(2);
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
	
	private void receiveData() {		
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
	
	private GameState setGameState() {
		SpaceshipModel shipModel = new SpaceshipModel("Ship 1", 1, 50, 30, 500, 4, 5);
		EnergyGeneratorModel generatorModel = new EnergyGeneratorModel(1, "Gen 1", 200, 10);
		EnergyGenerator generator = new EnergyGenerator(generatorModel, 200);
		EnergyShieldModels shieldModel = new EnergyShieldModels(1, "Sh 1", 500, 5, 5);
		EnergyShield shield = new EnergyShield(shieldModel, 500);
		SpaceshipEngine engine = new SpaceshipEngine(1, 50, 10, 30, "Eng 1");
		Weapon[] weapons = new Weapon[4];
		weapons[0] = new Weapon(new WeaponModel(1, "Wep 1", 60, 1, 200, 10, 10, 500, 700, 5), 3);
		weapons[1] = new Weapon(new WeaponModel(2, "Wep 2", 70, 2, 250, 9, 11, 550, 600, 6), 2);
		weapons[2] = new Weapon(new WeaponModel(3, "Wep 3", 60, 2, 220, 9, 9, 550, 750, 5), 3);
		weapons[3] = new Weapon(new WeaponModel(4, "Wep 4", 80, 3, 210, 10, 15, 300, 750, 4), 3);
		Spaceship ship = new Spaceship(1, shipModel, weapons, generator, engine, shield);
		List<PlayerInfo> players = new LinkedList<PlayerInfo>();
		players.add(new PlayerInfo((long)1, "Player 1", ship));
		
		shipModel = new SpaceshipModel("Ship 2", 2, 55, 35, 510, 5, 5);
		generatorModel = new EnergyGeneratorModel(2, "Gen 2", 210, 11);
		generator = new EnergyGenerator(generatorModel, 205);
		shieldModel = new EnergyShieldModels(2, "Sh 2", 520, 4, 4);
		shield = new EnergyShield(shieldModel, 490);
		engine = new SpaceshipEngine(2, 52, 13, 31, "Eng 2");
		weapons[0] = new Weapon(new WeaponModel(5, "Wep 5", 60, 1, 200, 10, 10, 500, 700, 5), 3);
		weapons[1] = new Weapon(new WeaponModel(6, "Wep 6", 70, 2, 250, 9, 11, 550, 600, 6), 2);
		weapons[2] = new Weapon(new WeaponModel(7, "Wep 7", 60, 2, 220, 9, 9, 550, 750, 5), 3);
		weapons[3] = new Weapon(new WeaponModel(8, "Wep 8", 80, 3, 210, 10, 15, 300, 750, 4), 3);
		ship = new Spaceship(2, shipModel, weapons, generator, engine, shield);
		players.add(new PlayerInfo((long)2, "Player 2", ship));
		
		shipModel = new SpaceshipModel("Ship 3", 3, 55, 35, 510, 5, 5);
		generatorModel = new EnergyGeneratorModel(3, "Gen 3", 210, 11);
		generator = new EnergyGenerator(generatorModel, 205);
		shieldModel = new EnergyShieldModels(3, "Sh 3", 520, 4, 4);
		shield = new EnergyShield(shieldModel, 490);
		engine = new SpaceshipEngine(3, 52, 13, 31, "Eng 3");
		weapons[0] = new Weapon(new WeaponModel(9, "Wep 9", 60, 1, 200, 10, 10, 500, 700, 5), 3);
		weapons[1] = new Weapon(new WeaponModel(10, "Wep 10", 70, 2, 250, 9, 11, 550, 600, 6), 2);
		weapons[2] = new Weapon(new WeaponModel(11, "Wep 11", 60, 2, 220, 9, 9, 550, 750, 5), 3);
		weapons[3] = new Weapon(new WeaponModel(12, "Wep 12", 80, 3, 210, 10, 15, 300, 750, 4), 3);
		ship = new Spaceship(3, shipModel, weapons, generator, engine, shield);
		players.add(new PlayerInfo((long)3, "Player 3", ship));
		
		return new GameState(players);
	}
}
