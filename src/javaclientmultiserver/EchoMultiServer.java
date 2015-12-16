/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaclientmultiserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mns
 */
class ConnectionHandler extends Thread {

    private Socket clientSocket = null;

    public ConnectionHandler(Socket soc) {
        this.clientSocket = soc;
        start();
    }

    @Override
    public void run() {
        System.out.println("Connection handler thread started !! \n");
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Server: " + inputLine);
                out.println(inputLine);

                if (inputLine.equals("Bye.")) {
                    break;
                }
            }

            out.close();
            in.close();
            clientSocket.close();

        } catch (IOException ex) {
            Logger.getLogger(EchoMultiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

public class EchoMultiServer {

    public static void main(String[] args) {
        int portNum = 9991;

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(portNum);
            System.out.println("Listening on port " + portNum);
        } catch (IOException ie) {
            System.err.println("Error listening at port " + portNum + "\n" + ie.getLocalizedMessage());
            System.exit(1);
        }

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ConnectionHandler(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

    }
}
