import javafx.scene.paint.Color;

/**
 * A class representing a food object in a game of snake. The location
 * of the food is stored in a Coordinate object. Eating a food causes a
 * snake to grow in length by 1.
 * 
 */

public class FoodData {
    
    //------------------ private instance variables --------------------

    private Coordinate location; // The coordinate of the food.
    private Color color; // The color of the food

    /**
     * Construct a FoodData object.
     */
    public FoodData(int x, int y, Color color) {
        location = new Coordinate(x, y);
        this.color = color;
    }

    //--------- methods for getting and setting snake properties ----------


    /**
     * Return the Coordinate of the FoodData object.
     */
    public Coordinate getLocation() {
        return location;
    }


    /**
     * Return the Color of the FoodData object.
     */
    public Color getColor() {
        return color;
    }


} // end of class FoodData
