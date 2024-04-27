package org.cis1200.Snake;

import java.awt.*;
import java.util.LinkedList;

public class Snake extends GameObject {
    private Direction direction;

    public Snake(int row, int col) {
        super(row, col);
    }

    public Snake(LinkedList<Point> body) {
        super(body.getFirst().x, body.getFirst().y);
        setGameObjects(body);
    }

    public boolean intersects(Fruit fruit) {
        Point head = this.getBody().getFirst();
        int[][] fruitPosition = fruit.getPosition();

        if (head.x >= 0 && head.x < fruitPosition.length &&
                head.y >= 0 && head.y < fruitPosition[0].length) {
            return fruitPosition[head.x][head.y] == 1;
        }

        return false;
    }

    private Point calculateNewHead() { // Head Position, Not Body
        LinkedList<Point> snakePoints = getGameObjects();
        Point head = snakePoints.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case UP:
                newHead.translate(0, -1);
                break;
            case DOWN:
                newHead.translate(0, 1);
                break;
            case LEFT:
                newHead.translate(-1, 0);
                break;
            case RIGHT:
                newHead.translate(1, 0);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        return newHead;
    }

    public boolean hitSelf() { // Check if the snake has hit itself
        LinkedList<Point> snakePoints = getGameObjects();
        Point newHead = calculateNewHead();

        for (Point point : snakePoints) {
            if (newHead.equals(point)) {
                return true;
            }
        }
        return false;
    }

    public boolean hitWall() { // Check if the snake has hit a wall
        Point newHead = calculateNewHead();

        return newHead.x < -1 || newHead.x > getMaxCol() ||
                newHead.y < -1 || newHead.y > getMaxRow();
    }

    public void move() {
        LinkedList<Point> snakePoints = getGameObjects();
        Point newHead = calculateNewHead();

        // Add new head
        snakePoints.addFirst(newHead);
        snakePoints.removeLast();
    }

    public void grow() {
        Point tail = getGameObjects().getLast();
        Point newSegment = new Point(tail);
        getGameObjects().addLast(newSegment);
    }

    public void setDirection(Direction direction) {
        if (getGameObjects().size() > 1) {
            Point head = getGameObjects().getFirst();
            Point second = getGameObjects().get(1);

            if (second.x < head.x && direction == Direction.LEFT) {
                return;
            }
            if (second.x > head.x && direction == Direction.RIGHT) {
                return;
            }
            if (second.y < head.y && direction == Direction.UP) {
                return;
            }
            if (second.y > head.y && direction == Direction.DOWN) {
                return;
            }
        }

        this.direction = direction;
    }

    public LinkedList<Point> getBody() {
        return getGameObjects();
    }

    @Override
    public void draw(Graphics g) {
        LinkedList<Point> body = getGameObjects();
        for (int i = 0; i < body.size(); i++) {
            Point point = body.get(i);
            if (i == 0) { // head of the snake COLOR
                g.setColor(Colors.HEAD);
            } else { // body of the snake COLOR
                g.setColor(Colors.BODY);
            }
            g.fillRect(point.x * getSize(), point.y * getSize(), getSize(), getSize());
        }
    }
}