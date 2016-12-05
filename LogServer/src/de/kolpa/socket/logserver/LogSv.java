package de.kolpa.socket.logserver;

import java.util.logging.Level;

/**
 * Created by Kolpa on 05.12.2016.
 * Code might be terrible read at own discretion.
 */
public class LogSv implements de.kolpa.socket.server.Log {
    public void writeLog(Level level, String logmsg) {
        Main.logger.log(level, logmsg);
    }
}
