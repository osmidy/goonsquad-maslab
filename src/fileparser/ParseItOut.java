package fileparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fieldobject.FieldObject;

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

    /**
     * Parse each line of the map file
     * 
     * @param file
     *            the map File to be parsed
     * @return a List of FieldObjects found on the given playing field
     * @throws IOException
     */
    public List<FieldObject> parseFile(File file) throws IOException {
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

    private static void parseWall(String[] tokens) {
        int x1 = Integer.parseInt(tokens[1]);
        int y1 = Integer.parseInt(tokens[2]);
        int x2 = Integer.parseInt(tokens[3]);
        int y2 = Integer.parseInt(tokens[4]);

    }

    private static void parsePlatform(String[] tokens) {
        int x1 = Integer.parseInt(tokens[1]);
        int y1 = Integer.parseInt(tokens[2]);
        int x2 = Integer.parseInt(tokens[3]);
        int y2 = Integer.parseInt(tokens[4]);

    }

    private static void parseStack(String[] tokens) {
        // TODO Auto-generated method stub

    }

    private static void parseHomeBase(String[] tokens) {
        // TODO Auto-generated method stub

    }

    private static void parseStartLocation(String[] tokens) {
        // TODO Auto-generated method stub

    }

}
