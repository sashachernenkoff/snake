public class Coordinate {

    //------------ instance variables --------------

    private int x; // The x value of the coord.
    private int y; // The y value of the coord.

    /**
    * Construct a Coordinate. The x and y values MUST be provided.
    */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Check to see if two coordinates are equal by seeing if their
     * x and y values are the same.
     */
    public boolean equals(Coordinate c) {
        if (c.getX() == x && c.getY() == y)
            return true;
        else
            return false;
    }


    //--------- methods for getting and setting coordinate properties ----------


    /**
     * Return the x value of the coord
     */
    public int getX() {
        return x;
    }


    /**
     * Set the x value of the coord
     */
    public void setX(int x) {
        this.x = x;
    }


    /**
     * Return the y value of the coord
     */
    public int getY() {
        return y;
    }


    /**
     * Set the y value of the coord
     */
    public void setY(int y) {
        this.y = y;
    }

} // end of class Coordinate