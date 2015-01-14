package fieldobject;

import java.util.List;

public class ScoringPlatform implements FieldObject {
    private final int height;
    private final int width;
    private final int depth;
    
    public ScoringPlatform() {
        height = 4;
        width = 12;
        depth = 8;
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
    public void setMaterial() {
        // TODO Auto-generated method stub

    }

    @Override
    public int getColor() {
        // TODO Auto-generated method stub
        return 0;
    }

}
