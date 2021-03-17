package knu.dc.lab6;

import java.util.Random;

public class Field {
    int width;
    int height;

    private volatile Cell[][] currentState;
    private volatile Cell[][] nextState;

    int[][] neighbourMask;


    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        currentState = new Cell[width][height];
        nextState = new Cell[width][height];

        neighbourMask = new int[][]{
                {-1, -1},
                {-1, 0},
                {-1, 1},
                {0, -1},
                {0, 1},
                {1, -1},
                {1, 0},
                {1, 1}
        };

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentState[i][j] = new Cell();
                nextState[i][j] = new Cell();
            }
        }
    }

    public void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentState[i][j].civilization = 0;
                nextState[i][j].civilization = 0;
            }
        }
    }

    public void generate(int numberOfCivilizations) {
        Random rand = new Random();
        int cellsPerCivilization = (int) (width * height * 0.5 / numberOfCivilizations);
        for (int i = 1; i <= numberOfCivilizations; i++) {
            for (int j = 0; j < cellsPerCivilization; j++) {
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);

                if (currentState[x][y].civilization == 0) {
                    currentState[x][y].civilization = i;
                }
            }
        }
    }

    public Cell getCell(int x, int y) {
        return currentState[x][y];
    }

    void updateField() {
        currentState = nextState;
        nextState = new Cell[height][width];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                nextState[i][j] = new Cell();
            }
        }
    }

    private int updateCell(int x, int y, int currentProcessingCivilization) {
        int countOfNeighbours = livingNeighbours(x, y, currentProcessingCivilization);

        if (currentState[x][y].civilization == currentProcessingCivilization) {
            if (countOfNeighbours < 2) return 0;
            else if (countOfNeighbours == 2 || countOfNeighbours == 3)
                return currentProcessingCivilization;
            else return 0;

        } else if (currentState[x][y].civilization == 0 && countOfNeighbours == 3)
            return currentProcessingCivilization;

        return nextState[x][y].civilization;
    }

    private boolean isInBounds(int x, int y) {
        return (x >= 0 && x < height && y >= 0 && y < width);
    }

    private int livingNeighbours(int x, int y, int currentProcessingCivilization) {
        int result = 0;

        for (int i = 0; i < 8; i++) {
            int neighbourX = x + neighbourMask[i][0];
            int neighbourY = y + neighbourMask[i][1];
            if (isInBounds(neighbourX, neighbourY)) {
                if (currentState[neighbourX][neighbourY].civilization == currentProcessingCivilization) {
                    result++;
                }
            }
        }
        return result;
    }

    void simulate(int numberOfCivilizations, int fromX, int toX) {
        for (int civilization = 1; civilization <= numberOfCivilizations; civilization++) {
            for (int x = fromX; x < toX; x++) {
                for (int y = 0; y < width; y++) {
                    nextState[x][y].civilization = updateCell(x, y, civilization);
                }
            }
        }
    }

}
