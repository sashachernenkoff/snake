import java.util.ArrayList;

import javafx.scene.paint.Color;

/**
 * A class representing a Data object in the Snake game. This class is
 * used as the basis for Data objects that can appear on the game board.
 * 
 */

public abstract class Data {
        
    //------------------ private instance variables --------------------
    

    private int x, y; // The coordinates of the Data on the grid.
    private final Color color; // The color of the Data. Cannot be changed after being created.
    

    /**
     * Construct a FoodData object.
     */
    public Data(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }


    /**
     * Determine if two Data objects have the same x and y coordinates.
     */
    public boolean sameLocation(Data d) {
        if (d.getX() == x && d.getY() == y)
            return true;
        else
            return false;   
    }


        /**
     * Checks to see if a FoodData object already exists in an ArrayList
     * (by comparing the sets of coordinates in both objects).
     */
    public boolean isInList(ArrayList<Data> list) {
        
        boolean inList = false;

        if (!list.isEmpty()) {
            for (Data data: list) {
                if (this.sameLocation(data))
                    inList = true;
            }
        }

        return inList;
    }
    

    //--------- methods for getting and setting snake properties ----------
    
    
    /**
     * Return the x coord of the Data object.
     */
    public int getX() {
        return x;
    }


    /**
     * Return the y coord of the Data object.
     */
    public int getY() {
        return y;
    }


    /**
     * Set the x coord of the Data object.
     */
    public void setX(int x) {
        this.x = x;
    }


    /**
     * Set the y coord of the Data object.
     */
    public void setY(int y) {
        this.y = y;
    }
    
    
    /**
     * Return the Color of the Data object.
     */
    public Color getColor() {
        return color;
    }


} // end of class Data
