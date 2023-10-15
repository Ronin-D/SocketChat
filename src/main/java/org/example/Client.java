package org.example;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static BufferedWriter out;
    private static BufferedReader in;
    private static String name;

    public static void main(String[] args){
        try {
            socket = new Socket("localhost",8081);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            readMessages();
            sendMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("enter your name: ");
                    name = scanner.nextLine();
                    out.write(name);
                    out.newLine();
                    out.flush();
                    while (!socket.isClosed()){
                        String msg = scanner.nextLine();
                        if (msg.equals("Leave")){
                            out.write(name+" left the chat");
                            out.newLine();
                            out.flush();
                            out.write("Leave");
                            out.newLine();
                            out.flush();
                            closeAll();
                        }
                        else{
                            out.write(msg);
                            out.newLine();
                            out.flush();
                        }
                    }
                } catch (IOException e) {

                }
            }
        }).start();

    }

    private static void readMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!socket.isClosed()){
                        String msg = in.readLine();
                        if (msg!=null){
                            System.out.println(msg);
                        }

                    }
                } catch (IOException e) {

                }
            }
        }).start();
    }
    private static void closeAll() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
