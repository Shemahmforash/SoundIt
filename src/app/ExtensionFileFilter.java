package app;

import java.io.File;
import javax.swing.filechooser.*;

/**
 * In order to have personalized fileFilters in a jFileChooser, one must extend
 * the abstract class FileFilter and define those filters in a new class.
 * This class is supposed to be used like this: ExtensionFileFilter filterSound = new ExtensionFileFilter("Wave and MP3", new String[]{"MP3", "WAV", "WAVE"});
 * @author wanderer
 */
public class ExtensionFileFilter extends FileFilter {

    String description;
    String extensions[];

    /**
     * Initializes this class
     * @param description the description of this filter
     * @param extension the extension allowed
     */
    public ExtensionFileFilter(String description, String extension) {
        this(description, new String[]{extension});
    }

    /**
     *
     * @param description the description of this filter
     * @param extensions an array of strings containing the extensions allowed
     */
    public ExtensionFileFilter(String description, String extensions[]) {
        if (description == null) {
            this.description = extensions[0];
        } else {
            this.description = description;
        }
        this.extensions = (String[]) extensions.clone();
        toLower(this.extensions);
    }

    /**
     * Converts an array of strings to lower case. It's useful to compare in easier way the extensions
     * @param array an array containing the paths of the files
     */
    private void toLower(String array[]) {
        for (int i = 0, n = array.length; i < n; i++) {
            array[i] = array[i].toLowerCase();
        }
    }

    /**
     * Gets the description of this filter
     * @return the description of this filter
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if a file has a valid extension
     * @param file the file to analyse
     * @return true if the file is valid, false otherwise
     */
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        } else {
            String path = file.getAbsolutePath().toLowerCase();
            for (int i = 0, n = extensions.length; i < n; i++) {
                String extension = extensions[i];
                //it the path ends with extension
                if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
                    return true;
                }
            }
        }
        return false;
    }
}
