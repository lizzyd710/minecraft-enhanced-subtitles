package lizzyd710.movablesubtitles;

/**
 * Enum for positions of the subtitle overlay.
 *
 * @author Elizabeth Sherrock
 */
public enum OverlayPosition {
    BOTTOM_RIGHT(0, 2.0F, 30),
    //TODO: Figure out real X and Y.
    BOTTOM_CENTER(1, 2.0F, 30),
    BOTTOM_LEFT(2, 2.0F, 30),
    CENTER_LEFT(3, 2.0F, 30),
    TOP_LEFT(4, 2.0F, 30),
    TOP_CENTER(5, 2.0F, 30),
    TOP_RIGHT(6, 2.0F, 30),
    CENTER_RIGHT(7, 2.0F, 30);

    private final int id;
    private final float xOffset;
    private final int yOffset;

    OverlayPosition(int i, float x, int y) {
        id = i;
        xOffset = x;
        yOffset = y;
    }

    /**
     * Returns the numerical id of the enum.
     *
     * @return the numerical id of the enum
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the x value used to transform the overlay into the enum's position
     *
     * @return the x value used to transform the overlay into the enum's position
     */
    public float getXOffset() {
        return this.xOffset;
    }

    /**
     * Returns the y value used to transform the overlay into the enum's position
     *
     * @return the y value used to transform the overlay into the enum's position
     */
    public int getYOffset() {
        return this.yOffset;
    }
}
