package de.kolpa.socket.client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * Created by Kolpa on 09.11.2016 use at own risk might be horribly broken...
 */
public class SocketClient {
    private static final String PROTOCOL = "HTTP/1.0";

    private static Socket _socket;
    private static BufferedReader _input;
    private static BufferedWriter _output;

    private static void openPipes(String host) throws IOException {
        _socket = new Socket(host, 4444);
        _input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        _output = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
    }

    private static void close() throws IOException {
        _output.flush();
        _socket.close();
    }

    static File downloadFile(String host, String remotePath, String localPath) throws IOException {
        openPipes(host);

        _output.write("GET " + remotePath + " " + PROTOCOL);
        _output.newLine();
        _output.flush();

        String line = _input.readLine();

        if (line == null)
            throw new IOException("Remote Server Error");

        if (line.equals("500 Internal Server Error"))
            throw new IOException("Remote FIle not Found");

        File output = new File(localPath);
        BufferedWriter fwrite = new BufferedWriter(new FileWriter(output));

        while (line != null) {
            fwrite.write(line);
            fwrite.newLine();
            line = _input.readLine();
        }

        fwrite.flush();
        fwrite.close();

        close();

        return output;
    }

    static void uploadFile(String host, File file, String targetPath) throws IOException {
        openPipes(host);

        _output.write("PUT " + targetPath + " " + PROTOCOL);
        _output.newLine();

        Stream<String> lines = Files.lines(file.toPath());

        _output.write("Content-Length: " + lines.count());
        _output.newLine();

        lines = Files.lines(file.toPath());

        lines.forEach(str -> {
            try {
                _output.write(str);
                _output.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        _output.flush();

        if (!_input.readLine().equals("200 OK"))
            throw new IOException("Something went wrong on the Server");

        close();
    }
}
