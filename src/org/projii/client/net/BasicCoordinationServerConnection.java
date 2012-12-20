package org.projii.client.net;

import org.jai.BSON.*;
import org.projii.commons.GameInfo;
import org.projii.commons.net.CoordinationServerRequests;
import org.projii.commons.net.CoordinationServerResponses;
import org.projii.commons.spaceship.Spaceship;
import org.projii.commons.spaceship.SpaceshipModel;
import org.projii.commons.spaceship.equipment.*;
import org.projii.commons.spaceship.weapon.Weapon;
import org.projii.commons.spaceship.weapon.WeaponModel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class BasicCoordinationServerConnection implements CoordinationServerConnection {

    private Socket socket;

    public BasicCoordinationServerConnection(String address, int port) {
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

//            int time = 0;
//            while (sin.available() == 0) {
//                if (time > 10000)
//                    return null;
//                Thread.sleep(1000);
//                time += 1000;
//            }

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
        request.add("type", CoordinationServerRequests.GET_MY_SHIPS_FULL);
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
        List<Spaceship> ships = new LinkedList<>();
        int count, id;

        SpaceshipModel shipModel = null;
        Weapon[] shipWeapons;
        SpaceshipEngine shipEngine = null;
        EnergyShield shipShield = null;
        EnergyGenerator shipGenerator = null;

        List<SpaceshipModel> spaceshipModels = getSpaceshipModels(response);
        List<WeaponModel> weaponModels = getWeaponModels(response);
        List<SpaceshipEngine> engineModels = getSpaceshipEngineModels(response);
        List<EnergyShieldModel> shieldModels = getEnergyShieldModels(response);
        List<EnergyGeneratorModel> generatorModels = getEnergyGeneratorModels(response);

        BSONDocument spaceships = (BSONDocument) response.get("spaceships");
        BSONDocument spaceship, weapons;
        List<Integer> weaponsId;

        for (int i = 0; i < spaceships.size(); i++) {
            spaceship = (BSONDocument) spaceships.get(i + "");
            weapons = (BSONDocument) spaceship.get("weapons");
            weaponsId = new LinkedList<>();
            for (count = 0; count < weapons.size() - 1; count++) {
                weaponsId.add((Integer) weapons.get(count + ""));
            }
            shipWeapons = new Weapon[weaponsId.size() - 1];

            for (count = 0, id = (int) spaceship.get("modelId"); count < spaceshipModels.size(); count++) {
                if (id == spaceshipModels.get(count).id) {
                    shipModel = spaceshipModels.get(count);
                    break;
                }
            }
            for (count = 0, id = (int) spaceship.get("engineId"); count < engineModels.size(); count++) {
                if (id == engineModels.get(count).getId()) {
                    shipEngine = engineModels.get(count);
                    break;
                }
            }
            for (count = 0, id = (int) spaceship.get("shieldId"); count < shieldModels.size(); count++) {
                if (id == shieldModels.get(count).id) {
                    shipShield = new EnergyShield(shieldModels.get(count));
                    break;
                }
            }
            for (count = 0, id = (int) spaceship.get("generatorId"); count < generatorModels.size(); count++) {
                if (id == generatorModels.get(count).id) {
                    shipGenerator = new EnergyGenerator(generatorModels.get(count));
                    break;
                }
            }
            for (int j = 0; j < weaponsId.size(); j++) {
                for (count = 0, id = weaponsId.get(j); count < weaponModels.size() - 1; count++) {
                    if (id == weaponModels.get(count).getId()) {
                        shipWeapons[j] = new Weapon(weaponModels.get(count));
                        break;
                    }
                }
            }

            id = (int) spaceship.get("id");
            ships.add(new Spaceship(id, shipModel, shipWeapons, shipGenerator, shipEngine, shipShield));
        }
        return ships;
    }

    private List<WeaponModel> getWeaponModels(BSONDocument response) {
        List<WeaponModel> weaponModels = new LinkedList<>();
        BSONDocument wm;
        BSONDocument weaponModelsInfo = (BSONDocument) response.get("weapons");
        for (int i = 0; i < weaponModelsInfo.size(); i++) {
            wm = (BSONDocument) weaponModelsInfo.get(i + "");
            int weaponModelId = (int) wm.get("id");
            String name = (String) wm.get("name");
            int rate = (int) wm.get("rate");
            int weaponType = (int) wm.get("type");
            int bulletSpeed = (int) wm.get("bulletSpeed");
            int damage = (int) wm.get("damage");
            int energyConsumption = (int) wm.get("energyConsumption");
            int distance = (int) wm.get("distance");
            int range = (int) wm.get("range");
            int cooldown = (int) wm.get("cooldown");
            weaponModels.add(new WeaponModel(weaponModelId, name, rate, weaponType, bulletSpeed, damage, energyConsumption,
                    distance, range, cooldown));
        }
        return weaponModels;
    }

    private List<SpaceshipModel> getSpaceshipModels(BSONDocument response) {
        List<SpaceshipModel> spaceShipModels = new LinkedList<>();
        BSONDocument spaceshipModelInfo;
        BSONDocument spaceshipModelsInfo = (BSONDocument) response.get("spaceshipModels");
        for (int i = 0; i < spaceshipModelsInfo.size(); i++) {
            spaceshipModelInfo = (BSONDocument) spaceshipModelsInfo.get(i + "");
            int spaceshipModelId = (int) spaceshipModelInfo.get("id");
            String spaceshipModelName = (String) spaceshipModelInfo.get("name");
            int health = (int) spaceshipModelInfo.get("health");
            int width = (int) spaceshipModelInfo.get("width");
            int length = (int) spaceshipModelInfo.get("length");
            int armor = (int) spaceshipModelInfo.get("armor");
            int weaponSlotCount = (int) spaceshipModelInfo.get("weaponSlotCount");
            spaceShipModels.add(new SpaceshipModel(spaceshipModelId, spaceshipModelName, length, width, health, weaponSlotCount, armor));
        }
        return spaceShipModels;
    }

    private List<SpaceshipEngine> getSpaceshipEngineModels(BSONDocument response) {
        List<SpaceshipEngine> engineModels = new LinkedList<>();
        BSONDocument engineModel;
        BSONDocument engineModelsInfo = (BSONDocument) response.get("engineModels");
        for (int i = 0; i < engineModelsInfo.size(); i++) {
            engineModel = (BSONDocument) engineModelsInfo.get(i + "");
            int engineModelId = (int) engineModel.get("id");
            int maneuverability = (int) engineModel.get("maneuverability");
            int maxSpeed = (int) engineModel.get("maxSpeed");
            int acceleration = (int) engineModel.get("acceleration");
            String engineModelName = (String) engineModel.get("name");
            engineModels.add(new SpaceshipEngine(engineModelId, maxSpeed, acceleration, maneuverability, engineModelName));
        }
        return engineModels;
    }

    private List<EnergyShieldModel> getEnergyShieldModels(BSONDocument response) {
        List<EnergyShieldModel> shieldModels = new LinkedList<>();
        BSONDocument shieldModel;
        BSONDocument shieldModelsInfo = (BSONDocument) response.get("shieldModels");
        for (int i = 0; i < shieldModelsInfo.size(); i++) {
            shieldModel = (BSONDocument) shieldModelsInfo.get(i + "");
            int shieldModelId = (int) shieldModel.get("id");
            int maxEnergyLevel = (int) shieldModel.get("maxEnergyLevel");
            int regenerationDelay = (int) shieldModel.get("regenerationDelay");
            int regenerationSpeed = (int) shieldModel.get("regenerationSpeed");
            String shieldModelName = (String) shieldModel.get("name");
            shieldModels.add(new EnergyShieldModel(shieldModelId, shieldModelName, maxEnergyLevel, regenerationSpeed, regenerationDelay));
        }
        return shieldModels;
    }

    private List<EnergyGeneratorModel> getEnergyGeneratorModels(BSONDocument response) {
        List<EnergyGeneratorModel> generatorModels = new LinkedList<>();
        BSONDocument generatorModel;
        BSONDocument generatorModelsInfo = (BSONDocument) response.get("generatorModels");
        for (int i = 0; i < generatorModelsInfo.size(); i++) {
            generatorModel = (BSONDocument) generatorModelsInfo.get(i + "");
            int modelId = (int) generatorModel.get("id");
            int maxEnergyLevel = (int) generatorModel.get("maxEnergyLevel");
            int regenerationSpeed = (int) generatorModel.get("regenerationSpeed");
            String generatorModelName = (String) generatorModel.get("name");
            generatorModels.add(new EnergyGeneratorModel(modelId, generatorModelName, maxEnergyLevel, regenerationSpeed));
        }
        return generatorModels;
    }

    public List<GameInfo> getGamesList() {
        LinkedList<GameInfo> games = null;
        BSONDocument request = new BSONDocument();
        request.add("type", CoordinationServerRequests.GET_GAMES);
        try {
            BSONDocument response = executeRequest(request);
            if (response == null || response.get("type") != CoordinationServerResponses.GAMES_INFO) {
                return null;
            }
            BSONDocument document = (BSONDocument) response.get("games");

            games = new LinkedList<>();
            for (BSONDocumentElement bsonDocumentElement : document) {
                GameInfo gameInfo = BSONSerializer.deserialize(GameInfo.class, (BSONDocument) bsonDocumentElement.getValue());
                games.add(gameInfo);
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