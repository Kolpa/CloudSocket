package de.kolpa.socket.client;

import java.io.File;
import java.io.IOException;

/**
 * Created by Kolpa on 09.11.2016 use at own risk might be horribly broken...
 */
public class Main {

    public static void main(String[] args) throws IOException {
        SocketClient.uploadFile("hostname", new File("C:\\Users\\Kolpa\\Desktop\\lodcfg.txt"), "testfile.txt");
        //SocketClient.downloadFile("hostname", "testfile.txt", "testdl.txt");
    }
}
