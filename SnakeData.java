import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * A class representing a snake object in a game of snake. The location
 * of the snake is stored as an ArrayList of Coordinates. The snake has a
 * color, a direction (which can be changed by a keypress, handled by the
 * event handler))
 * 
 */

public class SnakeData {


    //------------------ private instance variables --------------------
     

    private int rows; // The number of rows of rectangles in the grid.
    private int columns; // The number of columns of rectangles in the grid.

    private Color color; // Color of the snake.

    private ArrayList<Coordinate> location; // An arraylist containing the coordinates
                                            // of the snake's location. The head of the
                                            // snake is at position 0, and the tail of
                                            // the snake is the final position in the list.

    private int dir; // The direction that the snake is moving. 0 = up, 1 =
                     // right, 2 = down, 3 = left.


    /**
     * Construct a Snake by automtically adding four Coordinates
     * to the snakes location at the center of the board.
     */
    public SnakeData(int rows, int columns, Color color) {
        this.rows = rows;
        this.columns = columns;
        this.color = color;
        dir = 0;
        location = new ArrayList<Coordinate>();
        location.add(new Coordinate(rows/2, columns/2 - 1));
        location.add(new Coordinate(rows/2, columns/2));
        location.add(new Coordinate(rows/2, columns/2 + 1));
        location.add(new Coordinate(rows/2, columns/2 + 2));
    }


    //--------- methods for getting and setting snake properties ----------


    /**
     * Return the number of rows of rectangles in the grid.
     */
    public int getRowCount() {
        return rows;
    }


    /**
     * Return the number of columns of rectangles in the grid.
     */
    public int getColumnCount() {
        return columns;
    }


    /**
     * Return the location of the snake as an ArrayList of Coordinates.
     */
    public ArrayList<Coordinate> getLocation() {
        return location;
    }


    /**
     * Return the color of the snake.
     */
    public Color getColor() {
        return color;
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
     * moving.
     */
    public void update() {

        switch (dir) {
            case 0 -> { // snake is moving up
                for (int i = location.size() - 1 ; i >= 0 ; i--) {
                    if (i == 0) { 
                        location.get(i).setY(location.get(i).getY()-1);
                    }
                    else {
                        location.get(i).setX(location.get(i-1).getX());
                        location.get(i).setY(location.get(i-1).getY());
                    }
                }
            }
            case 1 -> { // snake is moving right
                for (int i = location.size() - 1 ; i >= 0 ; i--) {
                    if (i == 0) { 
                        location.get(i).setX(location.get(i).getX()+1);
                    }
                    else {
                        location.get(i).setX(location.get(i-1).getX());
                        location.get(i).setY(location.get(i-1).getY());
                    }
                }
            }
            case 2 -> { // snake is moving down
                for (int i = location.size() - 1 ; i >= 0 ; i--) {
                    if (i == 0) { 
                        location.get(i).setY(location.get(i).getY()+1);
                    }
                    else {
                        location.get(i).setX(location.get(i-1).getX());
                        location.get(i).setY(location.get(i-1).getY());
                    }
                }
            }
            default -> { // snake is moving left
                for (int i = location.size() - 1 ; i >= 0 ; i--) {
                    if (i == 0) {
                        location.get(i).setX(location.get(i).getX()-1);
                    }
                    else {
                        location.get(i).setX(location.get(i-1).getX());
                        location.get(i).setY(location.get(i-1).getY());
                    }
                }
            }
        }
    }


    /**
     * When the snake eats a food, a new Coordinate is added to the snake's location.
     * It is added to the same location as the last piece of the snake. This is OK,
     * because it will be updated to the correct location when the snake moves.
     */
    public void eat() {
        location.add(new Coordinate(location.get(location.size() - 1).getX(), location.get(location.size() - 1).getY()));
    }

} // end of class SnakeData