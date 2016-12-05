package de.kolpa.socket.server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by Kolpa on 08.11.2016 use at own risk might be horribly broken...
 */
public class SocketServerThread implements Runnable {
    private Socket _sock;
    private BufferedReader _input;
    private BufferedWriter _output;

    SocketServerThread(Socket sock) {
        _sock = sock;
    }

    @Override
    public void run() {
        try {
            _input = new BufferedReader(new InputStreamReader(_sock.getInputStream()));
            _output = new BufferedWriter(new OutputStreamWriter(_sock.getOutputStream()));

            Main.logger.writeLog(Level.INFO, "***********************************************");
            Main.logger.writeLog(Level.INFO, "***************GOT NEW CLIENT******************");

            String line = _input.readLine();

            if (line == null)
                throw new IOException("Empty Request");

            String[] segments = line.split(" ");

            if (segments.length != 3)
                throw new IOException("Wrong Request Structure");

            if (!segments[2].equals("HTTP/1.0"))
                throw new IOException("Wrong Protocol Stack");


            if (segments[0].equals("GET")) {
                handleGet(segments[1]);
                Main.logger.writeLog(Level.INFO, "***************HANDELED GET RQ*****************");
            }

            if (segments[0].equals("PUT")) {
                handlePut(segments[1]);
                Main.logger.writeLog(Level.INFO, "***************HANDELED PUT RQ*****************");
            }

        } catch (IOException e) {
            try {
                handleServerError(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                _output.flush();
                _input.close();
                _output.close();
                _sock.close();

                Main.logger.writeLog(Level.INFO, "***********************************************");
            } catch (IOException ignored) {
                Main.logger.writeLog(Level.INFO, "***********************************************");
            }
        }
    }

    private void handleServerError(IOException e) throws IOException {
        Main.logger.writeLog(Level.WARNING, "***************REQUEST ERROR*******************");
        Main.logger.writeLog(Level.WARNING, e.getMessage());

        _output.write("500 Internal Server Error");
        _output.newLine();
    }

    private void handlePut(String path) throws IOException {
        String line = _input.readLine();

        String[] info = line.split(" ");

        if (!info[0].equals("Content-Length:"))
            throw new IOException("400 Content Length Missing");

        int len = Integer.parseInt(info[1]);

        if (len < 0)
            throw new IOException("Invalid Content-Length");

        File file = new File(path);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        boolean dead = false;

        while (len > 0) {
            line = _input.readLine();

            if (line == null) {
                dead = true;
                break;
            }

            writer.write(line);
            writer.newLine();
            len--;
        }

        writer.flush();
        writer.close();

        if (dead) {
            file.delete();
            throw new IOException("Socket Closed while Reading");
        }

        _output.write("200 OK");
        _output.newLine();
    }

    private void handleGet(String path) throws IOException {
        File file = new File(path);

        BufferedReader fread = new BufferedReader(new FileReader(file));

        String line = fread.readLine();

        while (line != null) {
            _output.write(line);
            _output.newLine();
            line = fread.readLine();
        }

        fread.close();
    }
}
