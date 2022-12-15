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
 * A SnakeCanvas object represents a grid containing rows
 * and columns of colored rectangles. Methods are provided
 * for getting and setting the colors of the rectangles. This
 * object does all the drawing for the program.
 */
public class SnakeCanvas extends Canvas {


    //------------------ private instance variables --------------------


    private final int rows, columns; // The number of rows and columns of rectangles in the grid.

    private Data[][] grid; // An array that contains the status of the game grid. Each space in
                           //    the grid is either associated with a SnakeData object, a FoodData
                           //    object, or is null (ie. blank).

    private Color backgroundColor = Color.rgb(40, 40, 40); // The color of the background.

    private int score; // The score of the current game.

    private boolean playing; // True if game is in progress, false otherwise. This
                             //    is used by the SnakeApp class to determine if a
                             //    game is in progress during key or mouse events.

    private Snake snake; // The snake on the board.

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
        grid = new Data[rows][columns];
        snake = new Snake(rows, columns);
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
    public int getRows() {
        return rows;
    }


    /**
     * Return the number of columns of rectangles in the grid.
     */
    public int getColumns() {
        return columns;
    }   


    /**
     * Return a reference to an element in the grid.
     */
    public Data getGridData(int row, int column) {
        return grid[row][column];
    }


    /**
     * Set the value of an element in the grid.
     */
    public void setGridData(int row, int column, Data data) {
        grid[row][column] = data;
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
    public void setPlaying(boolean bool) {
        playing = bool;
    }


    //------------------ other useful public methods ---------------------


    /**
     * Update the grid according to the snake's newest location and any
     * food on the board and redraw the board.
     */
    public void updateGrid() {

        // Move snake to it's next location.
        snake.update();

        // Check to see if the snake hit itself (the snake hit itself if the
        // location of the first SnakeData point in the snake is the same as
        // any other SnakeData point in the snake.
        for (int i = 1 ; i < snake.getSnakeArray().size() ; i++) {
            if (snake.getSnakeArray().get(i).sameLocation(snake.getSnakeArray().get(0))) {
                playing = false;
                return;
            }
        }

        // Check to see if the snake hit a wall
        if (snake.getSnakeArray().get(0).getY() < 0
                || snake.getSnakeArray().get(0).getY() >= rows
                || snake.getSnakeArray().get(0).getX() >= columns
                || snake.getSnakeArray().get(0).getX() < 0) { // going off the board, game over
                    playing = false;
                    return;
        }

        // Check to see if the snake hit any food item in the foodList
        if (!foodList.isEmpty()) {
            for (int i = 0 ; i < foodList.size() ; i++) {
                
                if (foodList.get(i).sameLocation(snake.getSnakeArray().get(0))) { // the snake ate the food
                    if (foodList.get(i) instanceof FoodData myFood)
                        score += myFood.getValue();
                    foodList.remove(foodList.get(i));
                    snake.eat();

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
        for (int i = 0; i < grid.length; i++) {
            Arrays.fill(grid[i], null);
        }

        // Update grid with the food locations
        if (!foodList.isEmpty()) {
            for (Data f : foodList) {
                grid[f.getY()][f.getX()] = f;
            }
        }

        // Update the grid with the snake's new location
        for (Data s : snake.getSnakeArray()) {
            grid[s.getY()][s.getX()] = s;
        }

        drawBoard();
    }


    /**
     * Reset the entire grid, snake, and any food. This is called when a game is restarted
     * and the player is starting from scratch.
     */
    public void resetGrid() {

        grid = new Data[rows][columns];
        snake = new Snake(rows, columns);
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
     * on a location that the snake currently occupies or on top of another
     * FoodData element.
     */
    public void addFood() {

        FoodData newFood;
        boolean inFoodList = false, inSnake = false;

        do {
            newFood = new FoodData((int)(Math.random()*columns), (int)(Math.random()*rows));
            for (FoodData f : foodList)
                if (f.sameLocation(newFood))
                    inFoodList = true;
            for (SnakeData s : snake.getSnakeArray())
                if (s.sameLocation(newFood))
                    inSnake = true;
        } while (inFoodList || inSnake);
        
        foodList.add(newFood);
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
        
        Data elem = grid[row][col];
        Color c;

        if (elem == null)
            c = backgroundColor;
        else
            c = elem.getColor();

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