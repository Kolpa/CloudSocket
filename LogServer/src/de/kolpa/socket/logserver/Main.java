package de.kolpa.socket.logserver;

import de.kolpa.socket.server.Log;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Kolpa on 05.12.2016.
 * Code might be terrible read at own discretion.
 */
public class Main {
    static Logger logger;

    private static void setupLogHandlers() {
        try {
            FileHandler logfile = new FileHandler("server.log");
            logfile.setFormatter(new SimpleFormatter());
            logger.addHandler(logfile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not Create Logfile");
        }
    }

    private static void setupRMI() throws RemoteException, AlreadyBoundException {
        LogSv logsv = new LogSv();
        Log logstub = (Log) UnicastRemoteObject.exportObject(logsv, 0);

        LocateRegistry.createRegistry(1099).rebind("Log", logstub);
    }

    public static void main(String[] args) {
        logger = Logger.getLogger("Socket Server Log");
        setupLogHandlers();

        logger.log(Level.INFO, "Logger Online");

        try {
            setupRMI();
            logger.log(Level.INFO, "RMI Online");
        } catch (RemoteException | AlreadyBoundException e) {
            logger.log(Level.SEVERE, "Failed to setup RMI", e);
        }
    }


}
