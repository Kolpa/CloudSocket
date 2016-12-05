package de.kolpa.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Kolpa on 08.11.2016 use at own risk might be horribly broken...
 */
public class SocketServer {
    private ServerSocket _serverSock;

    SocketServer() {
        try {
            _serverSock = new ServerSocket(4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void listen() throws IOException {
        while (true) {
            Socket sock = _serverSock.accept();

            new Thread(new SocketServerThread(sock)).start();
        }
    }
}
