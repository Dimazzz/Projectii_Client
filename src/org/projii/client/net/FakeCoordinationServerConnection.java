package org.projii.client.net;

import org.projii.commons.GameInfo;
import org.projii.commons.spaceship.Spaceship;
import org.projii.commons.spaceship.SpaceshipModel;
import org.projii.commons.spaceship.equipment.*;

import java.util.ArrayList;
import java.util.List;

public class FakeCoordinationServerConnection implements CoordinationServerConnection {
    @Override
    public boolean logIn(String login, String password) {
        return login != null &&
                password != null &&
                login.equals("lamer@lala.la") &&
                password.equals("12345");
    }

    @Override
    public List<Spaceship> getMyShips() {
        List<Spaceship> spaceshipList = new ArrayList<>();
        SpaceshipModel modelA = new SpaceshipModel("LolModelA", 0, 100, 100, 100, 3, 100);
        SpaceshipModel modelB = new SpaceshipModel("LolModelB", 0, 100, 100, 100, 3, 100);

        EnergyGenerator generator = new EnergyGenerator(new EnergyGeneratorModel(0, "GeneratorA", 100, 100));
        EnergyShield shield = new EnergyShield(new EnergyShieldModels(0, "ShieldA", 100, 100, 100));
        SpaceshipEngine engineA = new SpaceshipEngine(0, 100, 100, 100, "EngineA");
        SpaceshipEngine engineB = new SpaceshipEngine(0, 100, 100, 100, "EngineA");

        spaceshipList.add(new Spaceship(0, modelA, null, generator, engineA, shield));
        spaceshipList.add(new Spaceship(1, modelB, null, generator, engineB, shield));

        return spaceshipList;
    }

    @Override
    public List<GameInfo> getGamesList() {
        List<GameInfo> gameInfoList = new ArrayList<>();
        gameInfoList.add(new GameInfo(0, "localhost", "map0", 0, 10));
        gameInfoList.add(new GameInfo(1, "::", "map42", 5, 5));
        gameInfoList.add(new GameInfo(2, "127.0.0.1", "map123", 0, 10));
        return gameInfoList;
    }

    @Override
    public void logOut() {
    }

    @Override
    public GameServerConnection joinGame(GameInfo g) {
        return null;
    }
}
