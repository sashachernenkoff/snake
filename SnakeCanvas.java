import javafx.application.Platform;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *  A SnakeCanvas object represents a grid containing rows
 *  and columns of colored rectangles. Methods are provided
 * for getting and setting the colors of the rectangles.
 */
public class SnakeCanvas extends Canvas {


    //------------------ private instance variables --------------------


    private int rows, columns;       // The number of rows and columns of rectangles in the grid.

    private int[][] grid; // An array that contains the status of the game grid. If
                          //    the snake occupies a space in the grid, the value is
                          //    set to 1. If an apple occupies the grid, the value
                          //    is set to 2. Else, the value is set to 0.

    private Color backgroundColor = Color.rgb(40, 40, 40); // The color of the background.
    private Color snakeColor = Color.OLIVE; // The starting color of the snake.
    private Color foodColor = Color.SIENNA; // The starting color of the food.

    private int score; // The score of the game.
    private boolean playing; // True if game is in progress, false otherwise.

    private SnakeData snake; // The snake on the board.

    private ArrayList<FoodData> foodList; // A list of all food items on the board.

    private GraphicsContext g; // The graphics context for drawing on this canvas.


    //------------------------ constructors -----------------------------


    /**
     *  Construct a SnakeCanvas with 42 rows and 42 columns of rectangles,
     *  and with preferred rectangle height and width both set to 16.
     */
    public SnakeCanvas() {
        this(30,30);
    }

    /**
     *  Construct a SnakeCanvas with specified numbers of rows and columns of rectangles,
     *  and with preferred rectangle height and width both set to 16.
     */
    public SnakeCanvas(int rows, int columns) {
        this(rows,columns,16,16);
    }


    /**
     * Construct a SnakeCanvas with the specified number of rows and columns of rectangles,
     * and with a specified preferred size for the rectangle. The default rectangle color
     * is black.
     * @param rows the canva will have this many rows of rectangles. This must be a
     *              positive number.
     * @param columns the canvas will have this many columns of rectangles. This must be a
     *              positive number.
     * @param preferredBlockWidth the preferred width of the canvas will be set to this value
     *              times the number of columns. The actual width is set by the component that
     *              contains the canvas, and so might not be equal to the preferred width. Size
     *              is measured in pixels. The value should not be less than about 5, and any
     *              smaller value will be increased to 5.
     * @param preferredBlockHeight the preferred height of the canvas will be set to this value
     *              times the number of rows.  The actual height is set by the component that
     *              contains the canvas, and so might not be equal to the preferred height. Size
     *              is measured in pixels. The value should not be less than about 5, and any
     *              smaller value will be increased to 5.
     */
    public SnakeCanvas(int rows, int columns, int preferredBlockWidth, int preferredBlockHeight) {
        
        this.rows = rows;
        this.columns = columns;
        if (rows <= 0 || columns <= 0)
            throw new IllegalArgumentException("Rows and Columns must be greater than zero.");
        preferredBlockHeight = Math.max( preferredBlockHeight, 5);
        preferredBlockWidth = Math.max( preferredBlockWidth, 5);
        playing = true;
        grid = new int[rows][columns];
        snake = new SnakeData(rows, columns, snakeColor);
        foodList = new ArrayList<FoodData>();
        addFood();
        setWidth(preferredBlockWidth*columns);
        setHeight(preferredBlockHeight*rows);
        g = getGraphicsContext2D();
    }


    //--------- methods for getting and setting grid properties ----------


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
     * Return the value of an element in the grid.
     */
    public int getGridValue(int row, int column) {
        return grid[row][column];
    }


    /**
     * Set the value of an element in the grid.
     */
    public void setGridValue(int row, int column, int val) {
        grid[row][column] = val;
    } 

    /**
     * Get the value of playing.
     */
    public boolean getPlaying() {
        return playing;
    }

    /**
     * Set the value of playing.
     */
    public void setPlaying(boolean b) {
        playing = b;
    }


    //------------------ other useful public methods ---------------------


    /**
     * Update the grid according to the snake's newest location and any
     * food on the board and redraw the board. This routine returns a boolean
     * which represents if the game is ongoing or has ended.
     */
    public void updateGrid() {

        // Move snake to it's next location.
        snake.update();

        // Check to see if the snake hit itself (the snake hit itself if the
        // first Coordinate in the snake is the same as any other Coordinate
        // in the snake.
        for (int i = 1 ; i < snake.getLocation().size() ; i++) {
            if (snake.getLocation().get(i).equals(snake.getLocation().get(0))) {
                playing = false;
                return;
            }
        }

        // Check to see if the snake hit a wall
        if (snake.getLocation().get(0).getY() < 0
                || snake.getLocation().get(0).getY() >= rows
                || snake.getLocation().get(0).getX() >= columns
                || snake.getLocation().get(0).getX() < 0) { // going off the board, game over
                    playing = false;
                    return;
        }

        // Check to see if the snake hit a food item
        if (!foodList.isEmpty()) {
            for (int i = 0 ; i < foodList.size() ; i++) {
                
                if (foodList.get(i).getLocation().equals(snake.getLocation().get(0))) { // the snake ate the food
                    foodList.remove(foodList.get(i));
                    snake.eat();
                    score += 10;

                    // to prevent game from getting stale, always endure there is
                    // at least one food on the board
                    if (foodList.isEmpty()) {
                        addFood();
                    }

                    break;
                }
            }
        }

        // Reset the grid
        for (int i = 0; i < grid[0].length; i++) {
            Arrays.fill(grid[i], 0);
        }

        // Update grid with the food locations
        if (!foodList.isEmpty()) {
            for (FoodData f : foodList) {
                grid[f.getLocation().getY()][f.getLocation().getX()] = 2;
            }
        }

        // Update the grid with the snake's new location
        for (Coordinate c : snake.getLocation()) {
            grid[c.getY()][c.getX()] = 1;
        }

        drawBoard();
    }


