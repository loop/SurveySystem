package helpers;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for creating the application folders.
 */

public class AppFolderHelper
{
    private static final Logger log = Logger.getLogger(AppFolderHelper.class.getName());

    /**
     * Creates the application folder.
     */
    public static void createAppFolder()
    {
        File dir = new File(getAppFolder());

        if (dir.exists()) {
            log.log(Level.INFO, "Desktop server folder already exists.");
        } else {
            if (!dir.mkdirs()) {
                log.log(Level.INFO, "Desktop server folder creation error!");

            } else {
                log.log(Level.INFO, "Desktop server folder created successfully.");
            }
        }
    }

    public static String getAppFolder() {
        return "appdatafyp";
    }

    public static String getDatabaseName() {
        return "database.db";
    }
}