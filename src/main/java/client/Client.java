package client;

import java.net.Socket;
import java.io.*;

public class Client {

    public static Socket socket;
    public static DataInputStream dis;
    public static DataOutputStream dos;

    public static void connect(){

        try{

            socket = new Socket("127.0.0.1",1234);

            dis = new DataInputStream(socket.getInputStream());

            dos = new DataOutputStream(socket.getOutputStream());

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}