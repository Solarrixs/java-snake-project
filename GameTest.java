package org.cis1200.Snake;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {
    void helperFunction(Snake snake, Fruit fruit) {
        int[][] fruitPosition = fruit.getPosition();
        System.out.println("Fruit Position Grid:");
        for (int[] row : fruitPosition) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }

        int[][] snakePosition = new int[20][20];
        Point head = snake.getBody().getFirst();
        snakePosition[head.y][head.x] = 2;

        // Display the snake position grid
        System.out.println("Snake Position Grid:");
        for (int[] row : snakePosition) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    void snakeHelperFunction(Snake snake) {
        int[][] snakePosition = new int[20][20];
        Point head = snake.getBody().getFirst();
        snakePosition[head.y][head.x] = 2;

        // Display the snake position grid
        System.out.println("Snake Position Grid:");
        for (int[] row : snakePosition) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    @Test
    void testSnakeMovementInAllDirections() {
        // Initialize the snake
        Snake snake = new Snake(5, 5);

        // Move right and check position
        snake.setDirection(Direction.RIGHT);
        snake.move();
        assertEquals(new Point(6, 5), snake.getBody().getFirst(), "(6, 5) right");

        // Move down and check position
        snake.setDirection(Direction.DOWN);
        snake.move();
        assertEquals(new Point(6, 6), snake.getBody().getFirst(), "(6, 6) down");

        // Move left and check position
        snake.setDirection(Direction.LEFT);
        snake.move();
        assertEquals(new Point(5, 6), snake.getBody().getFirst(), "(5, 6) left");

        // Move up and check position
        snake.setDirection(Direction.UP);
        snake.move();
        assertEquals(new Point(5, 5), snake.getBody().getFirst(), "(5, 5) up");
    }

    @Test
    void testIntersectsWhenFruitIsAtHead() {
        // Initialize snake and fruit at the same position
        Snake snake = new Snake(0, 0);
        Fruit fruit = new Fruit(0, 0, snake, false);

        // Check initial intersection
        assertTrue(snake.intersects(fruit));
        helperFunction(snake, fruit);

        // Move the snake to the right
        snake.setDirection(Direction.RIGHT);
        snake.move();
        helperFunction(snake, fruit);
        // Check that they no longer intersect after the snake moves
        assertFalse(snake.intersects(fruit));
    }

    @Test
    void testIntersectsAfterMoveToEatFruit2() {
        Snake snake = new Snake(0, 0);
        Fruit fruit = new Fruit(1, 1, snake, false);

        helperFunction(snake, fruit);
        snake.setDirection(Direction.RIGHT);
        snake.move();
        System.out.println("Snake Head Position: " + snake.getBody().getFirst());
        System.out.println("1,0");
        snake.setDirection(Direction.DOWN);
        snake.move();
        System.out.println("Snake Head Position: " + snake.getBody().getFirst());
        System.out.println("1,1");
        assertTrue(snake.intersects(fruit));
    }

    @Test
    void testGetBody() {
        Snake snake = new Snake(0, 0);
        LinkedList<Point> body = snake.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(new Point(0, 0), body.getFirst());
    }

    @Test
    void testDisplayPositions() {
        // Initialize snake and fruit
        Snake snake = new Snake(0, 0);
        Fruit fruit = new Fruit(0, 0, snake, false);

        // Retrieve positions
        Point head = snake.getBody().getFirst();
        int[][] fruitPosition = fruit.getPosition();

        // Display the fruit position grid and head position
        System.out.println("Fruit Position Grid:");
        for (int[] ints : fruitPosition) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
        System.out.println("Snake Head Position: " + head);
        boolean intersects = snake.intersects(fruit);
        assertTrue(intersects);
    }

    @Test
    void testGetFirst() {
        Snake snake = new Snake(0, 0);
        Point head = snake.getBody().getFirst();
        assertNotNull(head);
        assertEquals(new Point(0, 0), head);
    }

    @Test
    void testGetFirstAfterMove() {
        Snake snake = new Snake(0, 0);
        snake.setDirection(Direction.RIGHT);
        snake.move();
        Point head = snake.getBody().getFirst();
        assertNotNull(head);
        assertEquals(new Point(1, 0), head);
    }

    @Test
    void testSnakeMovement() {
        Snake snake = new Snake(5, 5);
        Point initialHeadPosition = new Point(5, 5);
        assertEquals(initialHeadPosition, snake.getBody().getFirst());

        snake.setDirection(Direction.RIGHT);
        snake.move();
        Point expectedPositionAfterRightMove = new Point(6, 5);
        assertEquals(expectedPositionAfterRightMove, snake.getBody().getFirst());
    }

    @Test
    void testIntersectsWhenFruitIsNotAtHead() {
        Snake snake = new Snake(0, 0);
        Fruit fruit = new Fruit(1, 0, snake, false);

        assertFalse(snake.intersects(fruit));
    }

    @Test
    void testSnakeGrow() {
        Snake snake = new Snake(0, 0);
        int initialLength = snake.getBody().size();
        int initialLength2 = snake.getGameObjects().size();
        snake.grow();
        assertEquals(initialLength + 1, snake.getBody().size());
        assertEquals(initialLength2 + 1, snake.getGameObjects().size());
    }

    @Test
    void testUpdatePosition() {
        Snake snake = new Snake(0, 0);
        Fruit fruit = new Fruit(0, 0, snake, false);
        Point oldPosition = fruit.getPoint();
        fruit.updatePosition();
        Point newPosition = fruit.getPoint();
        assertNotEquals(oldPosition, newPosition);
        assertTrue(newPosition.x >= 0 && newPosition.x < 20);
        assertTrue(newPosition.y >= 0 && newPosition.y < 20);
    }

    @Test
    void testFruitDoesNotSpawnOnSnake() {
        Snake snake = new Snake(0, 0);
        Fruit fruit = new Fruit(1, 0, snake, false);
        for (int i = 0; i < 1000; i++) { // 1000 Respawns lol
            fruit.updatePosition();
            Point fruitPosition = fruit.getPoint();
            assertFalse(snake.getGameObjects().contains(fruitPosition));
        }
    }

    @Test
    void testFruitAllPositionAndPointsMethods() {
        Snake snake = new Snake(0, 0);
        Fruit fruit = new Fruit(1, 0, snake, false);

        int[][] initialPositionGrid = fruit.getPosition();
        Point initialFruitPoint = fruit.getPoint();
        assertEquals(1, initialPositionGrid[initialFruitPoint.x][initialFruitPoint.y]);

        fruit.updatePosition();
        int[][] updatedPositionGrid = fruit.getPosition();
        Point updatedFruitPoint = fruit.getPoint();
        assertEquals(1, updatedPositionGrid[updatedFruitPoint.x][updatedFruitPoint.y]);

        assertNotEquals(initialFruitPoint, updatedFruitPoint);
    }

    @Test
    void testSnakeConstructorWithBody() {
        LinkedList<Point> body = new LinkedList<>();
        body.add(new Point(5, 5));
        body.add(new Point(6, 5));
        body.add(new Point(7, 5));

        Snake snake = new Snake(body);

        assertEquals(new Point(5, 5), snake.getBody().getFirst());

        assertEquals(body, snake.getBody());
    }

    @Test
    void testSnakeConstructorWithEmptyBody() {
        LinkedList<Point> body = new LinkedList<>();

        assertThrows(NoSuchElementException.class, () -> new Snake(body));
    }

    @Test
    void testSnakeConstructorWithNullBody() {
        assertThrows(NullPointerException.class, () -> new Snake(null));
    }

    @Test
    void testGetBodyWhenSnakeSizeIsOne() {
        Snake snake = new Snake(5, 5);
        LinkedList<Point> body = snake.getBody();

        assertNotNull(body, "Body should not be null");
        assertEquals(1, body.size());
        assertEquals(new Point(5, 5), body.getFirst());
    }

    @Test
    void testGetBodyWhenSnakeSizeIsMoreThanOne() {
        LinkedList<Point> body = new LinkedList<>();
        body.add(new Point(5, 5));
        body.add(new Point(6, 5));
        body.add(new Point(7, 5));

        Snake snake = new Snake(body);
        LinkedList<Point> returnedBody = snake.getBody();

        assertNotNull(returnedBody, "Body should not be null");
        assertEquals(3, returnedBody.size());
        assertEquals(new Point(5, 5), returnedBody.getFirst());
        assertEquals(new Point(7, 5), returnedBody.getLast());
    }
}
