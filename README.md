# Penn CIS 1200 Game Project

## Core Concepts Implemented
  1. 2D Arrays
  2. File I/O
  3. Inheritance/Subtyping
  4. JUnit Tests

## Classes
Colors - This class is used to store the colors of the game. It is used in the
Fruit, Snake, and GameGrid classes. It makes the code cleaner since I used custom colors
and didn't want to display the RGB values every time I wanted to use a color. No function
besides making the code cleaner and having nice colors for the game.

Direction - This class just stores the 4 possible directions the snake can move in. Used in
Snake and GameGrid.

Fruit - This class is used to create the fruit that the snake eats to grow. It extends the
GameObject class. The fruit constructor initializes the fruit's position either randomly or
at a specific location (for testing purposes). It also has a draw method that draws the fruit. It
also ensures that the Fruit can't be initialized on top of the snake.

GameGrid - This class is used to create the grid that the game is played on. It is also the
controller for the game, because it controls the Snake game logic, how the snake/fruit is
displayed, and the keyboard presses. It has methods for gamelogic stuff like resetting the game,
ending the game, saving the game, loading the game, displaying instructions, etc.

GameObject - Abstract class for the Fruit and Snake classes. Initializes a game object, which
is either a fruit or snake at a certain position and has methods for getting the position and
setting the position and similar stuff to that.

Snake - Creates the snake that the player controls. It extends the GameObject.
class. The snake constructor initializes the snake's position and direction. It also has a draw
method that draws the snake. It also has methods for moving the snake, growing the snake, and
checking if the snake is dead. It has all the logic that the snake requires for collisions
and movement restrictions.

RunSnake - Sets up GUI and game with the different Java Swing components. It creates the buttons
like Help or Load or Save games, which is handled by the GameGrid class.
