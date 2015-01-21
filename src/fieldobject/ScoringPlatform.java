package fieldobject;

import java.util.List;

public class ScoringPlatform implements FieldObject {
    private final int height;
    private final int width;
    private final int depth;
    // Location on field
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final Color color = Color.RED;
    
    public ScoringPlatform(int x1, int y1, int x2, int y2) {
        height = 4;
        width = 12;
        depth = 8;
        
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public double toMeters(double measurement) {
        double ratio = 0.0254; // .0254 m/in
        double metric = ratio * measurement;
        return metric;
    }

    @Override
    public double getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getDepth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Integer> getDimensions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public int getX() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getY() {
        // TODO Auto-generated method stub
        return 0;
    }

}
