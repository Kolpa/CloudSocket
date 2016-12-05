package de.kolpa.socket.server;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Kolpa on 05.12.2016.
 * Code might be terrible read at own discretion.
 */
class LocalLog implements Log {
    private static Logger logger;

    LocalLog() {
        logger = Logger.getLogger("Local Log");
        try {
            FileHandler fileHandler = new FileHandler("local.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not Create log File");
        }
    }

    @Override
    public void writeLog(Level level, String logmsg) {
        logger.log(level, logmsg);
    }
}
