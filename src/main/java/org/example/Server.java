package org.example;

import org.cef.handler.CefClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static final int PORT=8081;
    public static ArrayList<ChatHandler> handlers = new ArrayList<>();

    public static void main(String[] args){
        try (
                ServerSocket serverSocket = new ServerSocket(PORT)
        ){
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("request received");
                ChatHandler chatHandler = new ChatHandler(socket);
                new Thread(chatHandler).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
