import javafx.scene.paint.Color;

/**
 * A class representing a FoodData object in a game of snake. Food objects are
 * a subclass of Data, distinguished only by their color.
 * 
 */

public class FoodData extends Data {
    
    //------------------ private instance variables --------------------

    private final int value;

    /**
     * Construct a FoodData object.
     */
    public FoodData(int x, int y) {
        super(x, y, Color.SIENNA);
        value = 10;
    }


    //--------- methods for getting and setting FoodData properties ----------


    /*
     * Return the value of the FoodData.
     */
    public int getValue() {
        return value;
    }


} // end of class FoodData
