import java.util.ArrayList;

/**
 * A class representing a Snake object in a game of snake. The location of the
 * snake is stored as an ArrayList of SnakaData objects. The snake has a direction
 * (which can be changed by a keypress, handled by the event handler).
 */

public class Snake {


    //------------------ private instance variables --------------------


    private ArrayList<SnakeData> snakeArray; // An arraylist containing the SnakeData objects
                                             //    of the snake. The head of the snake is at
                                             //    position 0, and the tail of the snake is the
                                             //    final position in the list.

    private int dir; // The direction that the snake is moving. 0 = UP, 1 = RIGHT,
                     //    2 = DOWN, 3 = LEFT.


    /**
     * Construct a Snake by automtically adding four Coordinates
     * to the snakes location at the bottom of the board. The snake
     * always begins moving in an UP direction.
     * @param rows the number of rows in the grid
     * @param columns the number of columns in the grid
     */
    public Snake(int rows, int columns) {
        dir = 0;
        snakeArray = new ArrayList<SnakeData>();
        snakeArray.add(new SnakeData(columns/2, rows - 4));
        snakeArray.add(new SnakeData(columns/2, rows - 3));
        snakeArray.add(new SnakeData(columns/2, rows - 2));
        snakeArray.add(new SnakeData(columns/2, rows - 1));
    }


    //--------- methods for getting and setting Snake properties ----------


    /**
     * Return the location of the snake as an ArrayList of SnakeDatas.
     */
    public ArrayList<SnakeData> getSnakeArray() {
        return snakeArray;
    }


    //------------------ other useful public methods ---------------------


    /**
     * Change the direction of the snake.
     */
    public void changeDir(int dir) {
        this.dir = dir;
    }


    /**
     * Update the snake's location according to the direction it is currently
     * moving. This routine sets the x and y component of every SnakaData element
     * in the snake's location to the next SnakeData's x and y components, and
     * moved the first SnakeData element 1 square in the direction of movement. It
     * does not check to see whether the move is valid (that is an obligation of
     * the caller).
     */
    public void update() {

        switch (dir) {
            case 0 -> { // snake is moving up
                for (int i = snakeArray.size() - 1 ; i >= 0 ; i--) {
                    if (i == 0) { 
                        snakeArray.get(i).setY(snakeArray.get(i).getY()-1);
                    }
                    else {
                        snakeArray.get(i).setX(snakeArray.get(i-1).getX());
                        snakeArray.get(i).setY(snakeArray.get(i-1).getY());
                    }
                }
            }
            case 1 -> { // snake is moving right
                for (int i = snakeArray.size() - 1 ; i >= 0 ; i--) {
                    if (i == 0) { 
                        snakeArray.get(i).setX(snakeArray.get(i).getX()+1);
                    }
                    else {
                        snakeArray.get(i).setX(snakeArray.get(i-1).getX());
                        snakeArray.get(i).setY(snakeArray.get(i-1).getY());
                    }
                }
            }
            case 2 -> { // snake is moving down
                for (int i = snakeArray.size() - 1 ; i >= 0 ; i--) {
                    if (i == 0) { 
                        snakeArray.get(i).setY(snakeArray.get(i).getY()+1);
                    }
                    else {
                        snakeArray.get(i).setX(snakeArray.get(i-1).getX());
                        snakeArray.get(i).setY(snakeArray.get(i-1).getY());
                    }
                }
            }
            default -> { // snake is moving left
                for (int i = snakeArray.size() - 1 ; i >= 0 ; i--) {
                    if (i == 0) {
                        snakeArray.get(i).setX(snakeArray.get(i).getX()-1);
                    }
                    else {
                        snakeArray.get(i).setX(snakeArray.get(i-1).getX());
                        snakeArray.get(i).setY(snakeArray.get(i-1).getY());
                    }
                }
            }
        }
    }


    /**
     * When the snake eats a food, a new SnakeData is added to the snake's location.
     * It is added to the same location as the last piece of the snake. This is OK,
     * because it will be updated to the correct location when the snake moves.
     */
    public void eat() {
        Data last = snakeArray.get(snakeArray.size() - 1); // find the last SnakeData in the snake
        snakeArray.add(new SnakeData(last.getX(), last.getY()));
    }


} // end of class Snake