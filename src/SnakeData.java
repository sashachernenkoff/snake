import javafx.scene.paint.Color;

/**
 * A class representing a SnakeData object in a game of snake. Snake objects are
 * a subclass of Data, distinguished only by their color, but are used to make up
 * Snake objects on the game board.
 * 
 */

public class SnakeData extends Data {
    

    //------------------ private instance variables --------------------


    /**
     * Construct a SnakeData object.
     */
    public SnakeData(int x, int y) {
        super(x, y, Color.OLIVE);
    }


    //--------- methods for getting and setting SnakeData properties ----------


    /*
     * Methods are inherited from the Data class.
     */


} // end of class SnakeData
