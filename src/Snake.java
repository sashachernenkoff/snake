import javafx.application.Application;
import javafx.application.Platform;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.List;


/**
 * The class SnakeApp makes available a window made up of a grid
 * of rectangles. Routines are provided for opening and closing the
 * window. The program will end when the window is closed, either
 * because the user click's the window's close box or because the
 * program calls Snake.close().
 *
 * Initially, the canvas is loaded as a black screen.
 */

public class Snake extends Application {

    private static Stage window; // The application running the snake window (if one is open).
    private static SnakeCanvas canvas; // A component that actually manages and displays the rectangles.

    enum Speed {
        SLOW,
        MEDIUM,
        FAST
      }

    //------------------ private instance variables: GAME SETTINGS --------------------

    private static Speed SPEED = Speed.MEDIUM; // The speed of the snake.
    private static int SIZE_W = 30, SIZE_H = 30; // The width and height of the game canvas.

    /** 
     * Open the snake window with a 30-by-30 grid of squares, where each
     * square is 15 pixel on a side. Has no effect if the window is already open.
     */
    public static void open() {
        open(30,30,16,16);
    }


    /**
     * Opens the snake window containing a specified number of rows and
     * a specified number of columns of square. Each square is 16 pixels
     * on a side.  Has no effect if the window is already open.
     */
    public static void open(int rows, int columns) {
        open(rows,columns,16,16);
    }


    /**
     * Opens the "snake" window on the screen. If the snake window was
     * already open, has no effect.
     *
     * Precondition: The parameters rows, cols, w, and h are positive integers, and
     *              the snake window is not already open.
     * Postcondition: A window is open on the screen that can display rows and
     *              columns of colored rectangles. Each rectangle is w pixels
     *              wide and h pixels high. The number of rows is given by
     *              the first parameter and the number of columns by the
     *              second. Initially, all rectangles are black.
     * Note:  The rows are numbered from 0 to rows - 1, and the columns are 
     * numbered from 0 to cols - 1.
     */
    public static void open(int rows, int columns, int blockWidth, int blockHeight) {
        if ( window != null )
            return;
        new Thread( () -> launch(Snake.class, new String[] {""+rows,""+columns,""+blockWidth,""+blockHeight}) ).start();
        do {
            delay(100);
        } while (window == null || canvas == null);
    }


    /**
     * Close the snake window, if one is open, and ends the program.
     * The program will also end if the user closes the window.
     */
    public static void close() {
        if (window != null) {
            Platform.runLater( () -> window.close() );
        }
    }


    /**
     * Tests whether the snake window is currently open.
     * 
     * Precondition: None.
     * Postcondition: The return value is true if the window is open when this
     *              function is called, and it is false if the window is
     *              closed.
     */
    public static boolean isOpen() {
        return (window != null);
    }


    /**
     * Inserts a delay in the program (to regulate the speed at which the snake
     * moves, for example). Note that there is already a short delay
     * of about 1 millisecond between drawing operations. Calling this method
     * will add to that delay.
     *
     * Precondition: milliseconds is a positive integer.
     * Postcondition: The program has paused for at least the specified number
     *              of milliseconds, where one second is equal to 1000
     *              milliseconds.
     */
    public static void delay(int milliseconds) {
        if (milliseconds > 0) {
            try { Thread.sleep(milliseconds); }
            catch (InterruptedException e) { }
        }
    }
    
    
    public void start(Stage stage) {
        window = stage;
        List<String> params = getParameters().getUnnamed();
        if (params.size() != 4)
            canvas = new SnakeCanvas();
        else
            canvas = new SnakeCanvas(Integer.parseInt(params.get(0)),Integer.parseInt(params.get(1)),
                    Integer.parseInt(params.get(2)),Integer.parseInt(params.get(3)));
        canvas.forceRedraw();
        Pane pane = new Pane(canvas);
        StackPane root = new StackPane(pane);
        root.setStyle("-fx-border-width: 2px; -fx-border-color: #333");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest( e -> { System.exit(0); } );
        stage.setTitle("snake");
        stage.setResizable(false);
        stage.show();

        scene.setOnKeyPressed( e -> {
            
            // if no game is in progress, restart a game
            if (canvas.getPlaying() == false) {
                canvas.resetGrid();
            }

            // change the direction of the snake on key press
            if (e.getCode() == KeyCode.UP) {
                canvas.moveSnake(0);
            }
            if (e.getCode() == KeyCode.RIGHT) {
                canvas.moveSnake(1);
            }
            if (e.getCode() == KeyCode.DOWN) {
                canvas.moveSnake(2);
            }
            if (e.getCode() == KeyCode.LEFT) {
                canvas.moveSnake(3);
            }
        });

        scene.setOnMousePressed( e -> {
            // if no game is in progress, restart a game
            if (canvas.getPlaying() == false) {
                canvas.resetGrid();
            }
        });
    }


    /**
     * The main program creates the window, fills it with the background color,
     * initializes the snake, and then moves the snake around as long as the
     * window is open. It also will randomly insert a FoodData at a random location
     * on the canvas, that is not on top of a snake.
     */
    public static void main(String[] args) {

        Snake.open(SIZE_W, SIZE_H, 16, 16);

        while (true) {
            if (Math.random() < 0.03) // 3% chance every time canvas refreshes to spawn a food
                canvas.addFood();

            canvas.updateGrid();
            canvas.getPlaying();

            switch (SPEED) {
                case SLOW -> {
                    Snake.delay(300);
                }
                case MEDIUM -> {
                    Snake.delay(150);
                }
                case FAST -> {
                    Snake.delay(75);
                }
            }

            if (!canvas.getPlaying()) {
                canvas.gameOver();
                while (!canvas.getPlaying()) {
                    try { // to avoid overwhelming the application thread ...
                        Thread.sleep(1);
                    }
                    catch (InterruptedException e) {
                    }
                }
            }
        }
    }  // end main

}  // end of class Snake