/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