    /**
     * Reset the entire grid, snake, and any food. This is called when a game is restarted
     * and the player is starting from scratch.
     */
    public void resetGrid() {

        grid = new int[rows][columns];
        snake = new SnakeData(rows, columns, snakeColor);
        score = 0;
        foodList = new ArrayList<FoodData>();
        addFood();

        playing = true;
        drawBoard();
    }


    /**
     * Draws the GAME OVER screen.
     */
    public void gameOver() {
        drawGameOver();
    }


    /**
     * This method can be called to force redrawing of the entire canvas. This is only
     * called when the game first launches. All other redraws only happen from main().
     */
    final public void forceRedraw() {
        drawBoard();
    }


    /**
     * Pass a move from the event handler to the snake
     */
    public void moveSnake(int dir) {
        snake.changeDir(dir);
    }


    /**
     * Add a FoodData to the board, ensuring that the food is not placed
     * on a location that the snake currently occupies.
     */
    public void addFood() {

        FoodData newFood;
        boolean inList;

        do {
            newFood = new FoodData((int)(Math.random()*rows), (int)(Math.random()*columns), foodColor);
            inList = isInFoodList(newFood);
        } while (inList);
        
        foodList.add(newFood);
    }

    /**
     * Checks to see if a FoodData with a set of Coordinates already
     * exists in the food list.
     */
    public boolean isInFoodList(FoodData food) {
        
        boolean inList = false;

        if (!foodList.isEmpty()) {
            for (FoodData f : foodList) {
                if (f.getLocation().equals(food.getLocation()))
                    inList = true;
            }
        }

        return inList;
    }


    // private implementation section -- the only part that actually draws squares

    
    private void drawBoard() {
        if (Platform.isFxApplicationThread()) {
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < columns; c++)
                    drawOneSquare(r,c);
                g.setFill(Color.WHITE);
                g.setFont(Font.font ("Courier New", FontWeight.NORMAL, getWidth()/26));
                g.setTextAlign(TextAlignment.LEFT);
                g.fillText("score: " + score, getWidth()*0.05, getHeight()*0.95);
        }
        else {
            Platform.runLater( () -> {
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < columns; c++)
                    drawOneSquare(r,c);
            g.setFill(Color.WHITE);
            g.setFont(Font.font ("Courier New", FontWeight.NORMAL, getWidth()/26));
            g.setTextAlign(TextAlignment.LEFT);
            g.fillText("score: " + score, getWidth()*0.05, getHeight()*0.95);
            } );
        }

        try { // to avoid overwhelming the application thread with draw operations...
            Thread.sleep(1);
        }
        catch (InterruptedException e) {
        }
    }
    
    private void drawOneSquare(int row, int col) {

           // only called from two previous methods
        double rowHeight = getHeight() / rows;
        double colWidth = getWidth() / columns;
        int y = (int)Math.round(rowHeight*row);
        int h = Math.max(1, (int)Math.round(rowHeight*(row+1)) - y);
        int x = (int)Math.round(colWidth*col);
        int w = Math.max(1, (int)Math.round(colWidth*(col+1)) - x);
        
        int space = grid[row][col];
        Color c;

        switch (space) {
            case 2 -> c = foodColor;
            case 1 -> c = snake.getColor();
            default -> c = backgroundColor;
        }

        g.setFill(c);
        g.fillRect(x,y,w,h);
    }

    private void drawGameOver() {

        if (Platform.isFxApplicationThread()) {
            g.setFill(Color.WHITE);
            g.setFont(Font.font ("Courier New", FontWeight.BOLD, getWidth()/12));
            g.setTextAlign(TextAlignment.CENTER);
            g.fillText("GAME OVER", getWidth()*0.5, getHeight()*0.45);
            g.setFont(Font.font ("Courier New", FontWeight.NORMAL, getWidth()/26));
            g.fillText("click to play again", getWidth()*0.5, getHeight()*0.45 + 30);
        }
        else {
            Platform.runLater( () -> {
                g.setFill(Color.WHITE);
                g.setFont(Font.font ("Courier New", FontWeight.BOLD, getWidth()/12));
                g.setTextAlign(TextAlignment.CENTER);
                g.fillText("GAME OVER", getWidth()*0.5, getHeight()*0.45);
                g.setFont(Font.font ("Courier New", FontWeight.NORMAL, getWidth()/26));
                g.fillText("click to play again", getWidth()*0.5, getHeight()*0.45 + 30);
            } );
        }
    }



} // end class SnakeCanvas