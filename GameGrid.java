package org.cis1200.Snake;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;
import java.io.*;

public class GameGrid extends JPanel {
    public static final int ROWS = 20;
    public static final int COLUMNS = 20;
    public static final int CELL_SIZE = 20;

    private Snake snake;
    private Fruit fruit;

    private JLabel status;

    private int score = 0;
    private int highScore = 0;

    private boolean inGame = false;

    private final Timer timer;

    private GameObject[][] grid = new GameObject[ROWS][COLUMNS];

    public GameGrid(JLabel statusInit) {
        this.status = statusInit;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        initializeGrid();
        setPreferredSize(new Dimension(COLUMNS * CELL_SIZE, ROWS * CELL_SIZE));
        setFocusable(true);
        requestFocusInWindow();

        // Reading Best Score - Creates the file if doesn't exist with '0'
        File file = new File("files/highScore.txt");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("0");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Read Values
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            highScore = Integer.parseInt(reader.readLine().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If it exists, read the file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            highScore = Integer.parseInt(reader.readLine().trim());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize snake
        this.snake = new Snake(0, 10);
        this.fruit = new Fruit(randomRow(), randomCol(), snake, false);

        // GAME START!!!
        timer = new Timer(120, e -> {
            if (!inGame) {
                status.setText("GAME PAUSED");
            }
            if (inGame) {
                snake.move();
                status.setText("SCORE: " + score + " | HIGH: " + highScore);

                // snake collision
                if (snake.hitWall() || snake.hitSelf()) {
                    endGame();
                }

                // snake eats fruit
                if (snake.intersects(fruit)) {
                    score++;
                    status.setText("SCORE: " + score + " | HIGH: " + highScore);
                    snake.grow();
                    fruit.updatePosition();
                }
            }
            repaint();
        });

        // Keyboard Inputs
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                // Start game upon keypress
                if (!inGame && (e.getKeyCode() == KeyEvent.VK_LEFT ||
                        e.getKeyCode() == KeyEvent.VK_RIGHT ||
                        e.getKeyCode() == KeyEvent.VK_DOWN ||
                        e.getKeyCode() == KeyEvent.VK_UP)) {
                    inGame = true;
                }

                // Movements
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    GameGrid.this.snake.setDirection(Direction.LEFT);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    GameGrid.this.snake.setDirection(Direction.RIGHT);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    GameGrid.this.snake.setDirection(Direction.DOWN);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    GameGrid.this.snake.setDirection(Direction.UP);
                }
            }
        });
    }

    public void reset() {
        timer.stop();
        snake = new Snake(0, 10);
        fruit = new Fruit(randomRow(), randomCol(), snake, false);
        score = 0;
        inGame = false;
        repaint();
        requestFocusInWindow();
        timer.start();
    }

    public void endGame() {
        timer.stop();
        inGame = false;
        status.setText("DEATH COMES FOR US ALL...");
        repaint();
        requestFocusInWindow();

        // Highscore checker
        if (score > highScore) {
            highScore = score;

            // Save
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter("files/highScore.txt")
            )) {
                writer.write(String.valueOf(highScore));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // You Died Screen
        String message = "You died with a score of " + score + ". ";
        if (score < highScore) {
            message += "You did not beat the highscore of " + highScore + ".";
        } else if (score == highScore) {
            message += "That's the same as the highscore!";
        } else { // score > highScore
            message += "You beat the highscore! The new highscore is " + highScore + ".";
        }
        message += "\n\nWould you like to quit the game or restart?";
        Object[] options = { "Restart", "Quit" };
        int response = JOptionPane.showOptionDialog(
                null, message, "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        // Restart Game or Quit
        if (response == JOptionPane.YES_OPTION) {
            reset();
        } else {
            System.exit(0);
        }
    }

    public void saveGame() {
        timer.stop();
        inGame = false;
        status.setText("GAME SAVED... PRESS ARROW KEYS TO CONTINUE PLAYING");
        repaint();
        requestFocusInWindow();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("files/saveGame.txt"))) {
            // Save score
            writer.write(String.valueOf(score));
            writer.newLine();

            // Save snake's body positions
            for (Point point : snake.getBody()) {
                writer.write(point.x + "," + point.y);
                writer.newLine();
            }

            // fruit position
            Point fruitPosition = fruit.getPoint();
            writer.write(fruitPosition.x + "," + fruitPosition.y);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN ||
                        e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    inGame = true;
                    timer.start();
                    removeKeyListener(this);
                }
            }
        });
    }

    public void loadGame() {
        timer.stop();
        inGame = false;
        status.setText("GAME LOADED... PRESS ARROW KEYS TO CONTINUE PLAYING");
        repaint();
        requestFocusInWindow();

        try (BufferedReader reader = new BufferedReader(new FileReader("files/saveGame.txt"))) {

            // Load score
            String scoreLine = reader.readLine();
            if (scoreLine != null) {
                score = Integer.parseInt(scoreLine.trim());
            } else {
                score = 0;
            }

            // Load snake's body
            LinkedList<Point> body = new LinkedList<>();
            for (int i = 0; i <= score; i++) {
                String line = reader.readLine();
                if (line != null && !line.isEmpty()) {
                    String[] parts = line.split(",");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    body.add(new Point(x, y));
                }
            }

            // new snake with positions
            if (!body.isEmpty()) {
                snake = new Snake(body);
            }

            // fruit position
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[0]);
                Point fruitPosition = new Point(x, y);

                // Create new fruit with loaded position
                fruit = null;
                fruit = new Fruit(fruitPosition.x, fruitPosition.y, snake, false);
            }

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN ||
                            e.getKeyCode() == KeyEvent.VK_LEFT
                            || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        inGame = true;
                        timer.start();
                        removeKeyListener(this);
                    }
                }
            });

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "No previously saved game state found", "Error", JOptionPane.ERROR_MESSAGE
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void instructions() {
        timer.stop();
        inGame = false;
        status.setText("GAME PAUSED.");
        repaint();
        requestFocusInWindow();

        // Display instructions
        String message = """
                This is a simple game of Snake!
                Use the arrow keys to move the snake.
                Eat the fruits to grow the snake.
                Avoid hitting the walls or the snake itself, otherwise the game resets.
                Click reset to manually restart and play again.
                Click Save to save the game state. Click Load to load that game state.
                To play, click OK and have fun!""";
        JOptionPane.showMessageDialog(
                null, message,
                "Instructions", JOptionPane.INFORMATION_MESSAGE
        );

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN ||
                        e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    inGame = true;
                    timer.start();
                    removeKeyListener(this);
                }
            }
        });
    }

    private int randomRow() {
        return new Random().nextInt(ROWS);
    }

    private int randomCol() {
        return new Random().nextInt(COLUMNS);
    }

    private void initializeGrid() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                grid[i][j] = null;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                GameObject cell = grid[i][j];
                if (cell != null) {
                    cell.draw(g);
                } else {
                    if ((i + j) % 2 == 0) {
                        g.setColor(Colors.GRASS1);
                    } else {
                        g.setColor(Colors.GRASS2);
                    }
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // Draw the snake and fruit
        snake.draw(g);
        fruit.draw(g);
    }
}
