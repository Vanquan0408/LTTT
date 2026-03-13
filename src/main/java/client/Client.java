package client;

import java.net.Socket;
import java.io.*;

public class Client {

    public static Socket socket;
    public static DataInputStream dis;
    public static DataOutputStream dos;

    public static void connect(){

        try{

            socket = new Socket("172.31.99.61",9999);

            dis = new DataInputStream(socket.getInputStream());

            dos = new DataOutputStream(socket.getOutputStream());

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}