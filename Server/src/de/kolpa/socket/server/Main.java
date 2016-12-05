package de.kolpa.socket.server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;

/**
 * Created by Kolpa on 08.11.2016 use at own risk might be horribly broken...
 */
public class Main {
    static Log logger;

    private static void setupRMILog(String hostname) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(hostname);
        logger = (Log) registry.lookup("Log");
    }

    private static void setupLocalLog() {
        logger = new LocalLog();
    }

    public static void main(String[] args) {
        SocketServer serv = new SocketServer();

        try {
            setupRMILog(args[0]);
        } catch (RemoteException | NotBoundException e) {
            setupLocalLog();
            logger.writeLog(Level.SEVERE, "Could not connect to remote logging using local backup instead");
        }

        logger.writeLog(Level.INFO, "Logging Online");

        try {
            serv.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
