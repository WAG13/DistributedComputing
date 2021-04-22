package jdbc.dao;

import dao.WorldMapDao;
import entity.WorldMap;

public enum JdbcWorldMapDao implements WorldMapDao {
    INSTANCE;

    @Override
    public WorldMap getWorldMap() {
        return null;
    }

    @Override
    public void saveWorldMap(WorldMap worldMap) {

    }
}
