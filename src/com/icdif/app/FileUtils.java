package com.icdif.app;

/**
 * An auxiliar class that does operations with files and filenames
 * @author wanderer
 */
public class FileUtils {

	/**
	 * A static class that finds and returns the extension of a particular file
	 * @param fileName the filename to analyse
	 * @return the extension of filename
	 */
    public static String getExtension(String fileName) {
        int dotPos = fileName.lastIndexOf(".");
        String extension = fileName.substring(dotPos);

        return extension;
    }
}
