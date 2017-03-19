package opengl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class Utils {
    
    public static final String resourceFolder = "resources/";
    
    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = new FileInputStream(resourceFolder + fileName);
                Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }
}
