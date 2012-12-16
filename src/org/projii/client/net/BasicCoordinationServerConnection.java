package org.projii.client.net;

import org.jai.BSON.BSONDecoder;
import org.jai.BSON.BSONDocument;
import org.jai.BSON.BSONEncoder;
import org.projii.commons.GameInfo;
import org.projii.commons.net.CoordinationServerRequests;
import org.projii.commons.spaceship.Spaceship;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class BasicCoordinationServerConnection implements CoordinationServerConnection {

    private Socket socket;
    private String address;
    private int port;

    public BasicCoordinationServerConnection(String address, int port) {
        this.address = address;
        this.port = port;
        try {
            socket = new Socket(address, port);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private BSONDocument executeRequest(BSONDocument doc) {
        BSONDocument response = null;
        try {
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            sout.write(BSONEncoder.encode(doc).array());
            int read_length;
            byte buf[] = new byte[1024];
            int time = 0;
            Thread.currentThread();

            while (sin.available() == 0) {
                if (time > 10000)
                    return null;
                Thread.sleep(1000);
                time += 1000;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            do {
                read_length = sin.read(buf);
                baos.write(buf, 0, read_length);
            } while (sin.available() != 0);
            ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
            response = BSONDecoder.decode(buffer);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return response;
    }

    public boolean logIn(String name, String password) {
        boolean result = false;
        BSONDocument request = new BSONDocument();
        request.add("type", CoordinationServerRequests.AUTHORIZATION);
        request.add("login", name);
        request.add("password", password);
        try {
            BSONDocument response = executeRequest(request);
            result = response != null && (byte) response.get("result") > 0;
        } catch (Exception x) {
            x.printStackTrace();
        }
        return result;
    }

    public List<Spaceship> getMyShips() {
        List<Spaceship> ships = null;
        BSONDocument request = new BSONDocument();
        request.add("type", 2);
        try {
            BSONDocument response = executeRequest(request);
            if (response == null)
                return null;
            ships = convertShipData(response);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return ships;
    }

    private List<Spaceship> convertShipData(BSONDocument response) {
        LinkedList<Spaceship> ships = new LinkedList<Spaceship>();
//        BSONDocument ship_doc, weaponMas_doc, model_doc, generator_doc, engine_doc, shield_doc, weapon_doc, bullet_doc, location_doc;
//        Weapon[] weaponMas;
//        SpaceshipModel model;
//        EnergyGenerator generator;
//        SpaceshipEngine engine;
//        EnergyShield shield;
//        Bullet bullet;
//        Point location;
//        for (int i = 0; i < response.size() - 1; i++) {
//            ship_doc = (BSONDocument) response.get(Integer.toString(i));
//            model_doc = (BSONDocument) ship_doc.get("model");
//            generator_doc = (BSONDocument) ship_doc.get("generator");
//            engine_doc = (BSONDocument) ship_doc.get("engine");
//            shield_doc = (BSONDocument) ship_doc.get("energyShield");
//            weaponMas_doc = (BSONDocument) ship_doc.get("weapons");
//            generator = new EnergyGenerator((int) generator_doc.get("id"),
//                    (int) generator_doc.get("maxCount"),
//                    (int) generator_doc.get("regeneration"));
//            engine = new SpaceshipEngine((int) engine_doc.get("id"),
//                    (int) engine_doc.get("maxSpeed"),
//                    (int) engine_doc.get("speedUp"),
//                    (int) engine_doc.get("handleability"));
//            shield = new EnergyShield((int) shield_doc.get("id"),
//                    (int) shield_doc.get("maxCount"),
//                    (int) shield_doc.get("regeneration"),
//                    (int) shield_doc.get("time"));
//            model = new SpaceshipModel((String) model_doc.get("name"),
//                    (int) model_doc.get("id"),
//                    (int) model_doc.get("health"),
//                    (int) model_doc.get("armor"),
//                    (int) model_doc.get("weaponAmount"),
//                    (int) model_doc.get("length"),
//                    (int) model_doc.get("width"));
//            weaponMas = new Weapon[weaponMas_doc.size()];
//            for (int j = 0; j < weaponMas_doc.size(); j++) {
//                weapon_doc = (BSONDocument) weaponMas_doc.get(Integer.toString(j));
//                bullet_doc = (BSONDocument) weapon_doc.get("bullet");
//                location_doc = (BSONDocument) bullet_doc.get("location");
//                location = new Point((int) location_doc.get("x"), (int) location_doc.get("y"));
//                bullet = new Bullet((int) bullet_doc.get("id"),
//                        (int) bullet_doc.get("speed"),
//                        (int) bullet_doc.get("damage"),
//                        (int) bullet_doc.get("consumeEnergy"),
//                        (int) bullet_doc.get("distance"),
//                        (int) bullet_doc.get("range"),
//                        location,
//                        (int) bullet_doc.get("rotation"),
//                        (int) bullet_doc.get("length"),
//                        (int) bullet_doc.get("width"));
//                weaponMas[j] = new Blaster((int) weapon_doc.get("id"), bullet, (int) weapon_doc.get("rate"));
//            }
//            ships.add(new Spaceship(model, generator, engine, shield, weaponMas));
//        }
        return ships;
    }

    public List<GameInfo> getGamesList() {
        LinkedList<GameInfo> games = null;
        BSONDocument request = new BSONDocument();
        request.add("type", CoordinationServerRequests.GET_GAMES);
        try {
            BSONDocument response = executeRequest(request);
            if (response == null) {
                return null;
            }

            BSONDocument document = (BSONDocument) response.get("games");

            BSONDocument gameInfo;
            games = new LinkedList<GameInfo>();
            for (int i = 0; i < document.size(); i++) {
                gameInfo = (BSONDocument) document.get(Integer.toString(i));

                int gameId = (int) gameInfo.get("gameId");
                String serverIP = (String) gameInfo.get("serverIP");
                String mapName = (String) gameInfo.get("mapName");
                int currentPlayersAmount = (int) gameInfo.get("currentPlayersAmount");
                int maxPlayersAmount = (int) gameInfo.get("maxPlayersAmount");

                games.add(new GameInfo(gameId, serverIP, mapName, currentPlayersAmount, maxPlayersAmount));
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return games;
    }

    public void logOut() {
        BSONDocument request = new BSONDocument();
        request.add("type", CoordinationServerRequests.LOG_OUT);
        try {
            OutputStream sout = socket.getOutputStream();
            sout.write(BSONEncoder.encode(request).array());
            socket.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @Override
    public GameServerConnection joinGame(GameInfo g) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}