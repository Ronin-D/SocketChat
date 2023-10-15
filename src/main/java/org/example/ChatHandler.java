package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatHandler implements Runnable{
    private final Socket socket;
    private final String clientUsername;
    private final BufferedReader in;
    private final BufferedWriter out;
    ChatHandler(Socket socket){
        try{
            this.socket = socket;
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientUsername = in.readLine();
            for (ChatHandler handler:Server.handlers){
                handler.write(clientUsername+" joined chat");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Server.handlers.add(this);
    }

    @Override
    public void run() {
        try {
            String inputText;
            while (!socket.isClosed()){
                inputText = in.readLine();
                if (inputText.equals("Leave")){
                    Server.handlers.removeIf(handler->(handler.equals(this)));
                    closeAll();
                }
                else{
                    for (ChatHandler handler:Server.handlers){
                        if (!handler.equals(this)){
                            handler.write(clientUsername+": "+inputText);
                        }
                    }
                }
            }
        } catch (IOException e) {

        }
    }

    public void write(String text) throws IOException {
        out.write(text);
        out.newLine();
        out.flush();
    }

    private void closeAll() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

}
