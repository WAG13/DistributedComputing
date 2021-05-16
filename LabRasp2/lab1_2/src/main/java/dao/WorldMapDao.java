package dao;

import entity.WorldMap;

public interface WorldMapDao {

    WorldMap getWorldMap();

    void saveWorldMap(WorldMap worldMap);
}
