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

    public BasicCoordinationServerConnection(String address, Integer port) {
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
            Integer read_length;
            byte buf[] = new byte[1024];

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
            result = response != null && (Byte) response.get("result") > 0;
        } catch (Exception x) {
            x.printStackTrace();
        }
        return result;
    }

    public List<Spaceship> getMyShips() {
        List<Spaceship> ships = null;
        BSONDocument request = new BSONDocument();
        request.add("type", CoordinationServerRequests.GET_MY_SHIPS);
        try {
            BSONDocument response = executeRequest(request);
            if (response == null) {
                return null;
            }

            ships = convertShipData(response);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return ships;
    }

    private List<Spaceship> convertShipData(BSONDocument response) {
        List<Spaceship> ships = new LinkedList<Spaceship>();
        Integer count, id;

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

        for (Integer i = 0; i < spaceships.size(); i++) {
            spaceship = (BSONDocument) spaceships.get(i + "");
            weapons = (BSONDocument) spaceship.get("weapons");
            weaponsId = new LinkedList<Integer>();
            for (count = 0; count < weapons.size() - 1; count++) {
                weaponsId.add((Integer) weapons.get(count + ""));
            }
            shipWeapons = new Weapon[weaponsId.size() - 1];

            for (count = 0, id = (Integer) spaceship.get("modelId"); count < spaceshipModels.size(); count++) {
                if (id == spaceshipModels.get(count).id) {
                    shipModel = spaceshipModels.get(count);
                    break;
                }
            }
            for (count = 0, id = (Integer) spaceship.get("engineId"); count < engineModels.size(); count++) {
                if (id == engineModels.get(count).getId()) {
                    shipEngine = engineModels.get(count);
                    break;
                }
            }
            for (count = 0, id = (Integer) spaceship.get("shieldId"); count < shieldModels.size(); count++) {
                if (id == shieldModels.get(count).id) {
                    shipShield = new EnergyShield(shieldModels.get(count));
                    break;
                }
            }
            for (count = 0, id = (Integer) spaceship.get("generatorId"); count < generatorModels.size(); count++) {
                if (id == generatorModels.get(count).id) {
                    shipGenerator = new EnergyGenerator(generatorModels.get(count));
                    break;
                }
            }
            for (Integer j = 0; j < weaponsId.size(); j++) {
                for (count = 0, id = weaponsId.get(j); count < weaponModels.size() - 1; count++) {
                    if (id == weaponModels.get(count).getId()) {
                        shipWeapons[j] = new Weapon(weaponModels.get(count));
                        break;
                    }
                }
            }

            id = (Integer) spaceship.get("id");
            ships.add(new Spaceship(id, shipModel, shipWeapons, shipGenerator, shipEngine, shipShield));
        }
        return ships;
    }

    private List<WeaponModel> getWeaponModels(BSONDocument response) {
        List<WeaponModel> weaponModels = new LinkedList<WeaponModel>();
        BSONDocument wm;
        BSONDocument weaponModelsInfo = (BSONDocument) response.get("weapons");
        for (Integer i = 0; i < weaponModelsInfo.size(); i++) {
            wm = (BSONDocument) weaponModelsInfo.get(i + "");
            Integer weaponModelId = (Integer) wm.get("id");
            String name = (String) wm.get("name");
            Integer rate = (Integer) wm.get("rate");
            Integer weaponType = (Integer) wm.get("type");
            Integer bulletSpeed = (Integer) wm.get("projectileSpeed");
            Integer damage = (Integer) wm.get("damage");
            Integer energyConsumption = (Integer) wm.get("energyConsumption");
            Integer distance = (Integer) wm.get("distance");
            Integer range = (Integer) wm.get("range");
            Integer cooldown = (Integer) wm.get("cooldown");
            weaponModels.add(new WeaponModel(weaponModelId, name, rate, weaponType, bulletSpeed, damage, energyConsumption,
                    distance, range, cooldown));
        }
        return weaponModels;
    }

    private List<SpaceshipModel> getSpaceshipModels(BSONDocument response) {
        List<SpaceshipModel> spaceShipModels = new LinkedList<SpaceshipModel>();
        BSONDocument spaceshipModelInfo;
        BSONDocument spaceshipModelsInfo = (BSONDocument) response.get("spaceshipModels");
        for (Integer i = 0; i < spaceshipModelsInfo.size(); i++) {
            spaceshipModelInfo = (BSONDocument) spaceshipModelsInfo.get(i + "");
            Integer spaceshipModelId = (Integer) spaceshipModelInfo.get("id");
            String spaceshipModelName = (String) spaceshipModelInfo.get("name");
            Integer health = (Integer) spaceshipModelInfo.get("health");
            Integer width = (Integer) spaceshipModelInfo.get("width");
            Integer length = (Integer) spaceshipModelInfo.get("length");
            Integer armor = (Integer) spaceshipModelInfo.get("armor");
            Integer weaponSlotCount = (Integer) spaceshipModelInfo.get("weaponSlotCount");
            spaceShipModels.add(new SpaceshipModel(spaceshipModelId, spaceshipModelName, length, width, health, weaponSlotCount, armor));
        }
        return spaceShipModels;
    }

    private List<SpaceshipEngine> getSpaceshipEngineModels(BSONDocument response) {
        List<SpaceshipEngine> engineModels = new LinkedList<SpaceshipEngine>();
        BSONDocument engineModel;
        BSONDocument engineModelsInfo = (BSONDocument) response.get("engineModels");
        for (Integer i = 0; i < engineModelsInfo.size(); i++) {
            engineModel = (BSONDocument) engineModelsInfo.get(i + "");
            Integer engineModelId = (Integer) engineModel.get("id");
            Integer maneuverability = (Integer) engineModel.get("maneuverability");
            Integer maxSpeed = (Integer) engineModel.get("maxSpeed");
            Integer acceleration = (Integer) engineModel.get("acceleration");
            String engineModelName = (String) engineModel.get("name");
            engineModels.add(new SpaceshipEngine(engineModelId, maxSpeed, acceleration, maneuverability, engineModelName));
        }
        return engineModels;
    }

    private List<EnergyShieldModel> getEnergyShieldModels(BSONDocument response) {
        List<EnergyShieldModel> shieldModels = new LinkedList<EnergyShieldModel>();
        BSONDocument shieldModel;
        BSONDocument shieldModelsInfo = (BSONDocument) response.get("shieldModels");
        for (Integer i = 0; i < shieldModelsInfo.size(); i++) {
            shieldModel = (BSONDocument) shieldModelsInfo.get(i + "");
            Integer shieldModelId = (Integer) shieldModel.get("id");
            Integer maxEnergyLevel = (Integer) shieldModel.get("maxEnergyLevel");
            Integer regenerationDelay = (Integer) shieldModel.get("regenerationDelay");
            Integer regenerationSpeed = (Integer) shieldModel.get("regenerationSpeed");
            String shieldModelName = (String) shieldModel.get("name");
            shieldModels.add(new EnergyShieldModel(shieldModelId, shieldModelName, maxEnergyLevel, regenerationSpeed, regenerationDelay));
        }
        return shieldModels;
    }

    private List<EnergyGeneratorModel> getEnergyGeneratorModels(BSONDocument response) {
        List<EnergyGeneratorModel> generatorModels = new LinkedList<EnergyGeneratorModel>();
        BSONDocument generatorModel;
        BSONDocument generatorModelsInfo = (BSONDocument) response.get("generatorModels");
        for (Integer i = 0; i < generatorModelsInfo.size(); i++) {
            generatorModel = (BSONDocument) generatorModelsInfo.get(i + "");
            Integer modelId = (Integer) generatorModel.get("id");
            Integer maxEnergyLevel = (Integer) generatorModel.get("maxEnergyLevel");
            Integer regenerationSpeed = (Integer) generatorModel.get("regenerationSpeed");
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
            if (response == null || (Integer)response.get("type") != CoordinationServerResponses.GAMES) {
                return null;
            }
            BSONDocument document = (BSONDocument) response.get("games");

            games = new LinkedList<GameInfo>();
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
        request.add("type", CoordinationServerRequests.LOGOUT);
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
        return null;
    }

}