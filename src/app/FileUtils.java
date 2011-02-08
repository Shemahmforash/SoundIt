package app;

/**
 *
 * @author wanderer
 */
public class FileUtils {

    public static String getExtension(String fileName) {
        int dotPos = fileName.lastIndexOf(".");
        String extension = fileName.substring(dotPos);

        return extension;
    }
}
