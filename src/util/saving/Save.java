package util.saving;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class Save {

    /*
    * This class takes a list of objects and writes them to a file with the name given
    *
    * @precondition: objects must be serializable
     */

    private FileOutputStream fs;
    private File f;

    public Save(String fileName, Object...objects) {
        createFileStream(fileName);

        writeObjects(objects);
    }

    private void writeObjects(Object...objects) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(fs));

            for (Object o : objects) {
                output.writeObject(o);
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createFileStream(String fileName) {
        // creates FileOutputStream object with the correct absolute path
        // also creates the file
        try {
            f = new File(
                    SavePane.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .getPath()
                            .replace("%20", " ")
                            + "/data/saves/" + fileName
            );
            fs = new FileOutputStream(f.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // LOADING (STATIC) METHODS

    public static File[] getSaves() {
        return new File(Save.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()
                .replace("%20", " ")
                + "/data/saves").listFiles();
    }
    public static Object loadObject(String fileName) {
        return null; //TODO: fix this when implementing loading
    }
}
