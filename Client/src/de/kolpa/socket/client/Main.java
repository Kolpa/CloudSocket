package de.kolpa.socket.client;

import java.io.File;
import java.io.IOException;

/**
 * Created by Kolpa on 09.11.2016 use at own risk might be horribly broken...
 */
public class Main {

    public static void main(String[] args) throws IOException {
        SocketClient.uploadFile("ec2-35-165-25-225.us-west-2.compute.amazonaws.com", new File("C:\\Users\\Kolpa\\Desktop\\lodcfg.txt"), "testfile.txt");
        //SocketClient.downloadFile("ec2-52-25-45-214.us-west-2.compute.amazonaws.com", "testfile.txt", "testdl.txt");
    }
}
