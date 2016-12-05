package de.kolpa.socket.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Level;

/**
 * Created by Kolpa on 05.12.2016.
 * Code might be terrible read at own discretion.
 */
public interface Log extends Remote {
    void writeLog(Level level, String logmsg) throws RemoteException;
}
