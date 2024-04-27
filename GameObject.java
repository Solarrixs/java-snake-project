package org.cis1200.Snake;

import java.awt.*;
import java.util.LinkedList;

public abstract class GameObject {
    private final int size = 20;
    private final int maxRow = 20;
    private final int maxCol = 20;

    private LinkedList<Point> gameObjects;

    public GameObject(int col, int row) {
        this.gameObjects = new LinkedList<>();
        this.gameObjects.add(new Point(col, row));
    }

    // Getters
    public LinkedList<Point> getGameObjects() {
        return gameObjects;
    }

    public int getSize() {
        return size;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public int getMaxCol() {
        return maxCol;
    }

    // Setters
    public void setGameObjects(LinkedList<Point> gameObjects) {
        this.gameObjects = gameObjects;
    }

    // Draw
    public void draw(Graphics g) {
    }
}
