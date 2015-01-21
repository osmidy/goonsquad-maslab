package fileparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fieldobject.Cube;
import fieldobject.FieldObject;
import fieldobject.FieldObject.Color;
import fieldobject.NeutralZone;
import fieldobject.ScoringPlatform;
import fieldobject.Stack;
import fieldobject.Wall;

/**
 * This class provides methods for identifying the layout of a physical playing
 * field, using a passed in map file. Map files are formatted as specified in
 * the game rules.
 * 
 * @author osmidy
 *
 */
public class ParseItOut {
    private final List<FieldObject> fieldObjects = new ArrayList<FieldObject>();
    //private final FieldObject[][] fieldMap = new FieldObject[][];
    private final int unitsToInches = 24;  // Initialize values in inches
    private final int startX;
    private final int startY;
    
    public ParseItOut(File file) {
        try {
            parseFile(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Parse each line of the map file
     * 
     * @param file
     *            the map File to be parsed
     * @return a List of FieldObjects found on the given playing field
     * @throws IOException
     */
    private List<FieldObject> parseFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String line = reader.readLine(); line != null; line = reader
                .readLine()) {
            String[] tokens = line.split(",");
            String type = tokens[0];
            if (type.equals("W")) {
                parseWall(tokens);
            }
            if (type.equals("P")) {
                parsePlatform(tokens);
            }
            if (type.equals("S")) {
                parseStack(tokens);
            }
            if (type.equals("H")) {
                parseHomeBase(tokens);
            }
            if (type.equals("L")) {
                parseStartLocation(tokens);
            }
        }
        return fieldObjects;
    }

    private void parseWall(String[] tokens) {
        int x1 = Integer.parseInt(tokens[1]) * unitsToInches;
        int y1 = Integer.parseInt(tokens[2]) * unitsToInches;
        int x2 = Integer.parseInt(tokens[3]) * unitsToInches;
        int y2 = Integer.parseInt(tokens[4]) * unitsToInches;
        Wall wall = new Wall(x1, y1, x2, y2);
        fieldObjects.add(wall);

    }

    private void parsePlatform(String[] tokens) {
        int x1 = Integer.parseInt(tokens[1]) * unitsToInches;
        int y1 = Integer.parseInt(tokens[2]) * unitsToInches;
        int x2 = Integer.parseInt(tokens[3]) * unitsToInches;
        int y2 = Integer.parseInt(tokens[4]) * unitsToInches;
        ScoringPlatform platform =  new ScoringPlatform(x1, y1, x2, y2);
        fieldObjects.add(platform);
        

    }

    private void parseStack(String[] tokens) {
        int x = Integer.parseInt(tokens[1]) * unitsToInches;
        int y = Integer.parseInt(tokens[2]) * unitsToInches;
        Cube bottomCube = parseCube(x, y, tokens[3]);
        Cube middleCube = parseCube(x, y, tokens[4]);
        Cube topCube = parseCube(x, y, tokens[5]);
        Stack stack = new Stack(x, y, bottomCube, middleCube, topCube);
        fieldObjects.add(stack);
    }
    
    private Cube parseCube(int x, int y, String color) {
        if (color.equals("R")) {
            return new Cube(x, y, Color.RED);
        } else if (color.equals("G")) {
            return new Cube(x, y, Color.GREEN);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void parseHomeBase(String[] tokens) {
        int n = Integer.parseInt(tokens[1]);
        List<Integer> endpoints = new ArrayList<Integer>();
        for (int i = 0; i < 2*n; i ++) {
            endpoints.add(Integer.parseInt(tokens[i+2])); // Index for x1 is 1 + 2 = 3
        }
        NeutralZone homeBase = new NeutralZone(endpoints);
    }

    private void parseStartLocation(String[] tokens) {
        startX = Integer.parseInt(tokens[1]);
        startY = Integer.parseInt(tokens[2]);

    }

}
