package org.cis1200.Snake;

import java.util.Random;
import java.awt.*;

public class Fruit extends GameObject {
    private final int[][] position;
    private final Snake snake;

    public Fruit(int col, int row, Snake snake, boolean randomizePosition) {
        super(row, col);
        this.snake = snake;
        this.position = new int[20][20];

        if (randomizePosition) {
            updatePosition();
        } else {
            position[row][col] = 1;
        }
    }

    public int[][] getPosition() {
        return position;
    }

    public void updatePosition() {
        Random random = new Random();
        int newRow, newCol;
        do {
            newRow = random.nextInt(getMaxRow());
            newCol = random.nextInt(getMaxCol());
        } while (isPositionOccupiedBySnake(newRow, newCol));

        // Reset and update
        for (int r = 0; r < getMaxRow(); r++) {
            for (int c = 0; c < getMaxCol(); c++) {
                position[r][c] = 0;
            }
        }
        position[newRow][newCol] = 1;
    }

    public Point getPoint() {
        for (int row = 0; row < getMaxRow(); row++) {
            for (int col = 0; col < getMaxCol(); col++) {
                if (position[row][col] == 1) {
                    return new Point(row, col);
                }
            }
        }
        throw new IllegalStateException("Fruit's position not found in array");
    }

    private boolean isPositionOccupiedBySnake(int row, int col) {
        for (Point point : snake.getBody()) {
            if (point.x == row && point.y == col) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Colors.FRUIT);
        int[][] fruitPosition = getPosition();
        for (int i = 0; i < fruitPosition.length; i++) {
            for (int j = 0; j < fruitPosition[i].length; j++) {
                if (fruitPosition[i][j] == 1) {
                    g.fillRect(i * getSize(), j * getSize(), getSize(), getSize());
                }
            }
        }
    }
}
